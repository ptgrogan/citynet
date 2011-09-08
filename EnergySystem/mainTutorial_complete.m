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
SpreadsheetReader.ReadTemplate('masdar_energy.xls');

cityNet.GenerateCells();
cityNet.GenerateNodes();
cityNet.GenerateEdges();

figure(1)
cityNet.RenderCity();

clear b1
b1 = PVStation();
b1.Evaluate()

clear b2
b2 = CSPStation();
b2.Evaluate()

clear b3
b3 = WindFarm();
b3.Evaluate()

clear b4
b4 = HydropowerStation();
b4.Evaluate()

clear b5
b5 = BiomassEnergy();
b5.Evaluate()

clear b6
b6 = NaturalGas();
b6.Evaluate()

clear B
B= {b1,b2,b3,b4,b5,b6};
PlotGraphs(B);


