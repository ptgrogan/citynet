%% Energy Generation on a cell-by-cell basis
% 15-September, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef EnergyGeneration < Behavior
    properties
        cellEnergyGeneration = containers.Map('KeyType','int32','ValueType','double');
    end
    methods
        %% TransportationRecurringExpense Constructor
        % Instantiates a new TransportationRecurringExpense object.
        % 
        % obj = TransportationRecurringExpense()
        %   obj:            the new TransportationRecurringExpense object
        function obj = EnergyGeneration()
            obj = obj@Behavior('Energy Generation', ...
                ['Calcualtes the energy generated in ' ...
                'the energy system per year.'], ...
                'MWh','[0,inf)');
        end
        %% PlotCellEnergyGeneration Function
        % Plots the cell recurring expense behavior in a new figure.
        % 
        % obj.PlotCellRecurringExpense()
        function PlotCellEnergyGeneration(obj)
            figure
            title(['Energy Generation (' obj.units ')'])
            obj.PlotCellValueMap(obj.cellEnergyGeneration)
        end
    end
    methods(Access=protected)
        %% EvaluateImpl Function
        % Evaluates the behavior for a specified city. Note: the
        % superclass Evaluate function should be used for evaluation during
        % execution.
        %
        % val = obj.EvaluateImpl(city)
        %   val:    the evaluated value
        %   obj:    the TransportationFixedExpense object handle
        function val = EvaluateImpl(obj)
            val = 0;
            clear obj.cellEnergyGeneration
            obj.cellEnergyGeneration = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems(s).name,'Energy')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        energyVal=0;
                        
                        %%Check if there's a hydropowerstation in the cell
                        if strcmp(node.type.name,'Hydropower Station')
                           
                            plant_capacity = 0;
                            if node.GetNodeTypeAttributeValue('Plant Type') == 1 % impoundment type
                                plant_capacity = 9.81*node.GetNodeTypeAttributeValue('Head')*node.GetNodeTypeAttributeValue('Volumetric Flow Rate')...
                                    *node.GetNodeTypeAttributeValue('Efficiency')/1000;
                            elseif node.GetNodeTypeAttributeValue('Plant Type') == 2 % diversion/run of river type 
                                plant_capacity = (9.81*node.GetNodeTypeAttributeValue('Head') ...
                                    + 0.5*(node.GetNodeTypeAttributeValue('Outlet Water Speed')^2 - node.GetNodeTypeAttributeValue('Inlet Water Speed')^2))...
                                    *node.GetNodeTypeAttributeValue('Volumetric Flow Rate')*node.GetNodeTypeAttributeValue('Efficiency')/1000;
                            end

                            hydro_energy = plant_capacity*node.GetNodeTypeAttributeValue('Capacity factor')*8760;
                            energyVal = hydro_energy + energyVal;
                        end
                        
                        %%Check if there's a Biomass station in the cell
                        if strcmp(node.type.name,'Biomass Station')
                        plant_capacity = 0;
                        
                        if node.GetNodeTypeAttributeValue('Conversion Method') == 1
                            plant_capacity = node.GetNodeTypeAttributeValue('Steam Turbine Capacity');
                        else
                            plant_capacity = node.GetNodeTypeAttributeValue('Steam Turbine Capacity') + ...
                                node.GetNodeTypeAttributeValue('Gas Turbine Capacity');
                        end

                        
                        biomass_energy = node.GetNodeTypeAttributeValue('Full Load Hours of Operation') * plant_capacity;
                        energyVal = biomass_energy + energyVal;
                        end
                        
                        if obj.cellEnergyGeneration.isKey(node.cell.id)
                            obj.cellEnergyGeneration(node.cell.id) = obj.cellEnergyGeneration(node.cell.id) + energyVal;
                        else
                            obj.cellEnergyGeneration(node.cell.id) = energyVal;
                        end
                        val = val + energyVal;
                    end
                end
            end
        end
    end
end