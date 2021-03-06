%% City.Net Synthesis Template - Tutorial Input File (Spreadsheet Reader)
% This file uses a spreadsheet-based input file to synthesize the
% quick start tutorial city (Boston) model.
%
% 30-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%

clear classes
close all
clc
addpath('..')

%% define synthesis properties
cityNet = CityNet.instance();
SpreadsheetReader.ReadTemplate('masdar_energy_1.xls');

cityNet.GenerateCells();
cityNet.GenerateNodes();
cityNet.GenerateEdges();

% figure(1)
% cityNet.RenderCity();
% % 
% figure(2)
% cityNet.RenderLayer(3);
% 
% figure(3)
% cityNet.RenderLayer(4);
% % 
% figure(4)
% cityNet.RenderLayer(5);

% clear b1
% b1 = PVStation();
% b1.Evaluate()
% 
clear b1
b1 = EnergySystem();
b1.Evaluate()

%KPI(b1)
% clear b2
% b2 = CSPStation();
% b2.Evaluate()
% 
% clear b3
% b3 = WindFarm();
% b3.Evaluate()
% % % 
% % clear b4
% % b4 = HydropowerStation();
% % b4.Evaluate()
% % 
% % clear b5
% % b5 = BiomassEnergy();
% % b5.Evaluate()
% % 
% % clear b6
% % b6 = NaturalGas();
% % b6.Evaluate()

% clear B
% B= {b1,b2,b3};
% PlotGraphs(B);
% KPI(B);

% clear b8
% b8 = EnergyGeneration();
% b8.Evaluate()
% b8.PlotCellEnergyGeneration()

% clear b9
% b9 = EnergyLandUse();
% b9.Evaluate()
% b9.PlotCellLandUse()
