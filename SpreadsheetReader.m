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
        %% ReadTemplate Function
        % ReadTemplate opens a spreadsheet file and reads in the
        % information to the synthesis template object.
        %
        % ReadTemplate(filepath)
        %   filepath:   the path to the spreadsheet template
        function ReadTemplate(filepath)
            synthTemp = SynthesisTemplate.instance();
            synthTemp.city = SpreadsheetReader.ReadCity(filepath);
            synthTemp.cellTypes = SpreadsheetReader.ReadCellTypes(filepath);
            
            synthTemp.nextCellTypeId = max(synthTemp.nextCellTypeId, ...
                max([synthTemp.cellTypes.id])+1);
            for i=1:length(synthTemp.cellTypes)
                synthTemp.nextCellTypeAttributeId = max(synthTemp.nextCellTypeAttributeId, ...
                    max([synthTemp.cellTypes(i).attributes.id])+1);
            end
            synthTemp.nextCellId = max(synthTemp.nextCellId, ...
                max([synthTemp.city.cells.id])+1);
        end
    end
    methods(Static,Access=private)
        %% ReadCity Function
        % ReadCity opens a spreadsheet file and reads in the city object
        % definition, including dependent objects, e.g. cells.
        %
        % city = ReadCity(filepath)
        %   filepath:   the path to the spreadsheet template
        function city = ReadCity(filepath)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.cityWorksheet);
            city = City(raw{SpreadsheetReader.cityName,2});
            city.cells = SpreadsheetReader.ReadCells(filepath);
        end
        
        %% ReadCellTypes Function
        % ReadCellTypes opens a spreadsheet file and reads in the cell
        % type definitions, including dependent objects, e.g. attributes.
        %
        % cellTypes = ReadCellTypes(filepath)
        %   filepath:   the path to the spreadsheet template
        function cellTypes = ReadCellTypes(filepath)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.cellTypesWorksheet);
            cellTypes = CellType.empty();
            for i=2:size(raw,1)
                cellType = CellType( ...
                    raw{i,SpreadsheetReader.cellTypesId}, ...
                    raw{i,SpreadsheetReader.cellTypesName}, ...
                    raw{i,SpreadsheetReader.cellTypesDescription}, ...
                    [hex2dec(raw{i,SpreadsheetReader.cellTypesColor}(1:2)) ...
                    hex2dec(raw{i,SpreadsheetReader.cellTypesColor}(3:4)) ...
                    hex2dec(raw{i,SpreadsheetReader.cellTypesColor}(5:6))]/255);
                cellType.attributes = SpreadsheetReader.ReadCellTypeAttributes(filepath,cellType);
                cellTypes(end+1) = cellType;
            end
        end
        
        %% ReadCellTypes Function
        % ReadCellTypeAttributes opens a spreadsheet file and reads in the 
        % cell type attributes for a particular cell type.
        %
        % cellTypeAttributes = ReadCellTypeAttributes(filepath,cellType)
        %   filepath:   the path to the spreadsheet template
        %   cellType:   the cell type for which to read attributes
        function cellTypeAttributes = ReadCellTypeAttributes(filepath,cellType)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.cellTypeAttributesWorksheet);
            cellTypeAttributes = CellTypeAttribute.empty();
            for i=2:size(raw,1)
                if cellType.id==raw{i,SpreadsheetReader.cellTypeAttributesTypeId}
                    cellTypeAttributes(end+1) = CellTypeAttribute( ...
                        raw{i,SpreadsheetReader.cellTypeAttributesId}, ...
                        raw{i,SpreadsheetReader.cellTypeAttributesName}, ...
                        raw{i,SpreadsheetReader.cellTypeAttributesDescription}, ...
                        raw{i,SpreadsheetReader.cellTypeAttributesUnits}, ...
                        raw{i,SpreadsheetReader.cellTypeAttributesBounds}, ...
                        raw{i,SpreadsheetReader.cellTypeAttributesValue});
                end
            end
        end
                
        %% ReadCells Function
        % ReadCells opens a spreadsheet file and reads in the cell
        % type attributes for a particular cell type.
        %
        % cells = ReadCells(filepath)
        %   filepath:   the path to the spreadsheet template
        function cells = ReadCells(filepath)
            [num txt raw] =  xlsread(filepath,SpreadsheetReader.cellsWorksheet);
            cells = Cell.empty();
            for i=2:size(raw,1)
                cells(end+1) = Cell(raw{i,SpreadsheetReader.cellsId},...
                    [raw{i,SpreadsheetReader.cellsLocationX} raw{i,SpreadsheetReader.cellsLocationY}], ...
                    [raw{i,SpreadsheetReader.cellsDimensionX} raw{i,SpreadsheetReader.cellsDimensionY}]);
            end
        end
    end
end