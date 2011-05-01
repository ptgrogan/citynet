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
        nodeTypesWorksheet = 'node_types'; % name of the node types worksheet
        nodeTypesId = 1;            % column of the node type id input
        nodeTypesName = 2;          % column of the node type name input
        nodeTypesDescription = 3;   % column of the node type description input
        nodeTypesColor = 4;         % column of the node type color input
        nodeTypeAttributesWorksheet = 'node_type_attributes'; % name of the node type attributes worksheet
        nodeTypeAttributesId = 1;    % column of the node type attribute id input
        nodeTypeAttributesTypeId = 2; % column of the node type attribute type id input
        nodeTypeAttributesName = 3;  % column of the node type attribute name input
        nodeTypeAttributesDescription = 4; % column of the node type attribute description input
        nodeTypeAttributesUnits = 5; % column of the node type attribute units input
        nodeTypeAttributesBounds = 6; % column of the node type attribute bounds input
        nodeTypeAttributesValue = 7; % column of the node type attribute value input
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
            synthTemp.nodeTypes = SpreadsheetReader.ReadNodeTypes(filepath);
            
            synthTemp.nextNodeTypeId = max(synthTemp.nextNodeTypeId, ...
                max([synthTemp.nodeTypes.id])+1);
            for i=1:length(synthTemp.nodeTypes)
                synthTemp.nextNodeTypeAttributeId = max(synthTemp.nextNodeTypeAttributeId, ...
                    max([synthTemp.nodeTypes(i).attributes.id])+1);
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
        
        %% ReadNodeTypes Function
        % ReadNodeTypes opens a spreadsheet file and reads in the cell
        % type definitions, including dependent objects, e.g. attributes.
        %
        % nodeTypes = ReadNodeTypes(filepath)
        %   filepath:   the path to the spreadsheet template
        function nodeTypes = ReadNodeTypes(filepath)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodeTypesWorksheet);
            nodeTypes = NodeType.empty();
            for i=2:size(raw,1)
                nodeType = NodeType( ...
                    raw{i,SpreadsheetReader.nodeTypesId}, ...
                    raw{i,SpreadsheetReader.nodeTypesName}, ...
                    raw{i,SpreadsheetReader.nodeTypesDescription}, ...
                    [hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(1:2)) ...
                    hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(3:4)) ...
                    hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(5:6))]/255);
                nodeType.attributes = SpreadsheetReader.ReadNodeTypeAttributes(filepath,nodeType);
                nodeTypes(end+1) = nodeType;
            end
        end
        
        %% ReadNodeTypeAttributes Function
        % ReadNodeTypeAttributes opens a spreadsheet file and reads in the 
        % node type attributes for a particular node type.
        %
        % nodeTypeAttributes = ReadNodeTypeAttributes(filepath,nodeType)
        %   filepath:   the path to the spreadsheet template
        %   nodeType:   the node type for which to read attributes
        function nodeTypeAttributes = ReadNodeTypeAttributes(filepath,nodeType)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodeTypeAttributesWorksheet);
            nodeTypeAttributes = NodeTypeAttribute.empty();
            for i=2:size(raw,1)
                if nodeType.id==raw{i,SpreadsheetReader.nodeTypeAttributesTypeId}
                    nodeTypeAttributes(end+1) = NodeTypeAttribute( ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesId}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesName}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesDescription}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesUnits}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesBounds}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesValue});
                end
            end
        end
                
        %% ReadCells Function
        % ReadCells opens a spreadsheet file and reads in the cell
        % type attributes for a particular node type.
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