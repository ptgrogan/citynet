function KPI(A)
    n = length(A);
    values = zeros(9,n);
    for i=1:n
        values(1,i)=A{i}.lifetime;
        values(2,i)=A{i}.value;
        values(3,i)=A{i}.plant_capacity;
        values(4,i)=A{i}.resource_use.land;
        values(5,i)=A{i}.resource_use.water;
        values(6,i)=A{i}.annual_emissions_CO2;
        values(7,i)=A{i}.finance.capex;
        values(8,i)=A{i}.finance.om;
        values(4,i)=A{i}.resource_use.land_per_mw;
    end

    label = {A{1}.name, A{2}.name, A{3}.name};
    label2 = ['     PV       ','CSP      ', 'Wind     '];
    
    LEC = zeros(1,n);
    CCI = zeros(1,n);
    CO2_save = zeros(1,n);
    interest=0.03;
    
    for i=1:n
        CO2_save(i) = values(2,i)*371/1000;
        CCI(i)= 100*values(7,i)/(values(1,i)*values(2,i)*1000);
        LEC(i)= 100*values(7,i)*interest/(values(2,i)*1000*(1-((1+interest)^(-values(1,i))))) + values(8,i)/(values(2,i)*1000);      
    end
    figure(9)
    bar(LEC,'r');
    title('Levelized Cost','FontSize',14)
    ylabel('cents/kWh)')
    xlabel(label2)
    
    
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
    
end