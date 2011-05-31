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
synthTemp = SynthesisTemplate.instance();
SpreadsheetReader.ReadTemplate('synthesisTutorial.xls');

synthTemp.GenerateCells();
synthTemp.GenerateNodes();
synthTemp.GenerateEdges();

figure(1)
synthTemp.RenderCity();

figure(2)
subplot(2,2,1)
synthTemp.RenderLayer(1);
synthTemp.RenderNodeRegion2d(1);
synthTemp.RenderNodeRegion2d(2);
synthTemp.RenderNodeRegion2d(3);
synthTemp.RenderNodeRegion2d(4);
subplot(2,2,2)
synthTemp.RenderLayer(2);
synthTemp.RenderNodeRegion2d(5);
synthTemp.RenderNodeRegion2d(6);
subplot(2,2,3)
synthTemp.RenderLayer(3);
synthTemp.RenderNodeRegion2d(7);
synthTemp.RenderNodeRegion2d(8);
synthTemp.RenderNodeRegion2d(9);
synthTemp.RenderNodeRegion2d(10);
subplot(2,2,4)
synthTemp.RenderLayer(4);
synthTemp.RenderNodeRegion2d(11);
synthTemp.RenderNodeRegion2d(12);
synthTemp.RenderNodeRegion2d(13);