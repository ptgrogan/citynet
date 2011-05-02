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

%% render system
synthTemp.RenderCity;
synthTemp.RenderLayer(2);
synthTemp.RenderSystem(1);