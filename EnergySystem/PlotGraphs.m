function PlotGraphs(A)

    n = length(A);
    values = zeros(6,n);
    for i=1:n
        %names(i) ={A{i}.name};
        values(2,i)=A{i}.value;
        values(3,i)=A{i}.plant_capacity;
        values(4,i)=A{i}.resource_use.land;
        values(5,i)=A{i}.resource_use.water;
        values(6,i)=A{i}.annual_emissions_CO2;
    end
    
%     label = {A{1}.name, A{2}.name, A{3}.name, A{4}.name, A{5}.name, A{6}.name};
    label = {A{1}.name, A{2}.name, A{3}.name};
    label2 = ['     PV       ','CSP      ', 'Wind     '];
%     label2 = ['     PV       ','CSP      ', 'Wind     ', 'Hydro     ' ,'Biomass   ', 'Natural Gas'];
%     explode = [0 0 0 0 0 1];
%     explode2 = [0 0 0 0 1 1];
    explode = [0 0 0 ];
    explode2 = [0 0 0 ];
    
    figure(2)
    bar(values(2,:));
    title('Annual Energy Generated','FontSize',14)
    ylabel('Energy (MWH/yr)')
    xlabel(label2)
    colormap cool
    
    
    figure(3)
    pie(values(2,:),explode,label);
    title('Annual Energy Generated','FontSize',14)
    
    figure(4)
    bar(values(3,:));
    title('Installed Plant Capacities','FontSize',14)
    ylabel('Plant Capacity (MW)')
    xlabel(label2)
    colormap cool
    
    
    figure(5)
    bar(values(4,:));
    title('Land Requirements','FontSize',14)
    ylabel('Land Area (m2)')
    xlabel(label2)
    colormap cool
    
    
    figure(6)
    bar(values(5,:));
    title('Annual Water Requirements','FontSize',14)
    ylabel('Water Use (Tonnes/year)')
    xlabel(label2)
    colormap cool
    
    
    figure(7)
    bar(values(6,:),'k','EdgeColor',[1 0.5 0.5]);
    title('Annual CO2 Emissions','FontSize',14)
    ylabel('CO2 emissions (Tonnes/year)')
    xlabel(label2)
%     colormap hot
%     
    
    figure(8)
    pie(values(6,:),explode2,label);
    title('Annual CO2 Emissions','FontSize',14)
    
%     figure(9)
% 
%     bar([rot90(values(2,:),-1), rot90(values(3,:),-1)], 'grouped');
%     
end