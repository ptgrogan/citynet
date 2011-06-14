%% City.Net Synthesis Template - Example Input File (Spreadsheet Reader)
% This file uses a spreadsheet-based input file to synthesize the
% transportation system within a sample Masdar city model.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%

clear classes
close all
clc
addpath('..');

%% define synthesis properties
cityNet = CityNet.instance();
SpreadsheetReader.ReadTemplate('synthesisExample.xls');

cityNet.GenerateCells();
cityNet.GenerateNodes();
cityNet.GenerateEdges();

%% render system
figure(1)
subplot(2,2,1)
cityNet.RenderCells();
subplot(2,2,2)
cityNet.RenderLayer(1);
subplot(2,2,3)
cityNet.RenderLayer(2);
subplot(2,2,4)
cityNet.RenderLayer(3);
figure(2)
cityNet.RenderSystem(3);

%% shortest path calculation

b = QuickestPath([0.25 0.0 1],[1.3 2.1 1]);
path = b.Evaluate(cityNet.city.systems(3));
cityNet.RenderSystemPath(3,path);