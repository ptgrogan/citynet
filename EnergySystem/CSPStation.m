%% CSP Station Class Definition
% 06-September, 2011
% Adedamola Adepetu, aadepetu@masdar.ac.ae
%%
classdef CSPStation < Behavior
    properties
        csp_station_type;
        mirror_length;
        mirror_width;
        mirror_capacity;
%         solar_field_capacity;
%         storage_capacity;
        plant_capacity;
%         solar_multiple;
        number_of_mirrors;
        annual_efficiency;
        annual_dni;
%         csp_station_capacity;
        annual_energy_generated;
        annual_emissions_CO2;
        resource_use;
        finance;
        lifetime;
        area_multiplier;
        mirror_area;
        
        
    end
    methods
        function obj = CSPStation()
            obj = obj@Behavior('CSP station', ...
                'Estimates the energy generated by a CSP station and its capacity', ...
                'MWh','[0,inf)');
        end
        
%         function PlotCellBiomassEnergy(obj)
%             figure
%             title(['Biomass Energy (' obj.units ')'])
%             obj.PlotCellValueMap(obj.annual_energy_generated)
%         end
    end
    methods(Access=protected)
        %% EvaluateImpl Function
        
        function val = EvaluateImpl(obj)      
            
            % Set city identifier
            city = CityNet.instance().city;
            
            % Search for energy system identifier       
            for i = 1:length(city.systems)
                if strcmp(city.systems(i).name,'Energy')==1
                    sys_index = i;
                    break
                end
            end      
            
            % Search for CSP Station identifier in Energy Layer
            for i = 1:length(city.systems(sys_index).nodes)
                if strcmp(city.systems(sys_index).nodes(i).type.name,'CSP Station')
                    % Store node ID
                    node_index = i;
                    break
                end
            end          
            
            % Set CSP Station Identifier
            csp_station = city.systems(sys_index).nodes(node_index);
        
            obj.area_multiplier = csp_station.GetNodeTypeAttributeValue('Area Multiplier');
            obj.mirror_area = csp_station.GetNodeTypeAttributeValue('Mirror Reflecting Area');
            obj.mirror_width = csp_station.GetNodeTypeAttributeValue('Mirror Width');
            obj.mirror_length = csp_station.GetNodeTypeAttributeValue('Mirror Length');
            obj.annual_efficiency = csp_station.GetNodeTypeAttributeValue('Solar-to-Electric Efficiency');
            obj.annual_dni = csp_station.GetNodeTypeAttributeValue('Annual DNI');
            obj.number_of_mirrors = csp_station.GetNodeTypeAttributeValue('Number of mirrors');
            obj.mirror_capacity = csp_station.GetNodeTypeAttributeValue('Mirror Capacity');
            obj.lifetime = csp_station.GetNodeTypeAttributeValue('Plant Lifetime');
            obj.plant_capacity = obj.mirror_capacity*obj.number_of_mirrors/10^6; %MW
            obj.annual_energy_generated = obj.annual_efficiency*obj.annual_dni*obj.mirror_area*obj.number_of_mirrors/1000;
            
            obj.annual_emissions_CO2 = obj.annual_energy_generated/1000*csp_station.GetNodeTypeAttributeValue('Specific CO2 Emissions'); %Tonnes/year
            obj.finance.capex = csp_station.GetNodeTypeAttributeValue('Specific CAPEX') * obj.plant_capacity *1000; %$
            obj.finance.om = csp_station.GetNodeTypeAttributeValue('Specific O&M') * obj.plant_capacity *1000; %$
            obj.resource_use.land = obj.mirror_width*obj.mirror_length*obj.number_of_mirrors*obj.area_multiplier;
            obj.resource_use.land_per_mw = obj.resource_use.land/obj.plant_capacity; %m2 per MW
            obj.resource_use.water = csp_station.GetNodeTypeAttributeValue('Specific Water Use') * obj.annual_energy_generated;
            obj.resource_use.waste = 0;
            obj.resource_use.transport = 0;
            
            
            val = obj.annual_energy_generated;
            
        end
    end
end
    