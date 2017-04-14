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
        cityLatitude = 2;           % row of the city latitude input
        cityLongitude = 3;          % row of the city longitude input
        cityRotation = 4;           % row of the city rotation input
        cityImagePath = 5;          % row of the city image file path
        cityImageVerticesX = 6;     % row of the city image x-vertices
        cityImageVerticesY = 7;     % row of the city image y-vertices
        minIntersectionFraction = 8; % row of the minimum intersection area input
        systemsWorksheet = 'systems'; % name of the systems worksheet
        systemsId = 1;              % column of the systems id input
        systemsName = 2;            % column of the systems name input
        systemsDescription = 3;     % column of the systems description input
        nodeTypesWorksheet = 'node_types'; % name of the node types worksheet
        nodeTypesId = 1;            % column of the node type id input
        nodeTypesSystemId = 2;      % column of the node type system id input
        nodeTypesName = 3;          % column of the node type name input
        nodeTypesDescription = 4;   % column of the node type description input
        nodeTypesColor = 5;         % column of the node type color input
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
        edgeTypesSystemId = 2;      % column of the edge type system id input
        edgeTypesName = 3;          % column of the edge type name input
        edgeTypesDescription = 4;   % column of the edge type description input
        edgeTypesColor = 5;         % column of the edge type color input
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
        layersSystemId = 2;         % column of the layers system id input
        layersName = 3;             % column of the layers name input
        layersDescription = 4;      % column of the layers description input
        layersDisplayHeight = 5;    % column of the layers display height input
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
        nodeRegionsWorksheet = 'node_regions';  % name of the node regions worksheet
        nodeRegionsId = 1;          % column of the node regions id input
        nodeRegionsSystemId = 2;    % column of the node regions system id input
        nodeRegionsNodeTypeId = 3;  % column of the node regions node type id input
        nodeRegionsLayerId = 4;     % column of the node regions layer id input
        nodeRegionsVerticesX = 5;   % column of the node regions x-vertices input
        nodeRegionsVerticesY = 6;   % column of the node regions y-vertices input
        nodeRegionsType = 7;        % column of the node regions description input
        nodeRegionsDescription = 8; % column of the node regions description input
        edgeRegionsWorksheet = 'edge_regions';  % name of the edge regions worksheet
        edgeRegionsId = 1;          % column of the edge regions id input
        edgeRegionsSystemId = 2;    % column of the edge regions system id input
        edgeRegionsNodeTypeId = 3;  % column of the edge regions node type id input
        edgeRegionsLayerIds = 4;    % column of the edge regions layer ids input
        edgeRegionsVerticesX = 5;   % column of the edge regions x-vertices input
        edgeRegionsVerticesY = 6;   % column of the edge regions y-vertices input
        edgeRegionsType = 7;        % column of the edge regions connection type input
        edgeRegionsDirected = 8;    % column of the edge regions directed input
        edgeRegionsDescription = 9; % column of the edge regions description input
        cellRegionsWorksheet = 'cell_regions';  % name of the cell regions worksheet
        cellRegionsId = 1;          % column of the cell regions id input
        cellRegionsVerticesX = 2;   % column of the cell regions x-vertices input
        cellRegionsVerticesY = 3;   % column of the cell regions y-vertices input
        cellRegionsNumRows = 4;     % column of the cell regions number rows input
        cellRegionsNumCols = 5;     % column of the cell regions number columns input
        cellRegionsDescription = 6; % column of the cell regions description input
        h = waitbar(0);             % handle of waitbar
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
            waitbar(0,SpreadsheetReader.h,'Initializing City.Net');
            cityNet = CityNet.instance();
            
            % suppress xlsread warnings
            warning('off','MATLAB:xlsread:Mode');
                                    
            SpreadsheetReader.ReadCity(filepath,cityNet);
            
            waitbar(1,SpreadsheetReader.h,'Updating City.Net');
            if ~isempty(cityNet.city.cells)
                cityNet.nextCellId = max(cityNet.nextCellId, ...
                    max([cityNet.city.cells.id])+1);
            end
            if ~isempty(cityNet.city.cellRegions)
                cityNet.nextCellRegionId = max(cityNet.nextCellRegionId, ...
                    max([cityNet.city.cellRegions.id])+1);
            end
            cityNet.nextSystemId = length(cityNet.city.systems)+1;
            for i=1:length(cityNet.city.systems)
                system = cityNet.city.systems(i);
                if ~isempty(system.nodeTypes)
                    cityNet.nextNodeTypeId = max(cityNet.nextNodeTypeId, ...
                        max([system.nodeTypes.id])+1);
                end
                for j=1:length(system.nodeTypes)
                    if ~isempty(system.nodeTypes(j).attributes)
                        cityNet.nextNodeTypeAttributeId = max(cityNet.nextNodeTypeAttributeId, ...
                            max([system.nodeTypes(j).attributes.id])+1);
                    end
                end
                if ~isempty(system.edgeTypes)
                    cityNet.nextEdgeTypeId = max(cityNet.nextEdgeTypeId, ...
                        max([system.edgeTypes.id])+1);
                end
                for j=1:length(system.edgeTypes)
                    if ~isempty(system.edgeTypes(j).attributes)
                        cityNet.nextEdgeTypeAttributeId = max(cityNet.nextEdgeTypeAttributeId, ...
                            max([system.edgeTypes(j).attributes.id])+1);
                    end
                end
                if ~isempty(system.layers)
                    cityNet.nextLayerId = max(cityNet.nextLayerId, ...
                        max([system.layers.id])+1);
                end
                if ~isempty(system.nodes)
                    cityNet.nextNodeId = max(cityNet.nextNodeId, ...
                        max([system.nodes.id])+1);
                end
                if ~isempty(system.edges)
                    cityNet.nextEdgeId = max(cityNet.nextEdgeId, ...
                        max([system.edges.id])+1);
                end
                if ~isempty(system.nodeRegions)
                    cityNet.nextNodeRegionId = max(cityNet.nextNodeRegionId, ...
                        max([system.nodeRegions.id])+1);
                end
                if ~isempty(system.edgeRegions)
                    cityNet.nextEdgeRegionId = max(cityNet.nextEdgeRegionId, ...
                        max([system.edgeRegions.id])+1);
                end
            end
            close(SpreadsheetReader.h);
        end
    end
    methods(Static,Access=private)        
        %% ReadCity Function
        % ReadCity opens a spreadsheet file and reads in the city object
        % definition, including dependent objects, e.g. cells.
        %
        % ReadCity(filepath,cityNet)
        %   filepath:   the path to the spreadsheet template
        %   cityNet:  the synthesis template for which to read the city
        function ReadCity(filepath,cityNet)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.cityWorksheet,'','basic');
            cityNet.city = City(raw{SpreadsheetReader.cityName,2});
            cityNet.city.latitude = raw{SpreadsheetReader.cityLatitude,2};
            cityNet.city.longitude = raw{SpreadsheetReader.cityLongitude,2};
            cityNet.city.rotation = raw{SpreadsheetReader.cityRotation,2};
            cityNet.city.imagePath = raw{SpreadsheetReader.cityImagePath,2};
            cityNet.city.imageVerticesX = eval(raw{SpreadsheetReader.cityImageVerticesX,2});
            cityNet.city.imageVerticesY = eval(raw{SpreadsheetReader.cityImageVerticesY,2});
            cityNet.minIntersectionFraction = 0.5;
            SpreadsheetReader.ReadCells(filepath,cityNet.city);
            SpreadsheetReader.ReadCellRegions(filepath,cityNet.city);
            SpreadsheetReader.ReadSystems(filepath,cityNet.city);
        end
                
        %% ReadCells Function
        % ReadCells opens a spreadsheet file and reads in the cells.
        %
        % ReadCells(filepath,city)
        %   filepath:   the path to the spreadsheet template
        %   city:       the city in which to create the cells
        function ReadCells(filepath,city)
            [num txt raw] =  xlsread(filepath,SpreadsheetReader.cellsWorksheet,'','basic');
            for i=2:size(raw,1)
                waitbar(.0+.1*i/size(raw,1),SpreadsheetReader.h,['Reading Cell ' num2str(raw{i,SpreadsheetReader.cellsId})]);
                city.cells(end+1) = Cell(raw{i,SpreadsheetReader.cellsId},...
                    [raw{i,SpreadsheetReader.cellsLocationX} raw{i,SpreadsheetReader.cellsLocationY}], ...
                    [raw{i,SpreadsheetReader.cellsDimensionX} raw{i,SpreadsheetReader.cellsDimensionY}]);
            end
        end
        
        %% ReadCellRegions Function
        % ReadCellRegions opens a spreadsheet file and reads in the cell
        % regions.
        %
        % ReadCellRegions(filepath,city)
        %   filepath:   the path to the spreadsheet template
        %   city:     	city handle
        function ReadCellRegions(filepath,city)
            try
                [num txt raw] = xlsread(filepath,SpreadsheetReader.cellRegionsWorksheet,'','basic');
                for i=2:size(raw,1)
                    waitbar(.1+.2*i/size(raw,1),SpreadsheetReader.h,['Reading Cell Region ' num2str(raw{i,SpreadsheetReader.cellRegionsId})]);
                    city.cellRegions(end+1) = CellRegion( ...
                        raw{i,SpreadsheetReader.cellRegionsId}, ...
                        eval(raw{i,SpreadsheetReader.cellRegionsVerticesX}), ...
                        eval(raw{i,SpreadsheetReader.cellRegionsVerticesY}), ...
                        [raw{i,SpreadsheetReader.cellRegionsNumRows}, ...
                        raw{i,SpreadsheetReader.cellRegionsNumCols}], ...
                        raw{i,SpreadsheetReader.cellRegionsDescription});
                end
            catch ex
                % if worksheet does not exist, print temporary message
                disp(['CityNet Warning: Could not read "' ...
                    SpreadsheetReader.cellRegionsWorksheet ...
                    '" worksheet - please add at your convenience.'])
            end
        end
        
        %% ReadSystems Function
        % ReadSystems opens a spreadsheet file and reads in the systems.
        %
        % ReadSystems(filepath)
        %   filepath:   the path to the spreadsheet template
        %   city:       the city in which to create the systems
        function ReadSystems(filepath,city)
            [num txt raw] =  xlsread(filepath,SpreadsheetReader.systemsWorksheet,'','basic');
            for i=2:size(raw,1)
                waitbar(0.2+0.8*i/size(raw,1),SpreadsheetReader.h,['Reading ' raw{i,SpreadsheetReader.systemsName} ' System']);
                city.systems(end+1) = System(raw{i,SpreadsheetReader.systemsId},...
                        raw{i,SpreadsheetReader.systemsName}, ...
                        raw{i,SpreadsheetReader.systemsDescription});
                SpreadsheetReader.ReadNodeTypes(filepath,city.systems(end));
                SpreadsheetReader.ReadEdgeTypes(filepath,city.systems(end));
                SpreadsheetReader.ReadLayers(filepath,city.systems(end));
                SpreadsheetReader.ReadNodes(filepath,city.systems(end));
                SpreadsheetReader.ReadEdges(filepath,city.systems(end));
                SpreadsheetReader.ReadNodeRegions(filepath,city.systems(end));
                SpreadsheetReader.ReadEdgeRegions(filepath,city.systems(end));
            end
        end
        
        %% ReadNodeTypes Function
        % ReadNodeTypes opens a spreadsheet file and reads in the node
        % type definitions, including dependent objects, e.g. attributes.
        %
        % ReadNodeTypes(filepath,system)
        %   filepath:   the path to the spreadsheet template
        %   system:     the system handle for which to read the node types
        function ReadNodeTypes(filepath,system)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodeTypesWorksheet,'','basic');
            for i=2:size(raw,1)
                if system.id==raw{i,SpreadsheetReader.nodeTypesSystemId}
                    % color is formated as 0x######
                    system.nodeTypes(end+1) = NodeType( ...
                        raw{i,SpreadsheetReader.nodeTypesId}, ...
                        raw{i,SpreadsheetReader.nodeTypesName}, ...
                        raw{i,SpreadsheetReader.nodeTypesDescription}, ...
                        [hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(3:4)) ...
                        hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(5:6)) ...
                        hex2dec(raw{i,SpreadsheetReader.nodeTypesColor}(7:8))]/255);
                    SpreadsheetReader.ReadNodeTypeAttributes(filepath,system.nodeTypes(end));
                end
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
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodeTypeAttributesWorksheet,'','basic');
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
        % ReadEdgeTypes(filepath,system)
        %   filepath:   the path to the spreadsheet template
        %   system:     the system handle for which to read the edge types
        function ReadEdgeTypes(filepath,system)
            [num txt raw] = xlsread(filepath,SpreadsheetReader.edgeTypesWorksheet,'','basic');
            for i=2:size(raw,1)
                if system.id==raw{i,SpreadsheetReader.edgeTypesSystemId}
                    % color is formated as 0x######
                    system.edgeTypes(end+1) = EdgeType( ...
                        raw{i,SpreadsheetReader.edgeTypesId}, ...
                        raw{i,SpreadsheetReader.edgeTypesName}, ...
                        raw{i,SpreadsheetReader.edgeTypesDescription}, ...
                        [hex2dec(raw{i,SpreadsheetReader.edgeTypesColor}(3:4)) ...
                        hex2dec(raw{i,SpreadsheetReader.edgeTypesColor}(5:6)) ...
                        hex2dec(raw{i,SpreadsheetReader.edgeTypesColor}(7:8))]/255);
                    SpreadsheetReader.ReadEdgeTypeAttributes(filepath,system.edgeTypes(end));
                end
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
            [num txt raw] = xlsread(filepath,SpreadsheetReader.edgeTypeAttributesWorksheet,'','basic');
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
        
        %% ReadLayers Function
        % ReadLayers opens a spreadsheet file and reads in the layers.
        %
        % ReadLayers(filepath)
        %   filepath:   the path to the spreadsheet template
        %   system:     the system handle for which to create the layers
        function ReadLayers(filepath,system)
            [num txt raw] =  xlsread(filepath,SpreadsheetReader.layersWorksheet,'','basic');
            for i=2:size(raw,1)
                if system.id==raw{i,SpreadsheetReader.layersSystemId}
                    system.layers(end+1) = Layer(raw{i,SpreadsheetReader.layersId},...
                        raw{i,SpreadsheetReader.layersName}, ...
                        raw{i,SpreadsheetReader.layersDescription}, ...
                        raw{i,SpreadsheetReader.layersDisplayHeight});
                end
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
            [num txt raw] = xlsread(filepath,SpreadsheetReader.nodesWorksheet,'','basic');
            cityNet = CityNet.instance();
            for i=2:size(raw,1)
                if system.id==raw{i,SpreadsheetReader.nodesSystemId}
                    system.nodes(end+1) = Node(raw{i,SpreadsheetReader.nodesId}, ...
                        cityNet.city.cells([cityNet.city.cells.id]==raw{i,SpreadsheetReader.nodesCellId}), ...
                        system.layers([system.layers.id]==raw{i,SpreadsheetReader.nodesLayerId}), ...
                        system.nodeTypes([system.nodeTypes.id]==raw{i,SpreadsheetReader.nodesTypeId}));
                end
            end
        end
        
        %% ReadNodeRegions Function
        % ReadNodeRegions opens a spreadsheet file and reads in the node
        % regions.
        %
        % ReadNodeRegions(filepath,system)
        %   filepath:   the path to the spreadsheet template
        %   system:     system handle
        function ReadNodeRegions(filepath,system)
            try
                [num txt raw] = xlsread(filepath,SpreadsheetReader.nodeRegionsWorksheet,'','basic');
                for i=2:size(raw,1)
                    if system.id==raw{i,SpreadsheetReader.nodeRegionsSystemId}
                        regionType = NodeRegion.UNDEFINED;
                        if strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'polygon')
                            regionType = NodeRegion.POLYGON;
                        elseif strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'polyline')
                            regionType = NodeRegion.POLYLINE;
                        elseif strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'polypoint')
                            regionType = NodeRegion.POLYPOINT;
                        end
                        system.nodeRegions(end+1) = NodeRegion( ...
                            raw{i,SpreadsheetReader.nodeRegionsId}, ...
                            raw{i,SpreadsheetReader.nodeRegionsNodeTypeId}, ...
                            raw{i,SpreadsheetReader.nodeRegionsLayerId}, ...
                            eval(raw{i,SpreadsheetReader.nodeRegionsVerticesX}), ...
                            eval(raw{i,SpreadsheetReader.nodeRegionsVerticesY}), ...
                            regionType, ...
                            raw{i,SpreadsheetReader.nodeRegionsDescription});
                    end
                end
            catch ex
                % if worksheet does not exist, print temporary message
                disp(['City.Net Warning: Could not read "' ...
                    SpreadsheetReader.nodeRegionsWorksheet ...
                    '" worksheet - please add at your convenience.'])
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
            [num txt raw] = xlsread(filepath,SpreadsheetReader.edgesWorksheet,'','basic');
            cityNet = CityNet.instance();
            for i=2:size(raw,1)
                if system.id==raw{i,SpreadsheetReader.edgesSystemId}
                    system.edges(end+1) = Edge(raw{i,SpreadsheetReader.edgesId}, ...
                        system.nodes([system.nodes.id]==raw{i,SpreadsheetReader.edgesOriginId}), ...
                        system.nodes([system.nodes.id]==raw{i,SpreadsheetReader.edgesDestinationId}), ...
                        system.edgeTypes([system.edgeTypes.id]==raw{i,SpreadsheetReader.edgesTypeId}), ...
                        raw{i,SpreadsheetReader.edgesDirected});
                end
            end
        end
        
        %% ReadEdgeRegions Function
        % ReadEdgeRegions opens a spreadsheet file and reads in the edge
        % regions.
        %
        % ReadEdgeRegions(filepath,system)
        %   filepath:   the path to the spreadsheet template
        %   system:     system handle
        function ReadEdgeRegions(filepath,system)
            try
                [num txt raw] = xlsread(filepath,SpreadsheetReader.edgeRegionsWorksheet,'','basic');
                for i=2:size(raw,1)
                    if system.id==raw{i,SpreadsheetReader.edgeRegionsSystemId}
                        regionType = EdgeRegion.UNDEFINED;
                        if strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'orthogonal')
                            regionType = EdgeRegion.POLYGON_ORTHOGONAL;
                        elseif strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'neighbors') || ...
                                strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'adjacent')
                            regionType = EdgeRegion.POLYGON_ADJACENT;
                        elseif strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'connected')
                            regionType = EdgeRegion.POLYGON_CONNECTED;
                        elseif strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'polyline')
                            regionType = EdgeRegion.POLYLINE;
                        elseif strcmp(raw{i,SpreadsheetReader.edgeRegionsType},'polypoint')
                            regionType = EdgeRegion.POLYPOINT;
                        end
                        system.edgeRegions(end+1) = EdgeRegion( ...
                            raw{i,SpreadsheetReader.edgeRegionsId}, ...
                            raw{i,SpreadsheetReader.edgeRegionsNodeTypeId}, ...
                            eval(raw{i,SpreadsheetReader.edgeRegionsLayerIds}), ...
                            eval(raw{i,SpreadsheetReader.edgeRegionsVerticesX}), ...
                            eval(raw{i,SpreadsheetReader.edgeRegionsVerticesY}), ...
                            regionType, ...
                            raw{i,SpreadsheetReader.edgeRegionsDirected}, ...
                            raw{i,SpreadsheetReader.edgeRegionsDescription});
                    end
                end
            catch ex
                % if worksheet does not exist, print temporary message
                disp(['City.Net Warning: Could not read "' ...
                    SpreadsheetReader.edgeRegionsWorksheet ...
                    '" worksheet - please add at your convenience.'])
            end
        end
    end
end