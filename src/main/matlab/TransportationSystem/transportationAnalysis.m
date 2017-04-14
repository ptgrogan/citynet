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
SpreadsheetReader.ReadTemplate('masdar_transportation.xls');
cityNet.city.imagePath = 'master_plan.jpg'; % only .jpg is compatible

figure(1)
cityNet.RenderCity();

clear b1
b1 = TransportationLandUse();
b1.Evaluate()
b1.PlotCellLandUse();

clear b2
b2 = TransportationWaterUse();
b2.Evaluate()
b2.PlotCellWaterUse();

clear b3
b3 = TransportationEmissionsProduction();
b3.Evaluate()
b3.PlotCellEmissionsProduction();

clear b4
b4 = TransportationEnergyUse();
b4.Evaluate()
b4.PlotCellEnergyUse();

clear b5
b5 = TransportationFixedExpense();
b5.Evaluate()
b5.PlotCellFixedExpense();

clear b6
b6 = TransportationRecurringExpense();
b6.Evaluate()
b6.PlotCellRecurringExpense();

clear b7
b7 = QuickestPath([0.25 0.0 4],[1.3 2.1 4]);
b7.Evaluate()
figure
cityNet.RenderSystem(1);
cityNet.RenderSystemPath(1,b7.path);