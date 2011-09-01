%Description: 
%This function computes cost of water produced from
%a desalination Plant.

%Author: Afreen Siddiqi <siddiqi@mit.edu>

%Date: August 23, 2011

%References: 
%[1] Desalination and Advanced Water Treatment: Economics and Financing by Corrado Sommariva, 2010
%[2] Desalination: A National Perspective, National Research Council, 2008

%%
%Inputs:

% capexPerUnitVol = 2100; %[$/(m^3/day)], Ref: [1], Figure 10.2
% Wc = 15000; % rated water output, [m^3/day]
% z = 0.06; %discount factor
% n = 20; %amortization period [years], typically over life of plant
% 
% lambda = 4.5; %[kWh/m^3], electric energy intensity in SWRO, Ref [1], Table 2.1
% Yp = 0.07; %[$/kWh], price of electricity


function [specificCostOfWater,specificCapex,specificOpex,ElectricityCost]=computeCostOfWater(capexPerUnitVol, Wc, z, n, lambda,Yp)

%--------------compute capital costs---

capex = capexPerUnitVol*Wc;

psi = z/((1-(1+z)^(-1*n))); %annuity factor, [1/annum]

tau_eq = 8700*0.95/24; %[days], equivalent utilization time, 
%assuming annual 8700 hrs normal operating time and 95% availability

specificCapex = capex*psi/(Wc*tau_eq); %[$/m^3]

%%
%---------------compute operations costs----------------

%Reference: [2], Table 6-2
PartsAndMaintenance = 0.03; %[$/m^3]
Chemicals = 0.07; %[$/m^3]
Labor = 0.1; %[$/m^3]
Membranes = 0.03;  %[$/m^3]

specificOpex = PartsAndMaintenance+Chemicals+Labor+Membranes; %[$/m^3]

%%
%--------------compute electricity costs---------


ElectricityCost = lambda*Yp;

%%
specificCostOfWater = specificCapex + ElectricityCost + specificOpex  %[$/m^3]

