%% City.Net Synthesis Template - Transportation Input File
% This file uses a spreadsheet-based input file to synthesize the
% transportation system within a sample Masdar city model.
%
% 9-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%

clear classes
close all
clc
addpath(['..' filesep '..']);

%% define synthesis properties

% the following line generates a reference to the synthesis template
% application object.
synthTemp = SynthesisTemplate.instance();

% the following line loads a template from a saved spreadsheet file.
SpreadsheetReader.ReadTemplate('synthesisWater.xls');

% node and edge regions must be explicitly told to generate nodes and
% edges. normally this would occur within the graphical user interface, but
% as a proxy in this case, it is explicitly called via the following
% functions. you should not have to change them.
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

% consider using the following functions for visualizations. note that
% these function calls do not automatically open new figures.
%
% synthTemp.RenderCells();      % displays the cells and cell ids
%
% synthTemp.RenderLayer(1);     % displays a layer (here: 1) in a 2d plot
%
% synthTemp.RenderSystem(1);    % displays all system layers in a 3d plot
%
% synthTemp.RenderCity();       % displays all systems in a 3d plot

figure(1)
synthTemp.RenderCity;