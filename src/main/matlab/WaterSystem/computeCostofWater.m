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
% A = 0.95; %availability [%]
% z = 0.06; %discount factor
% n = 20; %amortization period [years], typically over life of plant
% 
% lambda = 4.5; %[kWh/m^3], electric energy intensity in SWRO, Ref [1], Table 2.1
% Yp = 0.07; %[$/kWh], price of electricity


function [specificCostOfWater,specificCapex,specificOpex,ElectricityCost]=computeCostOfWater(Wc, A, capexPerUnitVol, opexPerUnitVol,z, n, lambda,Yp)

%--------------compute capital costs---

capex = capexPerUnitVol*Wc;

psi = z/((1-(1+z)^(-1*n))); %annuity factor, [1/annum]

tau_eq = 365*A; %[days], equivalent utilization time, 


specificCapex = capex*psi/(Wc*tau_eq); %[$/m^3]

%%
%---------------compute operations costs----------------
opex = opexPerUnitVol*Wc;
specificOpex = opex/(Wc*tau_eq); %[$/m^3]
%%
%--------------compute electricity costs---------


ElectricityCost = lambda*Yp;

%%
specificCostOfWater = specificCapex + ElectricityCost + specificOpex;  %[$/m^3]

