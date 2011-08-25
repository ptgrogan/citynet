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

clear b
b = TransportationLandUse();
b.Evaluate()
b.PlotCellLandUse();