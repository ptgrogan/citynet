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
        % obj.PlotCellEnergyGeneration(obj)
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
                        
                        
                        %%Check if there's a CSP station in the cell
                        if strcmp(node.type.name,'CSP Station')
                            
                            csp_energy = node.GetNodeTypeAttributeValue('Solar-to-Electric Efficiency')...
                            *node.GetNodeTypeAttributeValue('Annual DNI')*node.GetNodeTypeAttributeValue('Mirror Reflecting Area')...
                            *node.GetNodeTypeAttributeValue('Number of mirrors')/1000;
                            energyVal = csp_energy + energyVal;
                            
                        end
                        
                        %%Check if there's a PV station in the cell
                        if strcmp(node.type.name,'PV Station')
                            
                            pv_energy = node.GetNodeTypeAttributeValue('PV Panel Efficiency')*node.GetNodeTypeAttributeValue('Annual DNI')*...
                                node.GetNodeTypeAttributeValue('Panel Length')*node.GetNodeTypeAttributeValue('Panel Width')*...
                                node.GetNodeTypeAttributeValue('Number of panels')/1000;
                            energyVal = pv_energy + energyVal;
                        end
                        
                        %%Check if there's a wind farm in the cell
                        if strcmp(node.type.name,'Wind Farm')
                           
                            wind_speed_hours = zeros(1,25);
            
                            for k=1:25
                                wind_speed_hours(k) = node.GetNodeTypeAttributeValue(['Hours of wind speed at ', num2str(k-0.5)]); 
                            end

                            cut_in_speed = node.GetNodeTypeAttributeValue('Cut-in speed');
                            cut_out_speed = node.GetNodeTypeAttributeValue('Cut-out speed');
                            rated_speed = node.GetNodeTypeAttributeValue('Rated speed');
                            coefficient_of_performance = node.GetNodeTypeAttributeValue('Coefficient of performance');
                            turbine_blade_length = node.GetNodeTypeAttributeValue('Turbine blade length');
                            number_of_turbines = node.GetNodeTypeAttributeValue('Number of turbines');

                            energy_per_turbine = zeros(1,25);

                            for k=1:25
                               if k-0.5 < cut_in_speed
                                    energy_per_turbine(k) = 0;
                               elseif (k-0.5 >= cut_in_speed) && (k-0.5 <= rated_speed)
                                    energy_per_turbine(k) = pi*1.22521*coefficient_of_performance*(turbine_blade_length)^2*(k-0.5)^3*wind_speed_hours(k)/(2*10^6);
                               elseif (k-0.5 > rated_speed) && (k-0.5 <= cut_out_speed)
                                    energy_per_turbine(k) = pi*1.22521*coefficient_of_performance*(turbine_blade_length)^2*rated_speed^3*wind_speed_hours(k)/(2*10^6);
                               else
                                    energy_per_turbine(k) = 0;
                               end

                            end
                            wind_energy = sum(energy_per_turbine)*number_of_turbines;
                            energyVal = wind_energy + energyVal;
                            
                    
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