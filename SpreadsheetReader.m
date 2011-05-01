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
        cellTypesWorksheet = 'cell_types'; % name of the cell types worksheet
        cellTypesId = 1;            % column of the cell type id input
        cellTypesName = 2;          % column of the cell type name input
        cellTypesDescription = 3;   % column of the cell type description input
        cellTypesColor = 4;         % column of the cell type color input
        cellTypeAttributesWorksheet = 'cell_type_attributes'; % name of the cell type attributes worksheet
        cellTypeAttributesId = 1;    % column of the cell type attribute id input
        cellTypeAttributesTypeId = 2; % column of the cell type attribute type id input
        cellTypeAttributesName = 3;  % column of the cell type attribute name input
        cellTypeAttributesDescription = 4; % column of the cell type attribute description input
        cellTypeAttributesUnits = 5; % column of the cell type attribute units input
        cellTypeAttributesBounds = 6; % column of the cell type attribute bounds input
        cellTypeAttributesValue = 7; % column of the cell type attribute value input
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
            synthTemp = SynthesisTemplate.instance();
            %% read in the city inputs
            [num txt] = xlsread(filepath,SpreadsheetReader.cityWorksheet);
            city = City(txt{SpreadsheetReader.cityName,2});
            %% read in the cell type inputs
            [num txt raw] = xlsread(filepath,SpreadsheetReader.cellTypesWorksheet);
            for i=2:size(raw,1)
                cellType = CellType( ...
                    raw{i,SpreadsheetReader.cellTypesId}, ...
                    raw{i,SpreadsheetReader.cellTypesName}, ...
                    raw{i,SpreadsheetReader.cellTypesDescription}, ...
                    [hex2dec(raw{i,SpreadsheetReader.cellTypesColor}(1:2)) ...
                    hex2dec(raw{i,SpreadsheetReader.cellTypesColor}(3:4)) ...
                    hex2dec(raw{i,SpreadsheetReader.cellTypesColor}(5:6))]/255);
                [num2 txt2 raw2] = xlsread(filepath,SpreadsheetReader.cellTypeAttributesWorksheet);
                for j=2:size(raw2,1)
                    if cellType.id==raw2{j,SpreadsheetReader.cellTypeAttributesTypeId}
                        cellType.attributes(end+1) = CellTypeAttribute( ...
                            raw2{j,SpreadsheetReader.cellTypeAttributesId}, ...
                            raw2{j,SpreadsheetReader.cellTypeAttributesName}, ...
                            raw2{j,SpreadsheetReader.cellTypeAttributesDescription}, ...
                            raw2{j,SpreadsheetReader.cellTypeAttributesUnits}, ...
                            raw2{j,SpreadsheetReader.cellTypeAttributesBounds}, ...
                            raw2{j,SpreadsheetReader.cellTypeAttributesValue});
                    end
                end
                synthTemp.cellTypes(end+1) = cellType;
            end
            % update the next available cell type id in the synthesis template
            synthTemp.nextCellTypeId = max(synthTemp.nextCellTypeId, max([synthTemp.cellTypes.id])+1);
            %% read in the edge type inputs
            %% read in the cell inputs
            num = xlsread(filepath,SpreadsheetReader.cellsWorksheet);
            for i=1:size(num,1)
                city.cells(end+1) = Cell(num(i,SpreadsheetReader.cellsId),...
                    [num(i,SpreadsheetReader.cellsLocationX) num(SpreadsheetReader.cellsLocationY)], ...
                    [num(i,SpreadsheetReader.cellsDimensionX) num(i,SpreadsheetReader.cellsDimensionY)]);
            end
            % update the next available cell id in the synthesis template
            synthTemp.nextCellId = max(synthTemp.nextCellId, max([city.cells.id])+1);
        end
    end
end