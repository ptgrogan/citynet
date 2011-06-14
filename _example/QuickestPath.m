classdef QuickestPath < SystemBehavior
    properties
        origin;       % origin location (x, y, layer id)
        destination;  % destination location (x, y, layer id)
    end
    methods
        function obj = QuickestPath(origin, destination)
            obj = obj@SystemBehavior('Quickest Path', ...
                ['Gets the list of edge IDs corresponding to the ' ...
                'quickest path between origin and destination nodes. ' ...
                'Uses Djikstra''s algorithm with Manhattan edge lengths.'], ...
                '-','-');
            obj.origin = origin;
            obj.destination = destination;
        end
        function val = EvaluateImpl(obj,system)
            originId = obj.GetNodeId(system,obj.origin);
            destinationId = obj.GetNodeId(system,obj.destination);
            
            lengths = zeros(length(system.edges),1);
            for i=1:length(system.edges)
                if ~isempty(system.edges(i).type.attributes) && ...
                        sum(strcmp({system.edges(i).type.attributes.name},'Speed'))==1
                    speed = system.edges(i).type.attributes(strcmp({system.edges(i).type.attributes.name},'Speed')).value;
                    lengths(i) = sqrt(sum((system.edges(i).origin.cell.location-system.edges(i).destination.cell.location).^2))/speed;
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
            
            val = [];
            u = destinationId;
            while previousNodeId(u)>0
                val = [previousEdgeId(u) val];
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
                        sum(inpolygon(location(1),location(2),cVx,cVy))==1
                    id = node.id;
                    break;
                end
            end
        end
    end
end