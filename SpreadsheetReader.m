%% SpreadsheetReader Class Definition
% The SpreadsheetReader class is used in a static sense to read in a city
% definition from a spreadsheet.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef SpreadsheetReader
    properties(Constant)
        cityWorksheet = 'city';
        cityName = 1;
        cellsWorksheet = 'cells';
        cellsId = 1;
        cellsLocationX = 2;
        cellsLocationY = 3;
        cellsDimensionX = 4;
        cellsDimensionY = 5;
    end
    methods(Access=private)
        %% SpreadsheetReader Constructor
        % SpreadsheetReader uses a private constructor to prevent it from
        % being instantiated -- use its methods in a static sense, e.g.
        % SpreadsheetReader.Read('test.xls').
        %
        % obj = SpreadsheetReader()
        
        function obj = SpreadsheetReader()
        end
    end
    methods(Static)
        function out = Read(filepath)
            [num txt] = xlsread(filepath,SpreadsheetReader.cityWorksheet);
            out = City(txt{SpreadsheetReader.cityName,2});
            num = xlsread(filepath,SpreadsheetReader.cellsWorksheet);
            for i=1:length(num)
                out.cells(end+1) = Cell(num(i,SpreadsheetReader.cellsId),...
                    [num(i,SpreadsheetReader.cellsLocationX) num(SpreadsheetReader.cellsLocationY)], ...
                    [num(i,SpreadsheetReader.cellsDimensionX) num(i,SpreadsheetReader.cellsDimensionY)]);
            end
        end
    end
end