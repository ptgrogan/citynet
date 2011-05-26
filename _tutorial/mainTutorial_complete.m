%% City.Net Synthesis Template - Tutorial Input File (Spreadsheet Reader)
% This file uses a spreadsheet-based input file to synthesize the
% quick start tutorial city (Boston) model.
%
% 25-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%

clear classes
close all
clc
addpath('..')

%% define synthesis properties
synthTemp = SynthesisTemplate.instance();
SpreadsheetReader.ReadTemplate('synthesisTutorial_complete.xls');

for i=1:length(synthTemp.city.cellRegions)
    synthTemp.city.cellRegions(i).GenerateCells(synthTemp.city);
end

for i=1:length(synthTemp.city.systems)
    system = synthTemp.city.systems{i};
    for j=1:length(system.nodeRegions)
        system.nodeRegions(j).GenerateNodes(system);
    end
    for j=1:length(system.edgeRegions)
        system.edgeRegions(j).GenerateEdges(system);
    end
end

%% render system
figure(1)
subplot(2,3,1)
synthTemp.RenderLayer(1);
subplot(2,3,2)
synthTemp.RenderLayer(2);
subplot(2,3,3)
synthTemp.RenderLayer(3);
subplot(2,3,4)
synthTemp.RenderLayer(4);
subplot(2,3,5)
synthTemp.RenderLayer(5);
subplot(2,3,6)
synthTemp.RenderLayer(6);