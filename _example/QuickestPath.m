%% QuickestPath Class Definition
% The QuickestPath behavior identifies the shortest path with respect to
% time between two locations. An implementation of Djikstra's algorithm is
% used to find the shortest path using Euclidean distance for edge lengths
% and speed specified by the `Speed` edge type attribute.
%
% 14-June, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef QuickestPath < SystemBehavior
    properties
        origin;       % origin location (x, y, layer id)
        destination;  % destination location (x, y, layer id)
        path;         % the evaluated path (list of edge ids)
    end
    methods
        %% QuickestPath Constructor
        % Instantiates a new QuickestPath object.
        % 
        % obj = QuickestPath(origin, destination)
        %   obj:            the new QuickestPath object
        %   origin:         the origin location (x-coord, y-coord, layer id)
        %   destination:    the destination location (x-coord, y-coord, layer id)
        function obj = QuickestPath(origin, destination)
            obj = obj@SystemBehavior('Quickest Path', ...
                ['Gets the time corresponding to the ' ...
                'quickest path between origin and destination nodes. ' ...
                'Uses Djikstra''s algorithm with Euclidean edge lengths.'], ...
                'hour','[0,inf)');
            obj.origin = origin;
            obj.destination = destination;
        end
    end
    methods(Access=protected)
        %% EvaluateImpl Function
        % Evalutaes the behavior for a specified system. Note: the
        % superclass Evaluate function should be used for evaluation during
        % execution.
        %
        % val = obj.EvaluateImpl(system)
        %   val:    the evaluated value
        %   obj:    the QuickestPath object handle
        %   system: the system in which this behavior is evaluated
        function val = EvaluateImpl(obj,system)
            originId = obj.GetNodeId(system,obj.origin);
            destinationId = obj.GetNodeId(system,obj.destination);
            
            lengths = zeros(length(system.edges),1);
            for i=1:length(system.edges)
                speed = system.edges(i).GetEdgeTypeAttributeValue('speed');
                if ~isempty(speed) && speed > 0
                    lengths(i) = system.edges(i).GetEuclideanLength()/speed;
                else
                    lengths(i) = inf;
                end
            end
            
            distance = inf*ones(1,length(system.nodes));           % unknown distance from origin to destination
            previousNodeId = zeros(1,length(system.nodes));        % previous node in optimal path from origin
            previousEdgeId = zeros(1,length(system.nodes));        % previous edge in optimal path from origin
            distance([system.nodes.id]==originId) = 0;         % distance from origin to origin
            Q = 1:length(system.nodes);                            % list of 'optimized' nodes
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
                for i=1:length(system.edges)
                    v = 0;
                    if system.edges(i).origin.id==system.nodes(u).id
                        % check if edge i goes from u-->v
                        for v=1:length(system.nodes)
                            if system.edges(i).destination.id==system.nodes(v).id
                                break;
                            end
                        end
                    elseif ~system.edges(i).directed && (system.edges(i).destination.id==system.nodes(u).id)
                        % check if edge i goes from v<-->u
                        for v=1:length(system.nodes)
                            if system.edges(i).origin.id==system.nodes(v).id
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
                            previousNodeId(v) = system.nodes(u).id;
                            previousEdgeId(v) = system.edges(i).id;
                        end
                    end
                end
            end
            
            obj.path = [];
            val = 0;
            u = destinationId;
            while previousNodeId(u)>0
                edge = system.edges([system.edges.id]==previousEdgeId(u));
                obj.path = [previousEdgeId(u) obj.path];
                val = val + edge.GetEuclideanLength()/edge.type.attributes(strcmpi({edge.type.attributes.name},'speed')).value;
                u = previousNodeId(u);
            end
        end
    end
    methods(Access=private,Static)
        %% GetNodeId Function
        % Gets the id of the node at a particular location.
        %
        % id = GetNodeId(system,location)
        %   id:         the id of the node
        %   system:     the system in which to look
        %   location:   the location at which to look (x,y,layer id)
        function id = GetNodeId(system,location)
            id = 0;
            for i=1:length(system.nodes)
                node = system.nodes(i);
                [cVx cVy] = node.cell.GetVertices();
                if node.layer.id==location(3) && ...
                        node.cell.ContainsPoint(location(1),location(2))
                    id = node.id;
                    break;
                end
            end
        end
    end
end