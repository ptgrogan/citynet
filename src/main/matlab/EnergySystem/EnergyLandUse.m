classdef EnergyLandUse < Behavior
    properties
        cellLandUse = containers.Map('KeyType','int32','ValueType','double');
    end
    methods 

        function obj = EnergyLandUse()
            obj = obj@Behavior('Land Use', ...
                ['Calcualtes the land area occupied by ' ...
                'the energy system.'], ...
                'sq-km','[0,inf)');
        end
        
  %% PlotCellLandUse Function
        % Plots the cell recurring expense behavior in a new figure.
        % 
        % obj.PlotCellLandUse()
        function PlotCellLandUse(obj)
            figure
            title(['Land Use in Energy System (' obj.units ')'])
            obj.PlotCellValueMap(obj.cellLandUse)
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
        function energyLand = EvaluateImpl(obj)
            energyLand = 0;
            clear obj.cellLandUse
            obj.cellLandUse = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems(s).name,'Energy')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        energyLandUse=0;
                
                        
                        %%Check if there's a CSP station in the cell
                        if strcmp(node.type.name,'CSP Station')
                                                       
                            csp_land = node.GetNodeTypeAttributeValue('Mirror Length')...
                            *node.GetNodeTypeAttributeValue('Mirror Width')*node.GetNodeTypeAttributeValue('Number of mirrors')...
                            *node.GetNodeTypeAttributeValue('Area Multiplier')/10^(6); %Area in square km
                            energyLandUse = csp_land + energyLandUse;
                            
                        end
                        
                        %%Check if there's a PV station in the cell
                        if strcmp(node.type.name,'PV Station')
                                                        
                            pv_land = node.GetNodeTypeAttributeValue('Panel Length')*node.GetNodeTypeAttributeValue('Panel Width')*...
                                node.GetNodeTypeAttributeValue('Number of panels')*1.5/10^(6);
                            energyLandUse = pv_land + energyLandUse;
                        end
                        
                        %%Check if there's a wind farm in the cell
                        if strcmp(node.type.name,'Wind Farm')
                           

                            turbine_blade_length = node.GetNodeTypeAttributeValue('Turbine blade length');
                            number_of_turbines = node.GetNodeTypeAttributeValue('Number of turbines');

                            wind_land = number_of_turbines*4*6.5/10^(6)*(2*turbine_blade_length)^2;
                            energyLandUse = wind_land + energyLandUse;
                        end
                        
                       
                        if obj.cellLandUse.isKey(node.cell.id)
                            obj.cellLandUse(node.cell.id) = obj.cellLandUse(node.cell.id) + energyLandUse;
                        else
                            obj.cellLandUse(node.cell.id) = energyLandUse;
                        end
                        energyLand = energyLand + energyLandUse;
                    end
                end
            end
        end
    end
end