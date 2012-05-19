function KPI(A)
    n = length(A);
    values = zeros(9,n);
    for i=1:n
        values(1,i)=A{i}.lifetime;
        values(2,i)=sum(A{i}.energy_generated);
        values(3,i)=A{i}.plant_capacity;
        values(4,i)=A{i}.resource_use.land;
        values(5,i)=A{i}.resource_use.water;
        values(6,i)=A{i}.annual_emissions_CO2;
        values(7,i)=A{i}.finance.capex;
        values(8,i)=A{i}.finance.om;
        values(9,i)=A{i}.finance.fuel;
        values(4,i)=A{i}.resource_use.land_per_mw;
    end

    %label = {A{1}.name, A{2}.name, A{3}.name};
    label2 = ['     PV       ','Biomass      ', 'Wind     '];
    
    LCOE = zeros(1,n);
    CCI = zeros(1,n);
    CO2_save = zeros(1,n);
    interest=0.05;
    
    
    for i=1:n
        CO2_save(i) = values(2,i)*371/1000;
        CCI(i)= 100*values(7,i)/(values(1,i)*values(2,i)*1000);
        %LCOE(i)= 100*values(7,i)*interest/(values(2,i)*1000*(1-((1+interest)^-(values(1,i))))) + (values(9,i)+ values(8,i))/(values(2,i)*1000);
        LCOE(i)=LEC(values(7,i), (values(8,i)+values(9,i)), values(1,i), values(2,i), interest);
    end
    figure(9)
    bar(LEC,'r');
    title('Levelized Cost','FontSize',14)
    ylabel('cents/kWh)')
    xlabel(label2)
    
    LCOE
    
    TotalCCI = sum(CCI);
    figure(10)
    bar(CCI);
    title(['Capital Cost Indicator= ' num2str(TotalCCI) 'cents/kWh'], 'FontSize' ,14)
    ylabel('cents/kWh)')
    xlabel(label2)
        
    CO2_savings = sum(values(2,:))*371/1000;
    
    figure(11)
    bar(CO2_save,'g');
    title(['Annual CO2 savings= ' num2str(CO2_savings) 'Tonnes'],'FontSize',14)
    text(11,380,['Total CO2 savings (Tonnes)' num2str(CO2_savings)])
    ylabel('Tonnes/year')
    xlabel(label2)
    
    totalAreaPerMW = sum(values(4,:));
    figure(12)
    bar(values(4,:),'b');
    title(['Total Area per MW ' num2str(totalAreaPerMW) 'm2/MW'],'FontSize',14)
    text(11,380,['Total Area per MW' num2str(totalAreaPerMW)])
    ylabel('m2/MW')
    xlabel(label2)
    

    
%     TotalEnergyGenerated =  system.pv.energy_generated + system.biomass.energy_generated + system.wind.energy_generated;
%     EnergySoldBack = -sum(system.grid.injection(:,1));
%     PercentEnergyFromGrid = TotalEnergyGenerated/(TotalEnergyGenerated-EnergySoldBack)*100; %percentage
%     GridLosses = sum(system.grid.total_branch_losses);
%     Emissions = system.biomass.annual_emissions_CO2 + system.pv.annual_emissions_CO2 + system.wind.annual_emissions_CO2;
%     WaterUse = system.biomass.resource_use.water + system.pv.resource_use.water + system.wind.resource_use.water; %Tonnes per year
%     LandOccupied = system.biomass.resource_use.land + system.pv.resource_use.land + system.wind.resource_use.land;
%                 plot obj.grid.branch_losses(hour,1) = {[results.branch(:,1),results.branch(:,2), results.branch(:,14) + results.branch(:,16),results.branch(:,15) + results.branch(:,17)]};
%                 plot obj.grid.voltages(hour,:) = rot90(results.bus(:,8));
%                  plot obj.grid.injection(hour,1) = results.gen(1,2:3);
%     
%                 
%                 obj.biomass.annual_emissions_CO2 = sum(obj.biomass.energy_generated)*biomass_station.GetNodeTypeAttributeValue('Specific CO2 Emissions')/1000; %Tonnes/year
%                 obj.biomass.finance.capex = biomass_station.GetNodeTypeAttributeValue('Specific CAPEX') * obj.biomass.plant_capacity *1000; %$
%                 obj.biomass.finance.om = biomass_station.GetNodeTypeAttributeValue('Specific O&M') * obj.biomass.plant_capacity *1000; %$
%                 obj.biomass.finance.fuel = biomass_station.GetNodeTypeAttributeValue('Specific Fuel Cost') * obj.biomass.biomass_flow_rate *1000;

end