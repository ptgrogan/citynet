%% System Class Definition
% An instance of the System class contains node and edge information on
% multiple layers.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef System < handle
    properties
        id;                 % unique indentifier, integer
        name;               % layer name, string
        description;        % layer description, string
        nodes;              % mutable object array of Node objects
        edges;              % mutable object array of Edge objects
    end
    methods
        %% System Constructor
        % Instantiates a new System object with specified name and
        % description.
        %
        % obj = System(id, name, description)
        %   id:             unique identifier
        %   name:           system name (string)
        %   description:    system description (string)
        %
        % obj = System(name, description)
        %   name:           system name (string)
        %   description:    system description (string)
        %
        % obj = System()
        function obj = System(varargin)
            if nargin==3
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
            elseif nargin==2
                obj.id = SynthesisTemplate.instance().GetNextSystemId();
                obj.name = varargin{1};
                obj.description = varargin{2};
            else
                obj.id = SynthesisTemplate.instance().GetNextSystemId();
                obj.name = ['System ' num2str(obj.id)];
                obj.description = '';
            end
            obj.nodes = Node.empty();
            obj.edges = Edge.empty();
        end
        
        %% GetShortestPath Function
        % Gets the path (set of edges) corresponding to the the shortest
        % path between an origin and destination cell IDs. Uses Djikstra's
        % algorithm with Euclidean distances for edge weights.
        %
        % path = GetShortestPath(obj,originId,destinationId)
        %   originId:       origin node identifier
        %   destinationId:  destination node identifier
        function path = GetShortestPath(obj,originId,destinationId)
            lengths = zeros(length(obj.edges),1);
            for i=1:length(obj.edges)
                lengths(i) = sqrt(sum((obj.edges(i).origin.cell.location-obj.edges(i).destination.cell.location).^2));
            end
            path = obj.GetShortestPathWithEdgeLengths(originId,destinationId,lengths);
        end
        
        %% GetPathDistance Function
        % Gets the length of a path (set of edges). Uses Euclidean
        % distances for edge lengths.
        %
        % out = GetPathDistance(obj,path)
        %   path:   an array of edge identifiers
        function out = GetPathDistance(obj,path)
            out = 0;
            for i=1:length(path)
                edge = obj.edges([obj.edges.id]==path(i));
                out = out + sqrt(sum((edge.origin.cell.location-edge.destination.cell.location).^2));
            end
        end
    end
    methods(Access=protected)
        %% GetShortestPathWithEdgeLengths
        % Gets the path (set of edge IDs) corresponding to the the shortest
        % path between an origin and destination cell IDs. Uses Djikstra's
        % algorithm with specified edge lengths.
        %
        % path = GetShortestPathWithEdgeLengths(obj,origin,destination,lengths)
        %   originId:       origin node identifier
        %   destinationId:  destination node identifier
        %   lengths:        lengths of edges
        function path = GetShortestPathWithEdgeLengths(obj,originId,destinationId,lengths)
            distance = inf*ones(1,length(obj.nodes));           % unknown distance from origin to destination
            previousNodeId = zeros(1,length(obj.nodes));        % previous node in optimal path from origin
            previousEdgeId = zeros(1,length(obj.nodes));        % previous edge in optimal path from origin
            distance([obj.nodes.id]==originId) = 0;             % distance from origin to origin
            Q = 1:length(obj.nodes);                            % list of 'optimized' nodes
            while ~isempty(Q)
                % u is the position of the node in Q with smallest distance
                u = Q(find(distance(Q)==min(distance(Q)),1));
                if distance(u) == inf
                    % all remaining nodes are inaccessible from origin
                    break;                          
                end
                % remove u from Q
                if find(Q==u,1)==1
                    Q = Q(2:end);
                elseif find(Q==u,1)==length(Q)
                    Q = Q(1:end-1);
                else
                    Q = [Q(1:find(Q==u,1)-1) Q(find(Q==u,1)+1:end)];
                end
                % for each adjacent node v that has not yet been removed from Q
                for i=1:length(obj.edges)
                    v = 0;
                    if obj.edges(i).origin.id==obj.nodes(u).id
                        % check if edge i goes from u-->v
                        for v=1:length(obj.nodes)
                            if obj.edges(i).destination.id==obj.nodes(v).id
                                break;
                            end
                        end
                    elseif ~obj.edges(i).directed && (obj.edges(i).destination.id==obj.nodes(u).id)
                        % check if edge i goes from v<-->u
                        for v=1:length(obj.nodes)
                            if obj.edges(i).origin.id==obj.nodes(v).id
                                break;
                            end
                        end
                    end
                    % if edge i is either u-->v or v<-->u and v is not yet
                    % removed from Q...
                    if v > 0 && ~isempty(find(Q==v,1))
                        % find new distance
                        alt = distance(u) + lengths(i);
                        if alt < distance(v)
                            % update best distance and previous cell
                            distance(v) = alt;
                            previousNodeId(v) = obj.nodes(u).id;
                            previousEdgeId(v) = obj.edges(i).id;
                        end
                    end
                end
            end
            
            path = [];
            u = destinationId;
            while previousNodeId(u)>0
                path = [previousEdgeId(u) path];
                u = previousNodeId(u);
            end
        end
    end
end