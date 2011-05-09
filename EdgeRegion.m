%% EdgeRegion Class Definition
% An EdgeRegion specifies a spatial area over which edges should be
% generated. There are several types of regions, inlcuding:
% POLYLINE_PERIMETER (link edges between adjacent vertices),
% ORTHOGONAL_NEIGHBORS (link edges between all orthogonal nodes inside
% region, assuming square grid spacing), ALL_NEIGHBORS (link edges between
% all nodes, orthogonal and diagonal, inside region, assuming square grid
% spacing), FULLY_CONNECTED (link edges between all nodes contained within
% region.
% 
% The EdgeRegion class was created to be able to specify edges without
% relying on cellular definitions.
%
% 7-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef EdgeRegion < AbstractRegion
    properties(Constant)
        POLYLINE_PERIMETER = 1;     % connects adjacent nodes in perimeter (polyline)
        ORTHOGONAL_NEIGHBORS = 2;   % connects orthogonal neighbors in region
        ALL_NEIGHBORS = 3;          % connects orthogonal and diagonal neighbors in region
        FULLY_CONNECTED = 4;        % connects all nodes in region
    end
    properties
        id;                 % unique identifier for edge region
        edgeTypeId;         % edge type id
        layerIds;           % list of layer ids
        type;               % type of connectivity desired in region
        directed;           % flag (0 or 1) if edge is directed
    end
    methods
        %% EdgeRegion Constructor
        % Instantiates a new EdgeRegion with specified parameters.
        %
        % obj = EdgeRegion(id, edgeTypeId, layerIds, verticesX, verticesY,
        %           type, directed)
        %   id:         unique identifier for edge region
        %   edgeTypeId: edge type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   type:       constant parameter to define type of generation
        %   directed:   0 if undirected, 1 if directed edges
        %
        % obj = EdgeRegion(edgeTypeId, layerIds, verticesX, verticesY, 
        %           type, directed)
        %   edgeTypeId: edge type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   type:       constant parameter to define type of generation
        %   directed:   0 if undirected, 1 if directed edges
        %
        % obj = EdgeRegion()
        function obj = EdgeRegion(varargin)
            if nargin == 7
                obj.id = varargin{1};
                obj.edgeTypeId = varargin{2};
                obj.layerIds = varargin{3};
                obj.verticesX = varargin{4};
                obj.verticesY = varargin{5};
                obj.type = varargin{6};
                obj.directed = varargin{7};
            elseif nargin == 6
                obj.id = SynthesisTemplate.instance().GetNextNodeRegionId();
                obj.edgeTypeId = varargin{1};
                obj.layerIds = varargin{2};
                obj.verticesX = varargin{3};
                obj.verticesY = varargin{4};
                obj.type = varargin{5};
                obj.directed = varargin{6};
            else
                obj.id = SynthesisTemplate.instance().GetNextNodeRegionId();
                obj.edgeTypeId = 0;
                obj.layerIds = 0;
                obj.verticesX = 0;
                obj.verticesY = 0;
                obj.type = EdgeRegion.POLYLINE_PERIMETER;
                obj.directed = 0;
            end
        end
        
        %% GenerateEdges Function
        % Generates the edges within the edge region and automatically adds
        % to system definition.
        %
        % GenerateEdges(system)
        %   system: the system within which to generate edges
        function GenerateEdges(obj,system)
            synthTemp = SynthesisTemplate.instance();
            if obj.type==EdgeRegion.POLYLINE_PERIMETER
                % find corresponding node id for each vertex
                nodeIds = zeros(length(obj.layerIds),1);
                for i=1:length(obj.layerIds)
                    for n=1:length(system.nodes)
                        [cVx cVy] = system.nodes(n).cell.GetVertices();
                        if system.nodes(n).layer.id==obj.layerIds(i) && ...
                                inpolygon(obj.verticesX(i),obj.verticesY(i),cVx,cVy)
                            nodeIds(i) = system.nodes(n).id;
                            % break out of for loop in case the point is on the
                            % boundary between two adjacent nodes
                            break;
                        end
                    end
                end
                for i=1:length(nodeIds)-1
                    origin = system.nodes([system.nodes.id]==nodeIds(i));
                    destination = system.nodes([system.nodes.id]==nodeIds(i+1));
                    if ~isempty(origin) && ~isempty(destination) && ...
                            origin.id~=destination.id
                        system.edges(end+1) = Edge(...
                            system.nodes([system.nodes.id]==nodeIds(i)), ...
                            system.nodes([system.nodes.id]==nodeIds(i+1)), ...
                            synthTemp.edgeTypes([synthTemp.edgeTypes.id]==obj.edgeTypeId), ...
                            obj.directed);
                    end
                end
            elseif obj.type==EdgeRegion.ORTHOGONAL_NEIGHBORS
                nodeIds = [];
                % find corresponding cell id for each vertex in region
                for n=1:length(system.nodes)
                    node = system.nodes(n);
                    if node.layer.id==obj.layerIds(1) && ...
                            obj.ContainsCell(node.cell)
                        nodeIds(end+1) = node.id;
                    end
                end
                for i=1:length(nodeIds)
                    for j=i+1:length(nodeIds)
                        origin = system.nodes([system.nodes.id]==nodeIds(i));
                        destination = system.nodes([system.nodes.id]==nodeIds(j));
                        [vxo vyo] = origin.cell.GetVertices();
                        [vxd vyd] = destination.cell.GetVertices();
                        % determine if cells share a border (two vertices
                        % in common)
                        if ~isempty(origin) && ~isempty(destination) && ...
                                origin.id~=destination.id && ...
                                size(intersect([vxo;vyo]',[vxd;vyd]','rows'),1)==2
                            system.edges(end+1) = Edge(...
                                system.nodes([system.nodes.id]==nodeIds(i)), ...
                                system.nodes([system.nodes.id]==nodeIds(j)), ...
                                synthTemp.edgeTypes([synthTemp.edgeTypes.id]==obj.edgeTypeId), ...
                                obj.directed);
                        end
                    end
                end
            elseif obj.type==EdgeRegion.ALL_NEIGHBORS
                nodeIds = [];
                % find corresponding cell id for each vertex in region
                for n=1:length(system.nodes)
                    node = system.nodes(n);
                    if node.layer.id==obj.layerIds(1) && ...
                            obj.ContainsCell(node.cell)
                        nodeIds(end+1) = node.id;
                    end
                end
                for i=1:length(nodeIds)
                    for j=i+1:length(nodeIds)
                        origin = system.nodes([system.nodes.id]==nodeIds(i));
                        destination = system.nodes([system.nodes.id]==nodeIds(j));
                        [vxo vyo] = origin.cell.GetVertices();
                        [vxd vyd] = destination.cell.GetVertices();
                        % determine if cells share a border (one vertex or
                        % more in common)
                        if ~isempty(origin) && ~isempty(destination) && ...
                                 origin.id~=destination.id && ...
                                 size(intersect([vxo;vyo]',[vxd;vyd]','rows'),1)>=1
                            system.edges(end+1) = Edge(...
                                system.nodes([system.nodes.id]==nodeIds(i)), ...
                                system.nodes([system.nodes.id]==nodeIds(j)), ...
                                synthTemp.edgeTypes([synthTemp.edgeTypes.id]==obj.edgeTypeId), ...
                                obj.directed);
                        end
                    end
                end
            elseif obj.type==EdgeRegion.FULLY_CONNECTED
                nodeIds = [];
                % find corresponding cell id for each vertex in region
                for n=1:length(system.nodes)
                    node = system.nodes(n);
                    if node.layer.id==obj.layerIds(1) && ...
                            obj.ContainsCell(node.cell)
                        nodeIds(end+1) = node.id;
                    end
                end
                for i=1:length(nodeIds)
                    for j=i+1:length(nodeIds)
                        origin = system.nodes([system.nodes.id]==nodeIds(i));
                        destination = system.nodes([system.nodes.id]==nodeIds(j));
                        if ~isempty(origin) && ~isempty(destination) && ...
                                origin.id~=destination.id
                            system.edges(end+1) = Edge(...
                                system.nodes([system.nodes.id]==nodeIds(i)), ...
                                system.nodes([system.nodes.id]==nodeIds(j)), ...
                                synthTemp.edgeTypes([synthTemp.edgeTypes.id]==obj.edgeTypeId), ...
                                obj.directed);
                        end
                    end
                end
            end
        end
    end
end