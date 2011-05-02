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
        edgeTypesWorksheet = 'edge_types'; % name of the edge types worksheet
        edgeTypesId = 1;            % column of the edge type id input
        edgeTypesName = 2;          % column of the edge type name input
        edgeTypesDescription = 3;   % column of the edge type description input
        edgeTypesColor = 4;         % column of the edge type color input
        edgeTypeAttributesWorksheet = 'edge_type_attributes'; % name of the edge type attributes worksheet
        edgeTypeAttributesId = 1;    % column of the edge type attribute id input
        edgeTypeAttributesTypeId = 2; % column of the edge type attribute type id input
        edgeTypeAttributesName = 3;  % column of the edge type attribute name input
        edgeTypeAttributesDescription = 4; % column of the edge type attribute description input
        edgeTypeAttributesUnits = 5; % column of the edge type attribute units input
        edgeTypeAttributesBounds = 6; % column of the edge type attribute bounds input
        edgeTypeAttributesValue = 7; % column of the edge type attribute value input
        cellsWorksheet = 'cells';   % name of the cells worksheet
        cellsId = 1;                % column of the cell id input
        cellsLocationX = 2;         % column of the x-location input
        cellsLocationY = 3;         % column of the y-location input
        cellsDimensionX = 4;        % column of the x-dimension input
        cellsDimensionY = 5;        % column of the y-dimension input
        layersWorksheet = 'layers'; % name of the layers worksheet
        layersId = 1;               % column of the layers id input
        layersName = 2;             % column of the layers name input
        layersDescription = 3;      % column of the layers description input
        layersDisplayHeight = 4;    % column of the layers display height input
        systemsWorksheet = 'systems'; % name of the systems worksheet
        systemsId = 1;              % column of the systems id input
        systemsName = 2;            % column of the systems name input
        systemsDescription = 3;     % column of the systems description input
        nodesWorksheet = 'nodes';   % name of the nodes worksheet
        nodesId = 1;                % column of the nodes id input
        nodesSystemId = 2;          % column of the nodes system id input
        nodesTypeId = 3;            % column of the nodes type id input
        nodesCellId = 4;            % column of the nodes cell id input
        nodesLayerId = 5;           % column of the nodes layer id input
        edgesWorksheet = 'edges';   % name of the edges worksheet
        edgesId = 1;                % column of the edges id input
        edgesSystemId = 2;          % column of the edges system id input
        edgesTypeId = 3;            % column of the edges type id input
        edgesOriginId = 4;          % column of the edges origin id input
        edgesDestinationId = 5;     % column of the edges destination id input
        edgesDirected = 6;          % column of the edges directed input
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
            SpreadsheetReader.ReadNodeTypes(filepath,synthTemp);
            SpreadsheetReader.ReadEdgeTypes(filepath,synthTemp);
            SpreadsheetReader.ReadCity(filepath,synthTemp);
            
            synthTemp.nextNodeTypeId = max(synthTemp.nextNodeTypeId, ...
                max([synthTemp.nodeTypes.id])+1);
            for i=1:length(synthTemp.nodeTypes)
                synthTemp.nextNodeTypeAttributeId = max(synthTemp.nextNodeTypeAttributeId, ...
                    max([synthTemp.nodeTypes(i).attributes.id])+1);
            end
            synthTemp.nextEdgeTypeId = max(synthTemp.nextEdgeTypeId, ...
                max([synthTemp.edgeTypes.id])+1);
            for i=1:length(synthTemp.edgeTypes)
                synthTemp.nextEdgeTypeAttributeId = max(synthTemp.nextEdgeTypeAttributeId, ...
                    max([synthTemp.edgeTypes(i).attributes.id])+1);
            end
            synthTemp.nextCellId = max(synthTemp.nextCellId, ...
                max([synthTemp.city.cells.id])+1);
            synthTemp.nextLayerId = max(synthTemp.nextLayerId, ...
                max([synthTemp.city.layers.id])+1);
            synthTemp.nextSystemId = max(synthTemp.nextSystemId, ...
                max([synthTemp.city.systems.id])+1);
        end
    end
    methods(Static,Access=private)
        %% ReadCity Function
        % ReadCity opens a spreadsheet file and reads in the city object
        % definition, including dependent objects, e.g. cells.
        %
        % ReadCity(filepath,synthTemp)
        %   filepath:   the path to the spreadsheet template
        %   synthTemp:  the synthesis template for which to read the city
        function ReadCity(filepath,synthTemp)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.cityWorksheet);
            synthTemp.city = City(raw{SpreadsheetReader.cityName,2});
            SpreadsheetReader.ReadCells(filepath,synthTemp.city);
            SpreadsheetReader.ReadLayers(filepath,synthTemp.city);
            SpreadsheetReader.ReadSystems(filepath,synthTemp.city);
        end
        
        %% ReadNodeTypes Function
        % ReadNodeTypes opens a spreadsheet file and reads in the node
        % type definitions, including dependent objects, e.g. attributes.
        %
        % ReadNodeTypes(filepath,synthTemp)
        %   filepath:   the path to the spreadsheet template
        %   synthTemp:  the synthesis template for which to read the node
        %   types
        function ReadNodeTypes(filepath,synthTemp)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodeTypesWorksheet);
            for i=2:size(raw,1)
                % color is formated as 0x######
                nodeType = NodeType( ...
                    raw{i,SpreadsheetReader.nodeTypesId}, ...
                    raw{i,SpreadsheetReader.nodeTypesName}, ...
                    raw{i,SpreadsheetReader.nodeTypesDescription}, ...
                    [hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(3:4)) ...
                    hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(5:6)) ...
                    hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(7:8))]/255);
                SpreadsheetReader.ReadNodeTypeAttributes(filepath,nodeType);
                synthTemp.nodeTypes(end+1) = nodeType;
            end
        end
        
        %% ReadNodeTypeAttributes Function
        % ReadNodeTypeAttributes opens a spreadsheet file and reads in the 
        % node type attributes for a particular node type.
        %
        % ReadNodeTypeAttributes(filepath,nodeType)
        %   filepath:   the path to the spreadsheet template
        %   nodeType:   the node type for which to read attributes
        function ReadNodeTypeAttributes(filepath,nodeType)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodeTypeAttributesWorksheet);
            for i=2:size(raw,1)
                if nodeType.id==raw{i,SpreadsheetReader.nodeTypeAttributesTypeId}
                    nodeType.attributes(end+1) = NodeTypeAttribute( ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesId}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesName}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesDescription}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesUnits}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesBounds}, ...
                        raw{i,SpreadsheetReader.nodeTypeAttributesValue});
                end
            end
        end
        
        %% ReadEdgeTypes Function
        % ReadEdgeTypes opens a spreadsheet file and reads in the edge
        % type definitions, including dependent objects, e.g. attributes.
        %
        % ReadEdgeTypes(filepath,synthTemp)
        %   filepath:   the path to the spreadsheet template
        %   synthTemp:  the synthesis template for which to read the edge
        %   types
        function ReadEdgeTypes(filepath,synthTemp)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.edgeTypesWorksheet);
            for i=2:size(raw,1)
                % color is formated as 0x######
                edgeType = EdgeType( ...
                    raw{i,SpreadsheetReader.edgeTypesId}, ...
                    raw{i,SpreadsheetReader.edgeTypesName}, ...
                    raw{i,SpreadsheetReader.edgeTypesDescription}, ...
                    [hex2dec(raw{i,SpreadsheetReader.edgeTypesColor}(3:4)) ...
                    hex2dec(raw{i,SpreadsheetReader.edgeTypesColor}(5:6)) ...
                    hex2dec(raw{i,SpreadsheetReader.edgeTypesColor}(7:8))]/255);
                SpreadsheetReader.ReadEdgeTypeAttributes(filepath,edgeType);
                synthTemp.edgeTypes(end+1) = edgeType;
            end
        end
        
        %% ReadEdgeTypeAttributes Function
        % ReadEdgeTypeAttributes opens a spreadsheet file and reads in the 
        % edge type attributes for a particular edge type.
        %
        % ReadEdgeTypeAttributes(filepath,edgeType)
        %   filepath:   the path to the spreadsheet template
        %   edgeType:   the edge type for which to read attributes
        function ReadEdgeTypeAttributes(filepath,edgeType)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.edgeTypeAttributesWorksheet);
            for i=2:size(raw,1)
                if edgeType.id==raw{i,SpreadsheetReader.edgeTypeAttributesTypeId}
                    edgeType.attributes(end+1) = EdgeTypeAttribute( ...
                        raw{i,SpreadsheetReader.edgeTypeAttributesId}, ...
                        raw{i,SpreadsheetReader.edgeTypeAttributesName}, ...
                        raw{i,SpreadsheetReader.edgeTypeAttributesDescription}, ...
                        raw{i,SpreadsheetReader.edgeTypeAttributesUnits}, ...
                        raw{i,SpreadsheetReader.edgeTypeAttributesBounds}, ...
                        raw{i,SpreadsheetReader.edgeTypeAttributesValue});
                end
            end
        end
                
        %% ReadCells Function
        % ReadCells opens a spreadsheet file and reads in the cells.
        %
        % ReadCells(filepath,city)
        %   filepath:   the path to the spreadsheet template
        %   city:       the city in which to create the cells
        function ReadCells(filepath,city)
            [num txt raw] =  xlsread(filepath,SpreadsheetReader.cellsWorksheet);
            for i=2:size(raw,1)
                city.cells(end+1) = Cell(raw{i,SpreadsheetReader.cellsId},...
                    [raw{i,SpreadsheetReader.cellsLocationX} raw{i,SpreadsheetReader.cellsLocationY}], ...
                    [raw{i,SpreadsheetReader.cellsDimensionX} raw{i,SpreadsheetReader.cellsDimensionY}]);
            end
        end
        
        %% ReadLayers Function
        % ReadLayers opens a spreadsheet file and reads in the layers.
        %
        % ReadLayers(filepath)
        %   filepath:   the path to the spreadsheet template
        %   city:       the city in which to create the layers
        function ReadLayers(filepath,city)
            [num txt raw] =  xlsread(filepath,SpreadsheetReader.layersWorksheet);
            for i=2:size(raw,1)
                city.layers(end+1) = Layer(raw{i,SpreadsheetReader.layersId},...
                    raw{i,SpreadsheetReader.layersName}, ...
                    raw{i,SpreadsheetReader.layersDescription}, ...
                    raw{i,SpreadsheetReader.layersDisplayHeight});
            end
        end
                
        %% ReadSystems Function
        % ReadSystems opens a spreadsheet file and reads in the systems.
        %
        % ReadSystems(filepath)
        %   filepath:   the path to the spreadsheet template
        %   city:       the city in which to create the systems
        function ReadSystems(filepath,city)
            [num txt raw] =  xlsread(filepath,SpreadsheetReader.systemsWorksheet);
            for i=2:size(raw,1)
                system = System(raw{i,SpreadsheetReader.systemsId},...
                    raw{i,SpreadsheetReader.systemsName}, ...
                    raw{i,SpreadsheetReader.systemsDescription});
                SpreadsheetReader.ReadNodes(filepath,system);
                SpreadsheetReader.ReadEdges(filepath,system);
                city.systems(end+1) = system;
            end
        end
        
        %% ReadNodes Function
        % ReadNodes opens a spreadsheet file and reads in the nodes for a 
        % particular system.
        %
        % ReadNodes(filepath,system)
        %   filepath:   the path to the spreadsheet template
        %   system:   the system for which to read nodes
        function ReadNodes(filepath,system)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodesWorksheet);
            synthTemp = SynthesisTemplate.instance();
            for i=2:size(raw,1)
                if system.id==raw{i,SpreadsheetReader.nodesSystemId}
                    system.nodes(end+1) = Node(raw{i,SpreadsheetReader.nodesId}, ...
                        synthTemp.city.cells([synthTemp.city.cells.id]==raw{i,SpreadsheetReader.nodesCellId}), ...
                        synthTemp.city.layers([synthTemp.city.layers.id]==raw{i,SpreadsheetReader.nodesLayerId}), ...
                        synthTemp.nodeTypes([synthTemp.nodeTypes.id]==raw{i,SpreadsheetReader.nodesTypeId}));
                end
            end
        end
        
        %% ReadEdges Function
        % ReadEdges opens a spreadsheet file and reads in the edges for a 
        % particular system.
        %
        % ReadEdges(filepath,system)
        %   filepath:   the path to the spreadsheet template
        %   system:     the system for which to read edges
        function ReadEdges(filepath,system)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.edgesWorksheet);
            synthTemp = SynthesisTemplate.instance();
            for i=2:size(raw,1)
                if system.id==raw{i,SpreadsheetReader.edgesSystemId}
                    system.edges(end+1) = Edge(raw{i,SpreadsheetReader.edgesId}, ...
                        system.nodes([system.nodes.id]==raw{i,SpreadsheetReader.edgesOriginId}), ...
                        system.nodes([system.nodes.id]==raw{i,SpreadsheetReader.edgesDestinationId}), ...
                        synthTemp.edgeTypes([synthTemp.edgeTypes.id]==raw{i,SpreadsheetReader.edgesTypeId}), ...
                        raw{i,SpreadsheetReader.edgesDirected});
                end
            end
        end
    end
end