%% PV Station Class Definition
% 06-September, 2011
% Adedamola Adepetu, aadepetu@masdar.ac.ae
%%
classdef PVStation < Behavior
    properties
        panel_length;
        panel_width;
        panel_capacity; %(kW)
        number_of_panels;
        annual_efficiency;
        annual_dni;
        plant_capacity;
        annual_energy_generated;
        annual_emissions_CO2;
        resource_use;
        finance;
        lifetime;
        AnnualHourlyEnergy;
        
        
    end
    methods
        function obj = PVStation()
            obj = obj@Behavior('PV power station', ...
                'Estimates the energy generated by a PV station and its capacity', ...
                'MWh','[0,inf)');
        end
        

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
            
            % Search for PV Station identifier in Energy Layer
            for i = 1:length(city.systems(sys_index).nodes)
                if strcmp(city.systems(sys_index).nodes(i).type.name,'PV Station')
                    % Store node ID
                    node_index = i;
                    break
                end
            end          
            SolarData =  xlsread('masdar_energy_1','GHI','A1:A8760');
            % Set PV Station Identifier
            pv_station = city.systems(sys_index).nodes(node_index);
        
            
            obj.panel_width = pv_station.GetNodeTypeAttributeValue('Panel Width');
            obj.panel_length = pv_station.GetNodeTypeAttributeValue('Panel Length');
            obj.annual_efficiency = pv_station.GetNodeTypeAttributeValue('PV Panel Efficiency');
            obj.annual_dni = pv_station.GetNodeTypeAttributeValue('Annual DNI');
            obj.number_of_panels = pv_station.GetNodeTypeAttributeValue('Number of panels');
            obj.panel_capacity = pv_station.GetNodeTypeAttributeValue('PV Panel Capacity');
            obj.lifetime = pv_station.GetNodeTypeAttributeValue('Plant Lifetime');
            obj.plant_capacity = obj.number_of_panels*obj.panel_capacity/(10^6);
            obj.annual_energy_generated = obj.annual_efficiency*obj.annual_dni*obj.panel_width*obj.panel_length*obj.number_of_panels/1000;
            
            obj.annual_emissions_CO2 = obj.annual_energy_generated*pv_station.GetNodeTypeAttributeValue('Specific CO2 Emissions')/1000; %Tonnes/year
            obj.finance.capex = pv_station.GetNodeTypeAttributeValue('Specific CAPEX') * obj.plant_capacity *1000; %$
            obj.finance.om = pv_station.GetNodeTypeAttributeValue('Specific O&M') * obj.plant_capacity *1000; %$
            obj.resource_use.land = obj.panel_width*obj.panel_length*obj.number_of_panels*1.5; %m2, assuming 1.5 times the size of the panels is required for the field
            obj.resource_use.land_per_mw = obj.resource_use.land/obj.plant_capacity; %m2 per MW
            obj.resource_use.water = 0;
            obj.resource_use.waste = 0;
            obj.resource_use.transport = 0;
            
            val = obj.annual_energy_generated; %in MWh/year
            annualHourlyOutput = zeros(8760,1);
            for hour = 1:8760
                annualHourlyOutput(hour) = pv_station.GetNodeTypeAttributeValue('PV Panel Efficiency')*SolarData(hour)*...
                                         pv_station.GetNodeTypeAttributeValue('Panel Length')*pv_station.GetNodeTypeAttributeValue('Panel Width')*...
                                         pv_station.GetNodeTypeAttributeValue('Number of panels');
           
            end
            obj.AnnualHourlyEnergy = annualHourlyOutput;
        end
    end
end
                  