%Author: Afreen Siddiqi <siddiqi@mit.edu>

%Description: 
%This script plots cost of water for varying input values of discount rate,
%plant life etc.

%Date: August 23, 2011


%References: 
%[1] Desalination and Advanced Water Treatment: Economics and Financing by Corrado Sommariva, 2010
%[2] Desalination: A National Perspective, National Research Council, 2008

clear all
close all


Wc = 15000; % rated water output, [m^3/day], assuming case for Madar
A = 0.95; %Availability

capexPerUnitVol = 2100; %[$/(m^3/day)], Ref: [1], Figure 10.2, for a SWRO plant

z = 0.06:0.01:0.1; %discount factor

n = 20:5:30; %amortization period [years], typically over life of plant


lambda = 4.5; %[kWh/m^3], electric energy intensity in SWRO, Ref [1], Table 2.1


Yp = 0.05:0.01:0.1; %[$/kWh], price of electricty

Cw = zeros(length(Yp),length(z),length(n));
specificCapex =zeros(length(Yp),length(z),length(n));
specificOpex =zeros(length(Yp),length(z),length(n));
ElectricityCost =zeros(length(Yp),length(z),length(n));

for YInd=1:length(Yp)  
    for zInd=1:length(z)
        for nInd=1:length(n)
            
            
            [Cw(YInd,zInd,nInd),specificCapex(YInd,zInd,nInd),specificOpex(YInd,zInd,nInd),ElectricityCost(YInd,zInd,nInd)]...
                =computeCostOfWater(Wc, A, capexPerUnitVol, z(zInd), n(nInd), lambda,Yp(YInd));
            
            
            
        end
    end
end

figure
plot(squeeze(Cw(:,:,1)),'-s')
grid on
xlabel('Price of Electricity [$/kWh]')
set(gca,'XTick',1:length(Yp))
set(gca,'XTickLabel',num2str(Yp'))
ylabel('Cost of Water [$/ m^3]')
legend(num2str(z'))
% hold on
% plot(squeeze(Cw(:,2,:)),'-*')
title('Cost of SWRO Desalinated Water for Varying Electricty Price and Discount Rates')

figure
bar(squeeze(specificCapex(1,:,:)),'stacked')



            
