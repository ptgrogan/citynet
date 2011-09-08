%% Natural Gas Combined Cycle Class Definition
% 06-September, 2011
% Adedamola Adepetu, aadepetu@masdar.ac.ae
%%
classdef NaturalGas < Behavior
    properties
        %conversion_method;          % if value is 1, Combustion/Steam Turbine cycle and if its a 2, its a Gasification/Gas-Steam Combined Cycle (input)
        NOx_removal_method;
        gas_turbine_capacity;       % capacity of gas turbine for gas-steam combined cycle (MW) (input)
        steam_turbine_capacity;     % capacity of steam turbine (MW) (input)
        plant_capacity;             % biomass plant rated capacity (MW)
        natural_gas_lhv;            % the lower heating value of the biomass fuel used in (kJ/kg) (input)
        plant_efficiency;           % the annual efficiency of the biomass plant (input)
        feed_rate;                  % the amount of fuel required annualy (Tonne/year)
        net_heat_rate;
        capacity_factor;
        heat_hhv;
        annual_energy_generated;    % The amount of electricity generated from the power plant annualy (MWh)
        annual_natural_gas;
        annual_emissions_CO2;
        resource_use;
        finance;
        
    end
    methods
        function obj = NaturalGas()
            obj = obj@Behavior('Natural Gas Power Plant', ...
                'Estimates the energy generated by a natural gas power plant, its capacity and the amount of fuel required annualy', ...
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
            
            % Search for NG Station identifier in Energy Layer
            for i = 1:length(city.systems(sys_index).nodes)
                if strcmp(city.systems(sys_index).nodes(i).type.name,'Natural Gas Plant')
                    % Store node ID
                    node_index = i;
                    break
                end
            end          
            
            % Set Biomass Station Identifier
            ngcc = city.systems(sys_index).nodes(node_index);
            
            
            obj.steam_turbine_capacity = ngcc.GetNodeTypeAttributeValue('Steam Turbine Capacity');
            obj.gas_turbine_capacity = ngcc.GetNodeTypeAttributeValue('Gas Turbine Capacity');
            obj.plant_efficiency = ngcc.GetNodeTypeAttributeValue('Power plant efficiency');
            obj.capacity_factor = ngcc.GetNodeTypeAttributeValue('Plant Capacity Factor');
            obj.heat_hhv = ngcc.GetNodeTypeAttributeValue('Heat of Combustion (HHV)');
            obj.plant_capacity = obj.steam_turbine_capacity + obj.gas_turbine_capacity;
       
            obj.net_heat_rate = 3600/obj.plant_efficiency;
            obj.feed_rate = obj.net_heat_rate*24*obj.plant_capacity/obj.heat_hhv;
            obj.annual_natural_gas = obj.feed_rate*24*obj.capacity_factor;
            obj.annual_energy_generated = obj.capacity_factor*8760*obj.plant_capacity;
            
            obj.annual_emissions_CO2 = obj.annual_energy_generated*ngcc.GetNodeTypeAttributeValue('Specific CO2 Emissions')/1000; %Tonnes/year
            obj.finance.capex = ngcc.GetNodeTypeAttributeValue('Specific CAPEX') * obj.plant_capacity *1000; %$
            obj.finance.om = ngcc.GetNodeTypeAttributeValue('Specific O&M') * obj.plant_capacity *1000; %$
            obj.finance.fuel = ngcc.GetNodeTypeAttributeValue('Specific Fuel Cost') * obj.annual_natural_gas *1000;
            obj.resource_use.land = ngcc.GetNodeTypeAttributeValue('Specific Land Use') * obj.plant_capacity *1000; %m2
            obj.resource_use.water = ngcc.GetNodeTypeAttributeValue('Specific Water Use') * obj.annual_energy_generated;
            obj.resource_use.waste = 0;
            obj.resource_use.transport = 0;
            
            val = obj.annual_energy_generated;
            
        end
    end
end
    
