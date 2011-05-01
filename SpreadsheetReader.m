%% SpreadsheetReader Class Definition
% The SpreadsheetReader class is used in a static sense to read in a city
% definition from a spreadsheet.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef SpreadsheetReader
    properties(Constant)
        cityWorksheet = 'city';     % name of the city worksheet
        cityName = 1;               % row of the city name input
        cellsWorksheet = 'cells';   % name of the cells worksheet
        cellsId = 1;                % column of the cell id input
        cellsLocationX = 2;         % column of the x-location input
        cellsLocationY = 3;         % column of the y-location input
        cellsDimensionX = 4;        % column of the x-dimension input
        cellsDimensionY = 5;        % column of the y-dimension input
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
        %% Read Function
        % Reads an input spreadsheet into the Matlab data structures.
        %
        % obj = SpreadsheetReader(filepath)
        %   filepath:   the path to the spreadsheet to read
        
        function city = Read(filepath)
            %% read in the city inputs
            [num txt] = xlsread(filepath,SpreadsheetReader.cityWorksheet);
            city = City(txt{SpreadsheetReader.cityName,2});
            %% read in the cell type inputs
            %% read in the edge type inputs
            %% read in the cell inputs
            num = xlsread(filepath,SpreadsheetReader.cellsWorksheet);
            for i=1:length(num)
                city.cells(end+1) = Cell(num(i,SpreadsheetReader.cellsId),...
                    [num(i,SpreadsheetReader.cellsLocationX) num(SpreadsheetReader.cellsLocationY)], ...
                    [num(i,SpreadsheetReader.cellsDimensionX) num(i,SpreadsheetReader.cellsDimensionY)]);
            end
            % update the next available cell id in the synthesis template
            synthTemp = SynthesisTemplate.instance();
            synthTemp.nextCellId = max(synthTemp.nextCellId, max([city.cells.id])+1);
        end
    end
end