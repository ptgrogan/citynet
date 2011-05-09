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

for i=1:length(synthTemp.city.systems)
    system = synthTemp.city.systems{1};
    for j=1:length(system.nodeRegions)
        system.nodeRegions(j).GenerateNodes(system);
    end
    for j=1:length(system.edgeRegions)
        system.edgeRegions(j).GenerateEdges(system);
    end
end

%% render system
synthTemp.RenderCity;

%% shortest path calculation
path = synthTemp.city.systems{1}.GetShortestPath(1,80);
synthTemp.RenderSystemPath(1,path);
distance = synthTemp.city.systems{1}.GetPathDistance(path);
duration = synthTemp.city.systems{1}.GetPathDuration(path);