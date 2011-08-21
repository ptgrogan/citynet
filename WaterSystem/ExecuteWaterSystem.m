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
rW=TotalResidentialWater();
rW.Evaluate();

%rW

rC=TotalCommercialWater();
rC.Evaluate();

%rC

b = ResidentialWasteWater();
b.totalWaterBehavior = TotalResidentialWater();
b.Evaluate();