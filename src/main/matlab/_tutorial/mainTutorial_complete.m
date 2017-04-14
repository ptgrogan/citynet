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
SpreadsheetReader.ReadTemplate('synthesisTutorial_complete.xls');

cityNet.GenerateCells();
cityNet.GenerateNodes();
cityNet.GenerateEdges();

figure(1)
cityNet.RenderCity();

figure(2)
subplot(2,2,1)
cityNet.RenderLayer(1);
cityNet.RenderNodeRegion2d(1);
cityNet.RenderNodeRegion2d(2);
cityNet.RenderNodeRegion2d(3);
cityNet.RenderNodeRegion2d(4);
subplot(2,2,2)
cityNet.RenderLayer(2);
cityNet.RenderNodeRegion2d(5);
cityNet.RenderNodeRegion2d(6);
subplot(2,2,3)
cityNet.RenderLayer(3);
cityNet.RenderNodeRegion2d(7);
cityNet.RenderNodeRegion2d(8);
cityNet.RenderNodeRegion2d(9);
cityNet.RenderNodeRegion2d(10);
subplot(2,2,4)
cityNet.RenderLayer(4);
cityNet.RenderNodeRegion2d(11);
cityNet.RenderNodeRegion2d(12);
cityNet.RenderNodeRegion2d(13);
