classdef EdgeRegion < AbstractRegion
    properties(Constant)
        POLYLINE_PERIMETER = 1;     % connects adjacent nodes in perimeter (polyline)
        ORTHOGONAL_NEIGHBORS = 2;   % connects orthogonal neighbors in region
        ALL_NEIGHBORS = 3;          % connects orthogonal and diagonal neighbors in region
        FULLY_CONNECTED = 4;        % connects all nodes in region
    end
    properties
        id;                 % unique identifier for edge region
        systemId;           % system id for edge assignment
        edgeTypeId;         % edge type id
        layerIds;           % list of layer ids
        type;               % type of connectivity desired in region
        directed;           % flag (0 or 1) if edge is directed
    end
    methods
        function obj = EdgeRegion(varargin)
            if nargin == 8
                obj.id = varargin{1};
                obj.systemId = varargin{2};
                obj.edgeTypeId = varargin{3};
                obj.layerIds = varargin{4};
                obj.verticesX = varargin{5};
                obj.verticesY = varargin{6};
                obj.type = varargin{7};
                obj.directed = varargin{8};
            elseif nargin == 7
                obj.id = SynthesisTemplate.GetNextNodeRegionId();
                obj.systemId = varargin{1};
                obj.edgeTypeId = varargin{2};
                obj.layerIds = varargin{3};
                obj.verticesX = varargin{4};
                obj.verticesY = varargin{5};
                obj.type = varargin{6};
                obj.directed = varargin{7};
            else
                obj.id = SynthesisTemplate.GetNextNodeRegionId();
                obj.systemId = 0;
                obj.edgeTypeId = 0;
                obj.layerIds = 0;
                obj.verticesX = 0;
                obj.verticesY = 0;
                obj.type = EdgeRegion.POLYLINE_PERIMETER;
                obj.directed = 0;
            end
        end
        function GenerateEdges(obj)
            synthTemp = SynthesisTemplate.instance();
            system = synthTemp.city.systems{obj.systemId};
            if obj.type==EdgeRegion.POLYLINE_PERIMETER
                % find corresponding node id for each vertex
                nodeIds = zeros(length(obj.layerIds),1);
                for i=1:length(obj.layerIds)
                    for n=1:length(system.nodes)
                        [cVx cVy] = system.nodes(n).cell.GetVertices();
                        if system.nodes(n).layer.id==obj.layerIds(i) && ...
                                inpolygon(obj.verticesX(i),obj.verticesY(i),cVx,cVy)
                            nodeIds(i) = system.nodes(n).id;
                        end
                    end
                end
                for i=1:length(nodeIds)-1
                    origin = system.nodes([system.nodes.id]==nodeIds(i));
                    destination = system.nodes([system.nodes.id]==nodeIds(i+1));
                    if ~isempty(origin) && ~isempty(destination)
                        system.edges(end+1) = Edge(...
                            system.nodes([system.nodes.id]==nodeIds(i)), ...
                            system.nodes([system.nodes.id]==nodeIds(i+1)), ...
                            synthTemp.edgeTypes([synthTemp.edgeTypes.id]==obj.edgeTypeId), ...
                            obj.directed);
                    end
                end
            elseif obj.type==EdgeRegion.ORTHOGONAL_NEIGHBORS
                % TODO
                ME = MException('Not Implemented', ...
                    'Option ORTHOGONAL_NEIGHBORS not yet implemented.');
                throw(ME);
            elseif obj.type==EdgeRegion.ALL_NEIGHBORS
                % TODO
                ME = MException('Not Implemented', ...
                    'Option ALL_NEIGHBORS not yet implemented.');
                throw(ME);
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
                        if ~isempty(origin) && ~isempty(destination)
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