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
        UNDEFINED = 0;              % type not defined
        POLYGON_ORTHOGONAL = 1;     % connects orthogonal neighbors in polygon
        POLYGON_ADJACENT = 2;       % connects orthogonal and diagonal neighbors in polygon
        POLYGON_CONNECTED = 3;      % connects all nodes within polygon
        POLYLINE = 4;               % connects adjacent nodes along polyline
        POLYPOINT = 5;              % connects nodes at polypoints
    end
    properties
        id;                 % unique identifier for edge region
        edgeTypeId;         % edge type id
        layerIds;           % list of layer ids
        type;               % type of connectivity desired in region
        directed;           % flag (0 or 1) if edge is directed
        description;        % description of region, string
    end
    methods
        %% EdgeRegion Constructor
        % Instantiates a new EdgeRegion with specified parameters.
        %
        % obj = EdgeRegion(id, edgeTypeId, layerIds, verticesX, verticesY,
        %           type, directed, description)
        %   id:         unique identifier for edge region
        %   edgeTypeId: edge type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   type:       constant parameter to define type of generation
        %   directed:   0 if undirected, 1 if directed edges
        %   description:description of the region, string
        %
        % obj = EdgeRegion(edgeTypeId, layerIds, verticesX, verticesY, 
        %           type, directed, description)
        %   edgeTypeId: edge type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   type:       constant parameter to define type of generation
        %   directed:   0 if undirected, 1 if directed edges
        %   description:description of the region, string
        %
        % obj = EdgeRegion()
        function obj = EdgeRegion(varargin)
            if nargin == 8
                obj.id = varargin{1};
                obj.edgeTypeId = varargin{2};
                obj.layerIds = varargin{3};
                obj.verticesX = varargin{4};
                obj.verticesY = varargin{5};
                obj.type = varargin{6};
                obj.directed = varargin{7};
                obj.description = varargin{8};
            elseif nargin == 7
                obj.id = CityNet.instance().GetNextEdgeRegionId();
                obj.edgeTypeId = varargin{1};
                obj.layerIds = varargin{2};
                obj.verticesX = varargin{3};
                obj.verticesY = varargin{4};
                obj.type = varargin{5};
                obj.directed = varargin{6};
                obj.description = varargin{7};
            else
                obj.id = CityNet.instance().GetNextEdgeRegionId();
                obj.edgeTypeId = 0;
                obj.layerIds = 0;
                obj.verticesX = 0;
                obj.verticesY = 0;
                obj.type = EdgeRegion.UNDEFINED;
                obj.directed = 0;
                obj.description = '';
            end
        end
        
        %% GenerateEdges Function
        % Generates the edges within the edge region and automatically adds
        % to system definition.
        %
        % GenerateEdges(system)
        %   system: the system within which to generate edges
        function GenerateEdges(obj,system)
            if obj.type==EdgeRegion.POLYLINE
                % find corresponding node ids along line
                nodeIds = [];
                for i=1:length(obj.layerIds)-1
                    for n=1:length(system.nodes)
                        % TODO: processes nodes in uncertain order
                        if system.nodes(n).layer.id == obj.layerIds(i) && ...
                                system.nodes(n).cell.IntersectsLine( ...
                                obj.verticesX(i),obj.verticesY(i), ...
                                obj.verticesX(i+1),obj.verticesY(i+1))
                            nodeIds(end+1) = system.nodes(n).id;
                        end
                    end
                end
                for i=1:length(nodeIds)-1
                    origin = system.nodes([system.nodes.id]==nodeIds(i));
                    destination = system.nodes([system.nodes.id]==nodeIds(i+1));
                    if ~isempty(origin) && ~isempty(destination) && ...
                            origin.id~=destination.id
                        obj.CreateEdge(system,origin,destination);
                    end
                end
            elseif obj.type==EdgeRegion.POLYPOINT
                % find corresponding node ids for each vertex
                nodeIds = zeros(length(obj.layerIds),1);
                for i=1:length(obj.layerIds)
                    for n=1:length(system.nodes)
                        if system.nodes(n).layer.id == obj.layerIds(i) && ...
                                system.nodes(n).cell.ContainsPoint(obj.verticesX(i),obj.verticesY(i))
                            nodeIds(i) = system.nodes(n).id;
                            % break out of for loop in case the point is on the
                            % boundary between two adjacent nodes
                            break
                        end
                    end
                end
                for i=1:length(nodeIds)-1
                    origin = system.nodes([system.nodes.id]==nodeIds(i));
                    destination = system.nodes([system.nodes.id]==nodeIds(i+1));
                    if ~isempty(origin) && ~isempty(destination) && ...
                            origin.id~=destination.id
                        obj.CreateEdge(system,origin,destination);
                    end
                end
            elseif obj.type==EdgeRegion.POLYGON_ORTHOGONAL
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
                            obj.CreateEdge(system,origin,destination);
                        end
                    end
                end
            elseif obj.type==EdgeRegion.POLYGON_ADJACENT
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
                            obj.CreateEdge(system,origin,destination);
                        end
                    end
                end
            elseif obj.type==EdgeRegion.POLYGON_CONNECTED
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
                            obj.CreateEdge(system,origin,destination);
                        end
                    end
                end
            end
        end
    end
    methods(Access=private)
        %% CreateEdge Function
        % Creates a new edge between origin and destination nodes within a 
        % system.
        function CreateEdge(obj,system,origin,destination)
            system.edges(end+1) = Edge(origin, destination, ...
                system.edgeTypes([system.edgeTypes.id]==obj.edgeTypeId), ...
                obj.directed);
        end
    end
end