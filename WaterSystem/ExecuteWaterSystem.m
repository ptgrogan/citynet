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