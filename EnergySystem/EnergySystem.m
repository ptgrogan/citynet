%% Energy System Class Definition
% 04 April 2012
% Adedamola Adepetu, aadepetu@masdar.ac.ae
%%
classdef EnergySystem < Behavior
    properties
        pv;
        csp;
        wind;
        hydro;
        naturalgas;
        biomass;
        load;
        grid;
        finance;
        water;
        waste;
        transportation;
        land;
        emissions;
        AnnualHourlyEnergy;
        output;
        
        
    end
    methods
        function obj = EnergySystem()
            obj = obj@Behavior('Energy Syste,', ...
                'Estimates the energy generated', ...
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
            
            
            SolarData =  xlsread('masdar_energy_1','GHI','C1:C8760');
            obj.pv.dni = SolarData;
            WindDataMeasure =  xlsread('masdar_energy_1','Wind Data','A1:A8760');
            obj.wind.wind_graph = WindDataMeasure;
            LoadData = xlsread('masdar_energy_1','Load Data','B2:M25');
            LoadDataIEEE = xlsread('masdar_energy_1','Load_Data_IEEE_RTS','B3:G26');
            
            Temperature = xlsread('masdar_energy_1','Temperature','B2:M25');
            % Set PV Station Identifier
            pv_station = city.systems(sys_index).nodes(node_index);
            clear node_index;
            
            obj.pv.dni = SolarData;
            obj.pv.panel_width = pv_station.GetNodeTypeAttributeValue('Panel Width');
            obj.pv.panel_length = pv_station.GetNodeTypeAttributeValue('Panel Length');
            obj.pv.panel_area = obj.pv.panel_width*obj.pv.panel_length;
            obj.pv.panel_efficiency = pv_station.GetNodeTypeAttributeValue('PV Panel Efficiency');
            obj.pv.number_of_panels = pv_station.GetNodeTypeAttributeValue('Number of panels');
            obj.pv.panel_capacity = pv_station.GetNodeTypeAttributeValue('PV Panel Capacity');
            obj.pv.lifetime = pv_station.GetNodeTypeAttributeValue('Plant Lifetime');
            obj.pv.rack_height = pv_station.GetNodeTypeAttributeValue('Rack Height');
            obj.pv.temperature_coefficient = pv_station.GetNodeTypeAttributeValue('Temperature Coefficient of Pmax');
            obj.pv.plant_capacity = obj.pv.number_of_panels*obj.pv.panel_capacity/(10^6);
            obj.pv.derate_factor = pv_station.GetNodeTypeAttributeValue('Derate Factor');

            obj.pv.bus = pv_station.GetNodeTypeAttributeValue('Bus Number');
            %val = obj.annual_energy_generated; %in MWh/year
            %annualHourlyOutput = zeros(8760,8);
            obj.pv.module_temperature = zeros(8760,1);
            obj.pv.energy_generated = zeros(8760,1);
            
            
            % Search for Wind Farm identifier in Energy Layer
            for i = 1:length(city.systems(sys_index).nodes)
                if strcmp(city.systems(sys_index).nodes(i).type.name,'Wind Farm')
                    % Store node ID
                    node_index = i;
                    break
                end
            end          
            
            % Set wind farm Identifier
            windfarm = city.systems(sys_index).nodes(node_index);
            clear node_index;
            
            obj.wind.cut_in_speed = windfarm.GetNodeTypeAttributeValue('Cut-in speed');
            obj.wind.cut_out_speed = windfarm.GetNodeTypeAttributeValue('Cut-out speed');
            obj.wind.rated_speed = windfarm.GetNodeTypeAttributeValue('Rated speed');
            obj.wind.coefficient_of_performance = windfarm.GetNodeTypeAttributeValue('Coefficient of performance');
            obj.wind.turbine_blade_length = windfarm.GetNodeTypeAttributeValue('Turbine blade length');
            obj.wind.lifetime = windfarm.GetNodeTypeAttributeValue('Plant Lifetime');
            obj.wind.number_of_turbines = windfarm.GetNodeTypeAttributeValue('Number of turbines');
            obj.wind.turbine_capacity = windfarm.GetNodeTypeAttributeValue('Wind Turbine Capacity');
            obj.wind.turbine_cutout_capacity = windfarm.GetNodeTypeAttributeValue('Wind Turbine Capacity at Cutout Speed');
            obj.wind.bus = windfarm.GetNodeTypeAttributeValue('Bus Number');
            obj.wind.hub_height = windfarm.GetNodeTypeAttributeValue('Hub Height');
            obj.wind.plant_capacity = obj.wind.number_of_turbines*obj.wind.turbine_capacity;
            
            wind_measured_height = xlsread('masdar_energy_1','Wind Data','B2');
            WindData = WindDataMeasure * (obj.wind.hub_height/wind_measured_height)^0.14;
            
            obj.wind.energy_generated = zeros(8760,1);
   
            % Search for Biomass Station identifier in Energy Layer
            for i = 1:length(city.systems(sys_index).nodes)
                if strcmp(city.systems(sys_index).nodes(i).type.name,'Biomass Station')
                    % Store node ID
                    node_index = i;
                    break
                end
            end    
           % Set Biomass Station Identifier
            biomass_station = city.systems(sys_index).nodes(node_index);
            clear node_index

            obj.biomass.conversion_method = biomass_station.GetNodeTypeAttributeValue('Conversion Method');
            obj.biomass.steam_turbine_capacity = biomass_station.GetNodeTypeAttributeValue('Steam Turbine Capacity');
            obj.biomass.gas_turbine_capacity = biomass_station.GetNodeTypeAttributeValue('Gas Turbine Capacity');
            obj.biomass.biomass_lhv = biomass_station.GetNodeTypeAttributeValue('Fuel LHV');
            obj.biomass.plant_efficiency = biomass_station.GetNodeTypeAttributeValue('Plant Conversion Efficiency');
            obj.biomass.full_load_hours = biomass_station.GetNodeTypeAttributeValue('Full Load Hours of Operation');
            obj.biomass.bus = biomass_station.GetNodeTypeAttributeValue('Bus Number');
            obj.biomass.lifetime = biomass_station.GetNodeTypeAttributeValue('Plant Lifetime');
            obj.biomass.energy_generated = zeros(8760,1);
            obj.biomass.biomass_flow_rate = 0;
            obj.biomass.hours_of_operation = 0;
            
            obj.biomass.schedule = xlsread('masdar_energy_1','Biomass Schedule','B2:M25');
            if obj.biomass.conversion_method == 1
                obj.biomass.plant_capacity = obj.biomass.steam_turbine_capacity;
            else
                obj.biomass.plant_capacity = obj.biomass.steam_turbine_capacity + obj.biomass.gas_turbine_capacity;
            end
            
%             % Search for NG Station identifier in Energy Layer
%             for i = 1:length(city.systems(sys_index).nodes)
%                 if strcmp(city.systems(sys_index).nodes(i).type.name,'Natural Gas Plant')
%                     % Store node ID
%                     node_index = i;
%                     break
%                 end
%             end          
%             
%             % Set NG Station Identifier
%             ngcc = city.systems(sys_index).nodes(node_index);
%             clear node_index
%             
%             obj.naturalgas.steam_turbine_capacity = ngcc.GetNodeTypeAttributeValue('Steam Turbine Capacity');
%             obj.naturalgas.gas_turbine_capacity = ngcc.GetNodeTypeAttributeValue('Gas Turbine Capacity');
%             obj.naturalgas.plant_efficiency = ngcc.GetNodeTypeAttributeValue('Power plant efficiency');
%             obj.naturalgas.capacity_factor = ngcc.GetNodeTypeAttributeValue('Plant Capacity Factor');
%             obj.naturalgas.heat_hhv = ngcc.GetNodeTypeAttributeValue('Heat of Combustion (HHV)');
%             obj.naturalgas.bus = ngcc.GetNodeTypeAttributeValue('Bus Number');
%             obj.naturalgas.plant_capacity = obj.naturalgas.steam_turbine_capacity + obj.naturalgas.gas_turbine_capacity;
%        
%             obj.naturalgas.net_heat_rate = 3600/obj.naturalgas.plant_efficiency;
%             obj.naturalgas.feed_rate = obj.naturalgas.net_heat_rate*24*obj.naturalgas.plant_capacity/obj.naturalgas.heat_hhv;
%             
%             obj.naturalgas.annual_energy_generated = obj.naturalgas.capacity_factor*8760*obj.naturalgas.plant_capacity;
%             
%          
%             obj.naturalgas.schedule = xlsread('masdar_energy_1','Natural Gas Schedule','B2:M25');
%             obj.naturalgas.annual_natural_gas = 0;
%             obj.naturalgas.hours_of_operation =0;
%             obj.naturalgas.energy_generated = zeros(1,8760);
            
            
            %%Check for buses
            number_of_buses = 5;
            IncomingBusData = zeros(number_of_buses,4);
            for j=1:number_of_buses
             for i = 1:length(city.systems(sys_index).nodes)
                if strcmp(city.systems(sys_index).nodes(i).type.name,['Bus ', num2str(j)])
                    % Store node ID
                    node_index = i;
                    break
                end
             end
             bus = city.systems(sys_index).nodes(node_index);
             IncomingBusData(j,:) = [j, bus.GetNodeTypeAttributeValue('Cell ID'), bus.GetNodeTypeAttributeValue('Bus Type'),bus.GetNodeTypeAttributeValue('Peak Load')];
            clear node_index 
            clear bus
            end
            obj.grid.bus_data=IncomingBusData;
            Sbase = 100; %MVA
            baseKV = 220; %kV
            Zbase = 1/Sbase*baseKV^2;
            power_factor=0.89;
            %%Declare Branch Data
            IncomingBranchData = xlsread('masdar_energy_1','Branch Data','A2:D7');
            number_of_branches = length(IncomingBranchData(:,1));
            BranchData = zeros(number_of_branches, 13);
            for b=1:number_of_branches
                fromBusCellID = IncomingBusData(IncomingBusData(:,1)==IncomingBranchData(b,1),2); %Get From Bus Cell ID
                toBusCellID = IncomingBusData(IncomingBusData(:,1)==IncomingBranchData(b,2),2); %Get To Bus Cell ID
                
                BranchData(b,:) = [IncomingBranchData(b,1:2), (IncomingBranchData(b,3:4))*...
                    distanceBetweenNodes(fromBusCellID, toBusCellID)/Zbase, 0, 250, 250, 250, 0, 0,1, -360,360]; %Get in edge data and convert R & X to p.u.
                clear fromBusCellID
                clear toBusCellID
            end
            
            %Declare Generator Data
            %GenData = [1, 0, 0, 300, -300, 1, 100, 1, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
            obj.grid.total_branch_losses = zeros(8760,1);
            obj.grid.voltages = zeros(8760,number_of_buses);
            obj.grid.injection = zeros(8760,2);
            for hour = 1:8760
                 Date = dateFromHour(hour);
                 month = Date(1);
                 time = Date(3);
                if ~isempty(pv_station)
                obj.pv.module_temperature(hour) = 0.943*Temperature(time,month)+ 0.028*10^3*SolarData(hour)-...
                                                 1.528*WindDataMeasure(hour)*(obj.pv.rack_height/wind_measured_height)^0.14 + 4.3;
                 
                obj.pv.energy_generated(hour) = SolarData(hour)/1000*obj.pv.derate_factor*obj.pv.panel_area*obj.pv.number_of_panels*obj.pv.panel_efficiency*...
                                            (1+(obj.pv.module_temperature(hour) - 25)*obj.pv.temperature_coefficient/100);%in MWh
             
                end
                
                if ~isempty(windfarm)
                    if (WindData(hour) < obj.wind.cut_in_speed)||(WindData(hour) > obj.wind.cut_out_speed)
                        energy_per_turbine = 0;
                    elseif (WindData(hour) >= obj.wind.cut_in_speed) && (WindData(hour) <= obj.wind.rated_speed)
                        energy_per_turbine = obj.wind.turbine_capacity*((WindData(hour)-obj.wind.cut_in_speed)/(obj.wind.rated_speed - obj.wind.cut_in_speed));
                    elseif (WindData(hour) > obj.wind.rated_speed) && (WindData(hour) <= obj.wind.cut_out_speed)
                        energy_per_turbine = obj.wind.turbine_capacity + (obj.wind.turbine_cutout_capacity - obj.wind.turbine_capacity)...
                            *(WindData(hour) - obj.wind.rated_speed)/(obj.wind.cut_out_speed - obj.wind.rated_speed);
                    else
                        energy_per_turbine = 0;
                    end
                    obj.wind.energy_generated(hour) = energy_per_turbine * obj.wind.number_of_turbines; %Energy Generated in MWh
                end
                
                %Do the Biomass Calculations
                if ~isempty(biomass_station)
                obj.biomass.biomass_flow_rate = obj.biomass.biomass_flow_rate + obj.biomass.plant_capacity*3.6*obj.biomass.schedule(time,month)...
                                /(obj.biomass.biomass_lhv*obj.biomass.plant_efficiency);
                obj.biomass.energy_generated(hour) = obj.biomass.plant_capacity*obj.biomass.schedule(time,month);
                obj.biomass.hours_of_operation = obj.biomass.hours_of_operation + obj.biomass.schedule(time,month);
                end
                
%                 %Do the Natural Gas Calculations
%                 obj.naturalgas.annual_natural_gas = obj.naturalgas.annual_natural_gas + obj.naturalgas.feed_rate*object.naturalgas.schedule(time,month);
%                 obj.naturalgas.energy_generated(hour) = obj.naturalgas.plant_capacity*object.naturalgas.schedule(time,month);
%                 obj.naturalgas.hours_of_operation = obj.naturalgas.hours_of_operation + obj.naturalgas.schedule(time,month);
                week = hour/(24*7);
                %Declare Bus Data
                BusData = zeros(number_of_buses, 13);
                for b=1:number_of_buses
                    if (week<=8 || week>=44)
                        BusData(b,:) = [b, IncomingBusData(b,3), LoadDataIEEE(time,1)*IncomingBusData(b,4)/100, LoadData(time,1)*IncomingBusData(b,4)*tan(acos(power_factor))/100, 0 0 1 1 0 baseKV, 1, 1.1, 0.9];
                    elseif (week>=18 || week<=30)
                        BusData(b,:) = [b, IncomingBusData(b,3), LoadDataIEEE(time,3)*IncomingBusData(b,4)/100, LoadData(time,3)*IncomingBusData(b,4)*tan(acos(power_factor))/100, 0 0 1 1 0 baseKV, 1, 1.1, 0.9];
                    else
                        BusData(b,:) = [b, IncomingBusData(b,3), LoadDataIEEE(time,5)*IncomingBusData(b,4)/100, LoadData(time,5)*IncomingBusData(b,4)*tan(acos(power_factor))/100, 0 0 1 1 0 baseKV, 1, 1.1, 0.9];
                    end
                    
                    
                    %BusData(b,:) = [b, IncomingBusData(b,3), LoadData(time,month), LoadData(time,month)*tan(acos(power_factor)), 0 0 1 1 0 baseKV, 1, 1.1, 0.9];
                end
                
                %%Declare Generator Data
                number_of_generators = ~isempty(biomass_station) + ~isempty(windfarm) + ~isempty(pv_station);
                GenData = zeros(number_of_generators+1, 21);
                
                GenData(1,1:10) = [1	0	0	300	-300	1	100	1	250	10];
                GenData(2,1:10) = [obj.biomass.bus, obj.biomass.energy_generated(hour), 0, 300, -300, 1, 100, 1, 250, 0];
                GenData(3,1:10) = [obj.pv.bus, obj.pv.energy_generated(hour,1), 0, 300, -300, 1, 100, 1, 250, 0];
                GenData(4,1:10) = [obj.wind.bus, obj.wind.energy_generated(hour), 0, 300, -300, 1, 100, 1, 250, 0];
                                
                %loadFlowText(BusData, GenData, BranchData);
                fileID = fopen(['case9_ies_', num2str(hour) '.m'],'w+');
                fprintf(fileID, ['function mpc = case9_ies_', num2str(hour) ' \n']);
                fprintf(fileID, 'mpc.version = ''2'' ; \n');
                fprintf(fileID, 'mpc.baseMVA = 100 ; \n');
                %input the bus details
                
                fprintf(fileID, 'mpc.bus = [ \n');
                [sizeBus,~]=size(BusData);
                
                for i=1:sizeBus
                    %fprintf(fileID,'%2.0f %2.0f %8.2f %8.2f %1.0f %1.0f %6.4f %6.4f %1.0f %8.4f %6.4f %6.4f %6.4f;\n', BusData);
                    fprintf(fileID,[num2str(BusData(i,1)) ' ' num2str(BusData(i,2)) ' ' num2str(BusData(i,3)) ' ' num2str(BusData(i,4)) ' ' num2str(BusData(i,5)) ' ' num2str(BusData(i,6)) ' ']);
                    fprintf(fileID,[num2str(BusData(i,7)) ' ' num2str(BusData(i,8)) ' ' num2str(BusData(i,9)) ' ' num2str(BusData(i,10)) ' ' num2str(BusData(i,11)) ' ' num2str(BusData(i,12)) ' ' num2str(BusData(i,13)) ';\n']);
                end
                fprintf(fileID, '];\n');
                
                %%input the generator details
                fprintf(fileID, 'mpc.gen = [\n');
                %fprintf(fileID,'%3.0f %8.2f %8.2f %8.2f %8.2f %2.0f %3.0f %2.0f %8.2f %8.2f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f;\n', GenData);
                [sizeGen,~]=size(GenData);
                
                for i=1:sizeGen
                    fprintf(fileID,[num2str(GenData(i,1)) ' ' num2str(GenData(i,2)) ' ' num2str(GenData(i,3)) ' ' num2str(GenData(i,4)) ' ' num2str(GenData(i,5)) ' ' num2str(GenData(i,6)) ' ']);
                    fprintf(fileID,[num2str(GenData(i,7)) ' ' num2str(GenData(i,8)) ' ' num2str(GenData(i,9)) ' ' num2str(GenData(i,10)) ' ' num2str(GenData(i,11)) ' ' num2str(GenData(i,12)) ' ' num2str(GenData(i,13)) ' ']);
                    fprintf(fileID,[num2str(GenData(i,14)) ' ' num2str(GenData(i,15)) ' ' num2str(GenData(i,16)) ' ' num2str(GenData(i,17)) ' ' num2str(GenData(i,18)) ' ' num2str(GenData(i,19)) ' ']);
                    fprintf(fileID,[num2str(GenData(i,20)) ' ' num2str(GenData(i,21)) ';\n']);
                end
                fprintf(fileID, '];\n');
                
                %%input the edge, i.e., powerline details
                fprintf(fileID, 'mpc.branch = [ \n');
                %fprintf(fileID,'%3.0f %3.0f %7.4f %7.4f %7.4f %3.0f %3.0f %3.0f %1.0f %1.0f %1.0f %4.0f %4.0f;\n', BranchData);
                [sizeBranch,~] = size(BranchData);
                for i=1:sizeBranch
                    fprintf(fileID,[num2str(BranchData(i,1)) ' ' num2str(BranchData(i,2)) ' ' num2str(BranchData(i,3)) ' ' num2str(BranchData(i,4)) ' ' num2str(BranchData(i,5)) ' ' num2str(BranchData(i,6)) ' ']);
                    fprintf(fileID,[num2str(BranchData(i,7)) ' ' num2str(BranchData(i,8)) ' ' num2str(BranchData(i,9)) ' ' num2str(BranchData(i,10)) ' ' num2str(BranchData(i,11)) ' ' num2str(BranchData(i,12)) ' ' num2str(BranchData(i,13)) ';\n']);
                end
                
                fprintf(fileID, '];\n');
                fprintf(fileID, 'mpc.gencost = [ \n');
                for i=1:sizeGen
                    fprintf(fileID, '2	1500	0	3	 0.11	5	150; \n');
                end
                fprintf(fileID, '];\n');
                %Close m file
                fclose(fileID);
       
                
            end
            
            for hour = 1:8760  
                results=runpf(['case9_ies_', num2str(hour)]);
                obj.grid.branch_losses(hour,1) = {[results.branch(:,1),results.branch(:,2), results.branch(:,14) + results.branch(:,16),results.branch(:,15) + results.branch(:,17)]};
                obj.grid.total_branch_losses(hour) = sum(results.branch(:,14) + results.branch(:,16)) + i*sum(results.branch(:,15) + results.branch(:,17));
                obj.grid.voltages(hour,:) = rot90(results.bus(:,8));
                obj.grid.injection(hour,:) = results.gen(1,2:3);
%                 annualHourlyOutput(hour,3) = energy_per_turbine * obj.wind.number_of_turbines;
%                 annualHourlyOutput(hour,1) = annualHourlyOutput(hour,3) + annualHourlyOutput(hour,2);
                clear results    
            end
            clear energy_per_turbine
            %Do the Annual Calculations for resources such as finance,
            %water, land, fuel, water, waste, and transportation (if any) +
            %emissions too
            
            %For Biomass
            obj.biomass.annual_emissions_CO2 = sum(obj.biomass.energy_generated)*biomass_station.GetNodeTypeAttributeValue('Specific CO2 Emissions')/1000; %Tonnes/year
            obj.biomass.finance.capex = biomass_station.GetNodeTypeAttributeValue('Specific CAPEX') * obj.biomass.plant_capacity *1000; %$
            obj.biomass.finance.om = biomass_station.GetNodeTypeAttributeValue('Specific O&M') * obj.biomass.plant_capacity *1000; %$
            obj.biomass.finance.fuel = biomass_station.GetNodeTypeAttributeValue('Specific Fuel Cost') * obj.biomass.biomass_flow_rate;
            obj.biomass.resource_use.land = biomass_station.GetNodeTypeAttributeValue('Specific Land Use') * obj.biomass.plant_capacity *1000; %m2
            obj.biomass.resource_use.water = biomass_station.GetNodeTypeAttributeValue('Specific Water Use') * sum(obj.biomass.energy_generated);
            obj.biomass.resource_use.waste = 0;
            obj.biomass.resource_use.transport = 0;
            
%             %For Natural Gas
%             obj.naturalgas.annual_emissions_CO2 = obj.naturalgas.energy_generated*ngcc.GetNodeTypeAttributeValue('Specific CO2 Emissions')/1000; %Tonnes/year
%             obj.naturalgas.finance.capex = ngcc.GetNodeTypeAttributeValue('Specific CAPEX') * obj.naturalgas.plant_capacity *1000; %$
%             obj.naturalgas.finance.om = ngcc.GetNodeTypeAttributeValue('Specific O&M') * obj.naturalgas.plant_capacity *1000; %$
%             obj.naturalgas.finance.fuel = ngcc.GetNodeTypeAttributeValue('Specific Fuel Cost') * obj.naturalgas.annual_natural_gas *1000;
%             obj.naturalgas.resource_use.land = ngcc.GetNodeTypeAttributeValue('Specific Land Use') * obj.naturalgas.plant_capacity *1000; %m2
%             obj.naturalgas.resource_use.water = ngcc.GetNodeTypeAttributeValue('Specific Water Use') * obj.naturalgas.energy_generated;
%             obj.naturalgas.resource_use.waste = 0;
%             obj.naturalgas.resource_use.transport = 0;          
            
            obj.wind.annual_emissions_CO2 = sum(obj.wind.energy_generated)*windfarm.GetNodeTypeAttributeValue('Specific CO2 Emissions')/1000; %Tonnes/year
            obj.wind.finance.capex = windfarm.GetNodeTypeAttributeValue('Specific CAPEX') * obj.wind.plant_capacity *1000; %$
            obj.wind.finance.om = windfarm.GetNodeTypeAttributeValue('Specific O&M') * obj.wind.plant_capacity *1000; %$
            obj.wind.resource_use.land = obj.wind.number_of_turbines*4*6.5*(2*obj.wind.turbine_blade_length)^2; %m2, assuming 4 diameters width and 6.5 diameters length per turbine
            obj.wind.resource_use.land_per_mw = obj.wind.resource_use.land/obj.wind.plant_capacity; %m2 per MW
            obj.wind.resource_use.water = 0;
            obj.wind.resource_use.waste = 0;
            obj.wind.resource_use.transport = 0;
            
                        
            obj.pv.annual_emissions_CO2 = sum(obj.pv.energy_generated)*pv_station.GetNodeTypeAttributeValue('Specific CO2 Emissions')/1000; %Tonnes/year
            obj.pv.finance.capex = pv_station.GetNodeTypeAttributeValue('Specific CAPEX') * obj.pv.plant_capacity *1000; %$
            obj.pv.finance.om = pv_station.GetNodeTypeAttributeValue('Specific O&M') * obj.pv.plant_capacity *1000; %$
            obj.pv.resource_use.land = obj.pv.panel_width*obj.pv.panel_length*obj.pv.number_of_panels*1.5; %m2, assuming 1.5 times the size of the panels is required for the field
            obj.pv.resource_use.land_per_mw = obj.pv.resource_use.land/obj.pv.plant_capacity; %m2 per MW
            obj.pv.resource_use.water = 0;
            obj.pv.resource_use.waste = 0;
            obj.pv.resource_use.transport = 0;
            
            %obj.AnnualHourlyEnergy = annualHourlyOutput;
            val = sum(obj.pv.energy_generated)+ sum(obj.biomass.energy_generated) + sum(obj.wind.energy_generated); %in MWh/year
            obj.output.energy_generated =  obj.pv.energy_generated+ obj.biomass.energy_generated + obj.wind.energy_generated;
           
            end
    end
end