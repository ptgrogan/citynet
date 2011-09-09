%% Script for executing water system model
% This script runs the various functions related to CityNet's Water System
% model. It can be used for testing and evaluation purposes.

% August 21, 2011
% Afreen Siddiqi, siddiqi@mit.edu

%%
close all
clear all

cityNet = CityNet.instance();

addpath('..')
SpreadsheetReader.ReadTemplate('synthesisTutorial_complete_water.xls');

cityNet.GenerateCells();
cityNet.GenerateNodes();
cityNet.GenerateEdges();

%execute residential and commercial water demand functions
resWater=TotalResidentialWater();
resWater.Evaluate();

comWater=TotalCommercialWater();
comWater.Evaluate();

%execute residential and commercial wastewater estimates functions

resWasteWater = ResidentialWasteWater();
resWasteWater.totalResidentialWaterBehavior = TotalResidentialWater();
resWasteWater.Evaluate();

comWasteWater = CommercialWasteWater();
comWasteWater.totalCommercialWaterBehavior = TotalCommercialWater();
comWasteWater.Evaluate();

SWROplant = SWROfacility();
SWROplant.Evaluate();

MBRplant = MBRfacility();
MBRplant.Evaluate();

%plot outputs
figure
subplot(2,2,1)
bar([resWater.kitchen, resWater.faucets, resWater.shower, resWater.toilets, resWater.laundry],'c')
title(strcat('Annual Residential Water Demand: ',num2str(resWater.value),' m^3'))
ylabel('m^3')
set(gca,'XTickLabel',{'kitchen','faucets','shower','toilets','laundry'})
grid on

subplot(2,2,3)
bar([resWasteWater.greywater, resWasteWater.blackwater],0.5,'b')
title('Annual Residential Waste Water Generation')
ylabel('m^3')
set(gca,'XTickLabel',{'grey','black'})
grid on

subplot(2,2,2)
bar([comWater.kitchen, comWater.faucets, comWater.toilets],'c')
title(strcat('Annual Commercial Water Demand: ',num2str(comWater.value),' m^3'))
ylabel('m^3')
set(gca,'XTickLabel',{'kitchen','faucets','toilets'})
grid on

subplot(2,2,4)
bar([comWasteWater.greywater, comWasteWater.blackwater],0.5,'b')
title('Annual Commercial Waste Water Generation')
ylabel('m^3')
set(gca,'XTickLabel',{'grey','black'})
grid on

figure
subplot(2,1,1)
%blue water, water demand
bar([resWater.kitchen, resWater.faucets, resWater.shower, resWater.toilets, resWater.laundry;...
    comWater.kitchen, comWater.faucets, 0, comWater.toilets, 0]','grouped')
title('Water demand')
ylabel('m^3')
grid on
legend('residential','commercial')
set(gca,'XTickLabel',{'kitchen','faucets','shower','toilets','laundry'})

subplot(2,1,2)
bar([resWasteWater.greywater, resWasteWater.blackwater;...
    comWasteWater.greywater, comWasteWater.blackwater]','grouped')
title('Waste Water Generation')
grid on
ylabel('m^3')
legend('residential','commercial')
set(gca,'XTickLabel',{'grey','black'})
grid on




figure
subplot(2,2,1)
bar([SWROplant.electricEnergy, MBRplant.electricEnergy],0.5,'r')
title('water facilities annual electric energy use')
ylabel('kWh')
set(gca,'XTickLabel',{'SWRO plant','MBR plant'})
grid on

subplot(2,2,2)
bar([SWROplant.landfootprint, MBRplant.landfootprint],0.5,'g')
title('water facilities land use')
ylabel('m^2')
set(gca,'XTickLabel',{'SWRO plant','MBR plant'})
grid on

subplot(2,2,3)
bar([SWROplant.costOfProducedWater, MBRplant.costOfTreatedWater],0.5,'k')
title('water costs per unit volume')
ylabel('$/m^3')
set(gca,'XTickLabel',{'SWRO plant','MBR plant'})
grid on
