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
synthTemp = SynthesisTemplate.instance();
SpreadsheetReader.ReadTemplate('synthesisExample.xls');

synthTemp.GenerateCells();
synthTemp.GenerateNodes();
synthTemp.GenerateEdges();

%% render system
figure(1)
subplot(2,2,1)
synthTemp.RenderCells();
subplot(2,2,2)
synthTemp.RenderLayer(1);
subplot(2,2,3)
synthTemp.RenderLayer(2);
subplot(2,2,4)
synthTemp.RenderLayer(3);
figure(2)
synthTemp.RenderSystem(3);

%% shortest path calculation
path = synthTemp.city.systems{3}.GetShortestPathBetweenLocations([0.25 0.0],1,[1.3 2.1],1);
synthTemp.RenderSystemPath(3,path);
distance = synthTemp.city.systems{3}.GetPathDistance(path);
duration = synthTemp.city.systems{3}.GetPathDuration(path);