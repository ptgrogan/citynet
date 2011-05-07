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

synthTemp.nodeTypes(end+1) = NodeType('Test','A test node type',[0 1 0]);
synthTemp.edgeTypes(end+1) = EdgeType('Test','A test edge type',[0 .5 0]);
synthTemp.city.layers(end+1) = Layer('Test','A test layer',4);
test = NodeRegion(1,1,6,4,[0.25 0.30 1.35 1.27],[0.50 1.15 1.00 0.80]);
test.GenerateNodes();
test = EdgeRegion(1,1,4,[4 4 4 4],[0.25 0.30 1.35 1.27],[0.50 1.15 1.00 0.80],EdgeRegion.FULLY_CONNECTED,0);
test.GenerateEdges();


%% render system
synthTemp.RenderCity;
synthTemp.RenderLayer(2);
synthTemp.RenderSystem(1);

%% shortest path calculation
% path = synthTemp.city.systems{1}.GetShortestPath(1,80);
% synthTemp.RenderSystemPath(1,path);
% distance = synthTemp.city.systems{1}.GetPathDistance(path);
% duration = synthTemp.city.systems{1}.GetPathDuration(path);