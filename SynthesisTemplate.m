%% SynthesisTemplate Class Definition
% The SynthesisTemplate is the class of the primary object used to maintain
% state in the synthesis template application. Its attributes include a
% System object which maintains state for cells and edges, and sets of both
% NodeType objects and EdgeType objects.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef SynthesisTemplate < Singleton
    properties
        city;               % mutable City object to contain state
        nodeTypes;          % mutable object array of NodeType objects
        nextNodeTypeId;     % next available identifier for node types
        nextNodeTypeAttributeId; % next available identifier for node type attributes
        edgeTypes;          % mutable object array of EdgeType objects
        nextEdgeTypeId;     % next available identifier for edge types
        nextEdgeTypeAttributeId; % next available identifier for edge type attributes
        nextCellId;         % next available identifier for cells
        nextLayerId;        % next available identifier for layers
        nextSystemId;       % next available identifier for systems
        nextNodeId;         % next available identifier for nodes
        nextEdgeId;         % next available identifier for edges
    end
    methods(Access=private)
        %% SynthesisTemplate Constructor
        % Creates a new instance of a SynthesisTemplate object with a new 
        % city. (Note: this constructor is private to support the 
        % Singleton design pattern, which allows only one active 
        % SynthesisTemplate at a time.)
        %
        % obj = SynthesisTemplate()
        
        function obj = SynthesisTemplate()
            obj.city = City();
            obj.nodeTypes = NodeType.empty();
            obj.nextNodeTypeId = 1;
            obj.nextNodeTypeAttributeId = 1;
            obj.edgeTypes = EdgeType.empty();
            obj.nextEdgeTypeId = 1;
            obj.nextEdgeTypeAttributeId = 1;
            obj.nextCellId = 1;
            obj.nextLayerId = 1;
            obj.nextSystemId = 1;
            obj.nextNodeId = 1;
            obj.nextEdgeId = 1;
        end
    end
    methods(Static)
        %% Concrete implementation
        % See Singleton superclass.
        function obj = instance()
            persistent uniqueInstance
            if isempty(uniqueInstance)
                obj = SynthesisTemplate();
                uniqueInstance = obj;
            else
                obj = uniqueInstance;
            end
        end
    end
    methods
        %% RenderLayer Function
        % Displays a specific layer of the city using a 2-D plot in a new
        % figure.
        function RenderLayer(obj,layerId)
            figure
            axis ij square
            title([obj.city.name ', ' obj.city.layers([obj.city.layers.id]==layerId).name ' Layer'])
            xlabel('x (km)')
            ylabel('y (km)')
            hold on
            filled = zeros(length(obj.city.cells),1);
            % display the nodes
            nodeTypeColorMap = obj.GetNodeTypeColorMap;
            for s=1:length(obj.city.systems)
                for n=1:length(obj.city.systems(s).nodes)
                    node = obj.city.systems(s).nodes(n);
                    if node.layerId==layerId
                        filled(node.cellId)=1;
                        cell = obj.city.cells([obj.city.cells.id]==node.cellId);
                        x = cell.location(1);
                        w = cell.dimensions(1);
                        y = cell.location(2);
                        h = cell.dimensions(2);
                        patch([x; x+w; x+w; x], [y; y; y+h; y+h], ...
                            nodeTypeColorMap(node.typeId,:));
                    end
                end
            end
            % fill in blank squares
            for i=1:length(filled)
                if filled(i)==0
                    cell = obj.city.cells([obj.city.cells.id]==i);
                    x = cell.location(1);
                    w = cell.dimensions(1);
                    y = cell.location(2);
                    h = cell.dimensions(2);
                    patch([x; x+w; x+w; x], [y; y; y+h; y+h], ...
                        [1 1 1],'FaceAlpha',.5);
                end
            end
            % display the edges
            edgeTypeColorMap = obj.GetEdgeTypeColorMap;
            for s=1:length(obj.city.systems)
                for e=1:length(obj.city.systems(s).edges)
                    edge = obj.city.systems(s).edges(e);
                    originNode = obj.city.systems(s).nodes([obj.city.systems(s).nodes.id]==edge.originNodeId);
                    destinationNode = obj.city.systems(s).nodes([obj.city.systems(s).nodes.id]==edge.destinationNodeId);                    
                    if originNode.layerId==layerId && destinationNode.layerId==layerId
                        originCell = obj.city.cells([obj.city.cells.id]==originNode.cellId);
                        destinationCell = obj.city.cells([obj.city.cells.id]==destinationNode.cellId);
                        x1 = originCell.location(1)+originCell.dimensions(1)/2;
                        x2 = destinationCell.location(1)+destinationCell.dimensions(1)/2;
                        y1 = originCell.location(2)+originCell.dimensions(2)/2;
                        y2 = destinationCell.location(2)+destinationCell.dimensions(2)/2;
                        line([x1;x2], [y1;y2],'Color',edgeTypeColorMap(edge.typeId,:));
                        line(x1,y1,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','o');
                        line(x2,y2,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','.');
                        if ~edge.directed
                            % if the edge is undirected, draw origin and
                            % destination symbols on both ends
                            line(x1,y1,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','.');
                            line(x2,y2,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','o');
                        end
                    end
                end
            end
            hold off
        end
        
        %% RenderSystem Function
        % Renders a single system using a 3-D plot in a new figure.
        function RenderSystem(obj,systemId)
            system = obj.city.systems([obj.city.systems.id]==systemId);
            figure
            zlabel('Layer')
            xlabel('x (km)')
            ylabel('y (km)')
            title([obj.city.name ', ' obj.city.systems([obj.city.systems.id]==systemId).name ' System'])
            axis ij equal
            hold on
            view(3)
            filled = zeros(length(obj.city.cells),length(obj.city.layers));
            % display nodes
            nodeTypeColorMap = obj.GetNodeTypeColorMap;
            for n=1:length(system.nodes)
                node = system.nodes(n);
                filled(node.cellId,node.layerId)=1;
                cell = obj.city.cells([obj.city.cells.id]==node.cellId);
                x = cell.location(1);
                w = cell.dimensions(1);
                y = cell.location(2);
                h = cell.dimensions(2);
                z = obj.city.layers([obj.city.layers.id]==node.layerId).displayHeight;
                patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z], ...
                    nodeTypeColorMap(node.typeId,:),'FaceAlpha',.75);
            end
            % fill in blank squares
            for i=1:size(filled,1)
                for j=1:size(filled,2)
                    if sum(filled(:,j))>0 && filled(i,j)==0
                        cell = obj.city.cells([obj.city.cells.id]==i);
                        x = cell.location(1);
                        w = cell.dimensions(1);
                        y = cell.location(2);
                        h = cell.dimensions(2);
                        z = obj.city.layers(j).displayHeight;
                        patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z],...
                            [1 1 1],'FaceAlpha',.5);
                    end
                end
            end
            filteredLayerHeights = [obj.city.layers(sum(filled,1)>0).displayHeight];
            filteredLayers = {obj.city.layers(sum(filled,1)>0).name};
            [vals order] = sort(filteredLayerHeights);
            set(gca,'ZTick',filteredLayerHeights(order))
            set(gca,'ZTickLabel',filteredLayers(order))
            % display edges
            edgeTypeColorMap = obj.GetEdgeTypeColorMap;
            for e=1:length(system.edges)
                edge = system.edges(e);
                originNode = system.nodes([system.nodes.id]==edge.originNodeId);
                destinationNode = system.nodes([system.nodes.id]==edge.destinationNodeId);
                originCell = obj.city.cells([obj.city.cells.id]==originNode.cellId);
                destinationCell = obj.city.cells([obj.city.cells.id]==destinationNode.cellId);
                x1 = originCell.location(1)+originCell.dimensions(1)/2;
                x2 = destinationCell.location(1)+destinationCell.dimensions(1)/2;
                y1 = originCell.location(2)+originCell.dimensions(2)/2;
                y2 = destinationCell.location(2)+destinationCell.dimensions(2)/2;
                z1 = obj.city.layers([obj.city.layers.id]==originNode.layerId).displayHeight;
                z2 = obj.city.layers([obj.city.layers.id]==destinationNode.layerId).displayHeight;
                line([x1;x2], [y1;y2], [z1;z2], ...
                    'Color',edgeTypeColorMap(edge.typeId,:));
                line(x1,y1,z1,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','o');
                line(x2,y2,z2,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','.');
                if ~edge.directed
                    % if the edge is undirected, draw origin and
                    % destination symbols on both ends
                    line(x1,y1,z1,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','.');
                    line(x2,y2,z2,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','o');
                end
            end
            hold off
        end
        
        %% RenderCity Function
        % Renders the complete city using a 3-D plot in a new figure.
        function RenderCity(obj)
            figure
            zlabel('Layer')
            xlabel('x (km)')
            ylabel('y (km)')
            title(obj.city.name)
            axis ij equal
            hold on
            view(3)
            filled = zeros(length(obj.city.cells),length(obj.city.layers));
            % display nodes
            nodeTypeColorMap = obj.GetNodeTypeColorMap;
            for s=1:length(obj.city.systems)
                system = obj.city.systems(s);
                for n=1:length(system.nodes)
                    node = system.nodes(n);
                    filled(node.cellId,node.layerId)=1;
                    cell = obj.city.cells([obj.city.cells.id]==node.cellId);
                    x = cell.location(1);
                    w = cell.dimensions(1);
                    y = cell.location(2);
                    h = cell.dimensions(2);
                    z = obj.city.layers([obj.city.layers.id]==node.layerId).displayHeight;
                    patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z], ...
                        nodeTypeColorMap(node.typeId,:),'FaceAlpha',.75);
                end
            end
            % fill in blank squares
            for i=1:size(filled,1)
                for j=1:size(filled,2)
                    if sum(filled(:,j))>0 && filled(i,j)==0
                        cell = obj.city.cells([obj.city.cells.id]==i);
                        x = cell.location(1);
                        w = cell.dimensions(1);
                        y = cell.location(2);
                        h = cell.dimensions(2);
                        z = obj.city.layers(j).displayHeight;
                        patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z],...
                            [1 1 1],'FaceAlpha',.5);
                    end
                end
            end
            filteredLayerHeights = [obj.city.layers(sum(filled,1)>0).displayHeight];
            filteredLayers = {obj.city.layers(sum(filled,1)>0).name};
            [vals order] = sort(filteredLayerHeights);
            set(gca,'ZTick',filteredLayerHeights(order))
            set(gca,'ZTickLabel',filteredLayers(order))
            % display edges
            edgeTypeColorMap = obj.GetEdgeTypeColorMap;
            for s=1:length(obj.city.systems)
                system = obj.city.systems(s);
                for e=1:length(system.edges)
                    edge = system.edges(e);
                    originNode = system.nodes([system.nodes.id]==edge.originNodeId);
                    destinationNode = system.nodes([system.nodes.id]==edge.destinationNodeId);
                    originCell = obj.city.cells([obj.city.cells.id]==originNode.cellId);
                    destinationCell = obj.city.cells([obj.city.cells.id]==destinationNode.cellId);
                    x1 = originCell.location(1)+originCell.dimensions(1)/2;
                    x2 = destinationCell.location(1)+destinationCell.dimensions(1)/2;
                    y1 = originCell.location(2)+originCell.dimensions(2)/2;
                    y2 = destinationCell.location(2)+destinationCell.dimensions(2)/2;
                    z1 = obj.city.layers([obj.city.layers.id]==originNode.layerId).displayHeight;
                    z2 = obj.city.layers([obj.city.layers.id]==destinationNode.layerId).displayHeight;
                    line([x1;x2], [y1;y2], [z1;z2], ...
                        'Color',edgeTypeColorMap(edge.typeId,:));
                    line(x1,y1,z1,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','o');
                    line(x2,y2,z2,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','.');
                    if ~edge.directed
                        % if the edge is undirected, draw origin and
                        % destination symbols on both ends
                        line(x1,y1,z1,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','.');
                        line(x2,y2,z2,'Color',edgeTypeColorMap(edge.typeId,:),'Marker','o');
                    end
                end
            end
            hold off
        end
        
        %% GetNextNodeTypeId Function
        % Gets and increments the next node type identifier.
        function out = GetNextNodeTypeId(obj)
            out = obj.nextNodeTypeId;
            obj.nextNodeTypeId = obj.nextNodeTypeId + 1;
        end
        
        %% GetNextNodeTypeAttributeId Function
        % Gets and increments the next node type identifier.
        function out = GetNextNodeTypeAttributeId(obj)
            out = obj.nextNodeTypeAttributeId;
            obj.nextNodeTypeAttributeId = obj.nextNodeTypeAttributeId + 1;
        end
        
        %% GetNextEdgeTypeId Function
        % Gets and increments the next edge type identifier.
        function out = GetNextEdgeTypeId(obj)
            out = obj.nextEdgeTypeId;
            obj.nextEdgeTypeId = obj.nextEdgeTypeId + 1;
        end
        
        %% GetNextEdgeTypeAttributeId Function
        % Gets and increments the next edge type identifier.
        function out = GetNextEdgeTypeAttributeId(obj)
            out = obj.nextEdgeTypeAttributeId;
            obj.nextEdgeTypeAttributeId = obj.nextEdgeTypeAttributeId + 1;
        end
        
        %% GetNextCellId Function
        % Gets and increments the next cell identifier.
        function out = GetNextCellId(obj)
            out = obj.nextCellId;
            obj.nextCellId = obj.nextCellId + 1;
        end
        
        %% GetNextLayerId Function
        % Gets and increments the next layer identifier.
        function out = GetNextLayerId(obj)
            out = obj.nextLayerId;
            obj.nextLayerId = obj.nextLayerId + 1;
        end
                
        %% GetNextSystemId Function
        % Gets and increments the next system identifier.
        function out = GetNextSystemId(obj)
            out = obj.nextSystemId;
            obj.nextSystemId = obj.nextSystemId + 1;
        end
                
        %% GetNextNodeId Function
        % Gets and increments the next node identifier.
        function out = GetNextNodeId(obj)
            out = obj.nextNodeId;
            obj.nextNodeId = obj.nextNodeId + 1;
        end
                
        %% GetNextEdgeId Function
        % Gets and increments the next edge identifier.
        function out = GetNextEdgeId(obj)
            out = obj.nextEdgeId;
            obj.nextEdgeId = obj.nextEdgeId + 1;
        end
    end
    methods(Access=private)
        %% GetNodeTypeColorMap Function
        % Returns a color map containing the specified display colors of 
        % each node type.
        function nodeTypeColorMap = GetNodeTypeColorMap(obj)
            nodeTypeColorMap = reshape([obj.nodeTypes.rgbColor],[length(obj.nodeTypes) 3]);
        end
        
        %% GetEdgeTypeColorMap Function
        % Returns a color map containing the specified display colors of 
        % each edge type.
        function edgeTypeColorMap = GetEdgeTypeColorMap(obj)
            edgeTypeColorMap = reshape([obj.edgeTypes.rgbColor],[length(obj.edgeTypes) 3]);
        end
    end
end