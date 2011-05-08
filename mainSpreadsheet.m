%% City.Net Synthesis Template - Input File (Spreadsheet Reader)
% This file uses a spreadsheet-based input file to synthesize the
% transportation system within a sample Masdar city model.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%

clear classes
close all
clc

%% define synthesis properties
synthTemp = SynthesisTemplate.instance();
SpreadsheetReader.ReadTemplate('masdar.xls');

for i=1:length(synthTemp.city.nodeRegions)
    synthTemp.city.nodeRegions(i).GenerateNodes();
end
for i=1:length(synthTemp.city.edgeRegions)
    synthTemp.city.edgeRegions(i).GenerateEdges();
end

%% render system
synthTemp.RenderCity;
synthTemp.RenderLayer(2);
synthTemp.RenderSystem(1);

%% shortest path calculation
path = synthTemp.city.systems{1}.GetShortestPath(1,80);
synthTemp.RenderSystemPath(1,path);
distance = synthTemp.city.systems{1}.GetPathDistance(path);
duration = synthTemp.city.systems{1}.GetPathDuration(path);