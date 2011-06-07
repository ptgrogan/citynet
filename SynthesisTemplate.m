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
        minIntersectionFraction; % lower bound of intersection area
                            % required to specify a node (0-1)
        nextNodeTypeId;     % next available identifier for node types
        nextNodeTypeAttributeId; % next available identifier for node type attributes
        nextEdgeTypeId;     % next available identifier for edge types
        nextEdgeTypeAttributeId; % next available identifier for edge type attributes
        nextCellId;         % next available identifier for cells
        nextLayerId;        % next available identifier for layers
        nextSystemId;       % next available identifier for systems
        nextNodeId;         % next available identifier for nodes
        nextEdgeId;         % next available identifier for edges
        nextNodeRegionId;   % next available identifier for node regions
        nextEdgeRegionId;   % next available identifier for edge regions
        nextCellRegionId;   % next available identifier for cell regions
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
            obj.minIntersectionFraction = 0.1;
            obj.nextNodeTypeId = 1;
            obj.nextNodeTypeAttributeId = 1;
            obj.nextEdgeTypeId = 1;
            obj.nextEdgeTypeAttributeId = 1;
            obj.nextCellId = 1;
            obj.nextLayerId = 1;
            obj.nextSystemId = 1;
            obj.nextNodeId = 1;
            obj.nextEdgeId = 1;
            obj.nextNodeRegionId = 1;
            obj.nextEdgeRegionId = 1;
            obj.nextCellRegionId = 1;
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
        %% GenerateCells Function
        % Generates cells for all defined cell regions.
        function GenerateCells(obj)
            for i=1:length(obj.city.cellRegions)
                obj.city.cellRegions(i).GenerateCells(obj.city);
            end
        end
        
        %% GenerateNodes Function
        % Generates nodes for all defined node regions in all systems.
        function GenerateNodes(obj)
            for i=1:length(obj.city.systems)
                system = obj.city.systems{i};
                for j=1:length(system.nodeRegions)
                    system.nodeRegions(j).GenerateNodes(system);
                end
            end
        end
        
        %% GenerateEdges Function
        % Generates edges for all defined edge regions in all systems.
        function GenerateEdges(obj)
            for i=1:length(obj.city.systems)
                system = obj.city.systems{i};
                for j=1:length(system.edgeRegions)
                    system.edgeRegions(j).GenerateEdges(system);
                end
            end
        end
        
        %% ClearCells Function
        % Clears all defined cells. Also calls ClearNodes.
        function ClearCells(obj)
            obj.ClearNodes();
            obj.city.cells = Cell.empty();
            obj.nextCellId = 1;
        end
        
        %% ClearNodes Function
        % Clears all defined nodes across all systems. Also calls
        % ClearEdges.
        function ClearNodes(obj)
            obj.ClearEdges();
            for i=1:length(obj.city.systems)
                obj.city.systems{i}.nodes = Node.empty();
            end
            obj.nextNodeId = 1;
        end
        
        %% ClearEdges Function
        % Clears all defined edges across all systems.
        function ClearEdges(obj)
            for i=1:length(obj.city.systems)
                obj.city.systems{i}.edges = Edge.empty();
            end
            obj.nextEdgeId = 1;
        end
        
        %% RenderNodeRegion3d Function
        % Renders a node region in the current 3D figure with a thick line, 
        % the same color as the node type color.
        function RenderNodeRegion3d(obj,nodeRegionId)
            [nodeRegion system] = obj.GetNodeRegionHandle(nodeRegionId);
            hold on
            if nodeRegion.type == nodeRegion.POLYGON;
                % close polygon shape: use first vertex as last
                x = 0;
            else
                % do not close polygon shape
                x = 1;
            end
            for i=1:length(nodeRegion.verticesX)-x
                x1 = nodeRegion.verticesX(i);
                x2 = nodeRegion.verticesX(mod(i,length(nodeRegion.verticesX))+1);
                y1 = nodeRegion.verticesY(i);
                y2 = nodeRegion.verticesY(mod(i,length(nodeRegion.verticesY))+1);
                z1 = system.layers([system.layers.id]==nodeRegion.layerId).displayHeight;
                z2 = system.layers([system.layers.id]==nodeRegion.layerId).displayHeight;
                line([x1;x2], [y1;y2], [z1;z2], ...
                    'Color',system.nodeTypes([system.nodeTypes.id]==nodeRegion.nodeTypeId).rgbColor, ...
                    'LineWidth',2);
            end
            hold off
        end
        
        %% RenderNodeRegion2d Function
        % Renders a node region in the current 2d figure with a thick line, 
        % the same color as the node type color.
        function RenderNodeRegion2d(obj,nodeRegionId)
            [nodeRegion system] = obj.GetNodeRegionHandle(nodeRegionId);
            hold on
            if nodeRegion.type == nodeRegion.POLYGON;
                % close polygon shape: use first vertex as last
                x = 0;
            else
                % do not close polygon shape
                x = 1;
            end
            for i=1:length(nodeRegion.verticesX)-x
                x1 = nodeRegion.verticesX(i);
                x2 = nodeRegion.verticesX(mod(i,length(nodeRegion.verticesX))+1);
                y1 = nodeRegion.verticesY(i);
                y2 = nodeRegion.verticesY(mod(i,length(nodeRegion.verticesY))+1);
                line([x1;x2], [y1;y2], ...
                    'Color',system.nodeTypes([system.nodeTypes.id]==nodeRegion.nodeTypeId).rgbColor, ...
                    'LineWidth',2);
            end
            hold off
        end
        
        %% RenderLayer Function
        % Displays a specific layer of the city using a 2-D plot in the
        % current figure.
        function RenderLayer(obj,layerId)
            [layer system] = obj.GetLayerHandle(layerId);
            title([obj.city.name ', ' layer.name ' Layer'])
            xlabel(['x (' obj.city.distanceUnits ')'])
            ylabel(['y (' obj.city.distanceUnits ')'])
            hold on
            nodeAlpha = 0.75;
            cellAlpha = 0.50;
            if obj.city.HasImage()
                imagesc([min(obj.city.imageVerticesX) max(obj.city.imageVerticesX)], ...
                [min(obj.city.imageVerticesY) max(obj.city.imageVerticesY)], ...
                obj.city.GetImage())
                nodeAlpha = 0.50;
                cellAlpha = 0.25;
            end
            filled = zeros(length(obj.city.cells),1);
            % display the nodes
            for n=1:length(system.nodes)
                node = system.nodes(n);
                if node.layer.id==layerId
                    filled([obj.city.cells.id]==node.cell.id)=1;
                    x = node.cell.location(1);
                    w = node.cell.dimensions(1);
                    y = node.cell.location(2);
                    h = node.cell.dimensions(2);
                    patch([x; x+w; x+w; x], [y; y; y+h; y+h], ...
                        node.type.rgbColor,'FaceAlpha',nodeAlpha);
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
                        [1 1 1],'FaceAlpha',cellAlpha);
                end
            end
            % display the edges
            for e=1:length(system.edges)
                edge = system.edges(e);
                if edge.origin.layer.id==layerId && edge.destination.layer.id==layerId
                    x1 = edge.origin.cell.location(1)+edge.origin.cell.dimensions(1)/2;
                    x2 = edge.destination.cell.location(1)+edge.destination.cell.dimensions(1)/2;
                    y1 = edge.origin.cell.location(2)+edge.origin.cell.dimensions(2)/2;
                    y2 = edge.destination.cell.location(2)+edge.destination.cell.dimensions(2)/2;
                    line([x1;x2], [y1;y2],'Color',edge.type.rgbColor);
                    line(x1,y1,'Color',edge.type.rgbColor,'Marker','o');
                    line(x2,y2,'Color',edge.type.rgbColor,'Marker','.');
                    if ~edge.directed
                        % if the edge is undirected, draw origin and
                        % destination symbols on both ends
                        line(x1,y1,'Color',edge.type.rgbColor,'Marker','.');
                        line(x2,y2,'Color',edge.type.rgbColor,'Marker','o');
                    end
                end
            end
            axis ij square tight
            hold off
        end
        
        %% RenderCells Function
        % renders the cells using a 2-D plot in the current figure.
        function RenderCells(obj)
            % options start
            showCellIds = 1;
            edgeColor = [0 0 0];
            textColor = [0 0 0];
            % options end
            title([obj.city.name ' Cells'])
            xlabel(['x (' obj.city.distanceUnits ')'])
            ylabel(['y (' obj.city.distanceUnits ')'])
            hold on
            if ischar(obj.city.imagePath) && ~strcmp(obj.city.imagePath,'')
                imagesc([min(obj.city.imageVerticesX) max(obj.city.imageVerticesX)], ...
                [min(obj.city.imageVerticesY) max(obj.city.imageVerticesY)], ...
                obj.city.GetImage())
            end
            for i=1:length(obj.city.cells)
                [cVx cVy] = obj.city.cells(i).GetVertices();
                patch(cVx, cVy, [1 1 1],'FaceAlpha',0.1,'EdgeColor',edgeColor);
                if showCellIds
                    text(sum(cVx)/4,sum(cVy)/4, ...
                        num2str(obj.city.cells(i).id), ...
                        'HorizontalAlignment','center','Color',textColor)
                end
            end
            axis ij square tight
            hold off
        end
        
        %% RenderCity Function
        % Renders an entire city (all systems) using a 3-D plot in the
        % current figure.
        function RenderCity(obj)
            zlabel('Layer')
            xlabel(['x (' obj.city.distanceUnits ')'])
            ylabel(['y (' obj.city.distanceUnits ')'])
            title(obj.city.name)
            hold on
            view(3)
            nodeAlpha = 0.75;
            cellAlpha = 0.50;
            if ischar(obj.city.imagePath) && ~strcmp(obj.city.imagePath,'')
                I = obj.city.GetImage();
                surf(linspace(min(obj.city.imageVerticesX),max(obj.city.imageVerticesX),size(I,1)), ...
                    linspace(min(obj.city.imageVerticesY),max(obj.city.imageVerticesY),size(I,2)), ...
                    zeros(size(I(:,:,1))), ...
                    reshape(1:size(I,1)*size(I,2),size(I,1),size(I,2)),'EdgeColor','none')
                colormap(reshape(double(I)/255,size(I,1)*size(I,2),3))
                nodeAlpha = 0.50;
                cellAlpha = 0.25;
            end
            layers = obj.city.GetLayers();
            filled = zeros(length(obj.city.cells),length(layers));
            for s=1:length(obj.city.systems)
                system = obj.city.systems{s};
                % display nodes
                for n=1:length(system.nodes)
                    node = system.nodes(n);
                    filled([obj.city.cells.id]==node.cell.id,[layers.id]==node.layer.id)=1;
                    x = node.cell.location(1);
                    w = node.cell.dimensions(1);
                    y = node.cell.location(2);
                    h = node.cell.dimensions(2);
                    z = node.layer.displayHeight;
                    patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z], ...
                        node.type.rgbColor,'FaceAlpha',nodeAlpha);
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
                        z = layers(j).displayHeight;
                        patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z],...
                            [1 1 1],'FaceAlpha',cellAlpha);
                    end
                end
            end
            filteredLayerHeights = [layers(sum(filled,1)>0).displayHeight];
            filteredLayers = {layers(sum(filled,1)>0).name};
            [vals order] = sort(filteredLayerHeights);
            set(gca,'ZTick',filteredLayerHeights(order))
            set(gca,'ZTickLabel',filteredLayers(order))
            for s=1:length(obj.city.systems)
                system = obj.city.systems{s};
                for e=1:length(system.edges)
                    edge = system.edges(e);
                    x1 = edge.origin.cell.location(1)+edge.origin.cell.dimensions(1)/2;
                    x2 = edge.destination.cell.location(1)+edge.destination.cell.dimensions(1)/2;
                    y1 = edge.origin.cell.location(2)+edge.origin.cell.dimensions(2)/2;
                    y2 = edge.destination.cell.location(2)+edge.destination.cell.dimensions(2)/2;
                    z1 = edge.origin.layer.displayHeight;
                    z2 = edge.destination.layer.displayHeight;
                    line([x1;x2],[y1;y2],[z1;z2],'Color',edge.type.rgbColor);
                    line(x1,y1,z1,'Color',edge.type.rgbColor,'Marker','o');
                    line(x2,y2,z2,'Color',edge.type.rgbColor,'Marker','.');
                    if ~edge.directed
                        % if the edge is undirected, draw origin and
                        % destination symbols on both ends
                        line(x1,y1,z1,'Color',edge.type.rgbColor,'Marker','.');
                        line(x2,y2,z2,'Color',edge.type.rgbColor,'Marker','o');
                    end
                end
            end
            axis ij equal tight
            hold off
        end
        
        %% RenderSystem Function
        % Renders a single system using a 3-D plot in the current figure.
        function RenderSystem(obj,systemId)
            system = obj.city.systems{systemId};
            zlabel('Layer')
            xlabel(['x (' obj.city.distanceUnits ')'])
            ylabel(['y (' obj.city.distanceUnits ')'])
            title([obj.city.name ', ' system.name ' System'])
            hold on
            view(3)
            nodeAlpha = 0.75;
            cellAlpha = 0.50;
            if ischar(obj.city.imagePath) && ~strcmp(obj.city.imagePath,'')
                I = obj.city.GetImage();
                surf(linspace(min(obj.city.imageVerticesX),max(obj.city.imageVerticesX),size(I,1)), ...
                    linspace(min(obj.city.imageVerticesY),max(obj.city.imageVerticesY),size(I,2)), ...
                    zeros(size(I(:,:,1))), ...
                    reshape(1:size(I,1)*size(I,2),size(I,1),size(I,2)),'EdgeColor','none')
                colormap(reshape(double(I)/255,size(I,1)*size(I,2),3))
                nodeAlpha = 0.50;
                cellAlpha = 0.25;
            end
            filled = zeros(length(obj.city.cells),length(system.layers));
            % display nodes
            for n=1:length(system.nodes)
                node = system.nodes(n);
                filled([obj.city.cells.id]==node.cell.id,[system.layers.id]==node.layer.id)=1;
                x = node.cell.location(1);
                w = node.cell.dimensions(1);
                y = node.cell.location(2);
                h = node.cell.dimensions(2);
                z = node.layer.displayHeight;
                patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z], ...
                    node.type.rgbColor,'FaceAlpha',nodeAlpha);
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
                        z = system.layers(j).displayHeight;
                        patch([x;x+w;x+w;x],[y;y;y+h;y+h],[z;z;z;z],...
                            [1 1 1],'FaceAlpha',cellAlpha);
                    end
                end
            end
            filteredLayerHeights = [system.layers(sum(filled,1)>0).displayHeight];
            filteredLayers = {system.layers(sum(filled,1)>0).name};
            [vals order] = sort(filteredLayerHeights);
            set(gca,'ZTick',filteredLayerHeights(order))
            set(gca,'ZTickLabel',filteredLayers(order))
            % display edges
            for e=1:length(system.edges)
                edge = system.edges(e);
                x1 = edge.origin.cell.location(1)+edge.origin.cell.dimensions(1)/2;
                x2 = edge.destination.cell.location(1)+edge.destination.cell.dimensions(1)/2;
                y1 = edge.origin.cell.location(2)+edge.origin.cell.dimensions(2)/2;
                y2 = edge.destination.cell.location(2)+edge.destination.cell.dimensions(2)/2;
                z1 = edge.origin.layer.displayHeight;
                z2 = edge.destination.layer.displayHeight;
                line([x1;x2],[y1;y2],[z1;z2],'Color',edge.type.rgbColor);
                line(x1,y1,z1,'Color',edge.type.rgbColor,'Marker','o');
                line(x2,y2,z2,'Color',edge.type.rgbColor,'Marker','.');
                if ~edge.directed
                    % if the edge is undirected, draw origin and
                    % destination symbols on both ends
                    line(x1,y1,z1,'Color',edge.type.rgbColor,'Marker','.');
                    line(x2,y2,z2,'Color',edge.type.rgbColor,'Marker','o');
                end
            end
            axis ij equal tight
            hold off
        end
        
        %% RenderSystemPath Function
        % Renders a path (list of edge IDs) within a system in the 
        % existing figure using a thick black line.
        function RenderSystemPath(obj,systemId,path)
            system = obj.city.systems{systemId};
            hold on
            for i=1:length(path)
                edge = system.edges([system.edges.id]==path(i));
                x1 = edge.origin.cell.location(1)+edge.origin.cell.dimensions(1)/2;
                x2 = edge.destination.cell.location(1)+edge.destination.cell.dimensions(1)/2;
                y1 = edge.origin.cell.location(2)+edge.origin.cell.dimensions(2)/2;
                y2 = edge.destination.cell.location(2)+edge.destination.cell.dimensions(2)/2;
                z1 = edge.origin.layer.displayHeight;
                z2 = edge.destination.layer.displayHeight;
                line([x1;x2], [y1;y2], [z1;z2], 'Color',[0 0 0],'LineWidth',2);
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
                
        %% GetNextNodeRegionId Function
        % Gets and increments the next node region identifier.
        function out = GetNextNodeRegionId(obj)
            out = obj.nextNodeRegionId;
            obj.nextNodeRegionId = obj.nextNodeRegionId + 1;
        end
                
        %% GetNextEdgeRegionId Function
        % Gets and increments the next edge region identifier.
        function out = GetNextEdgeRegionId(obj)
            out = obj.nextEdgeRegionId;
            obj.nextEdgeRegionId = obj.nextEdgeRegionId + 1;
        end
                
        %% GetNextCellRegionId Function
        % Gets and increments the next cell region identifier.
        function out = GetNextCellRegionId(obj)
            out = obj.nextCellRegionId;
            obj.nextCellRegionId = obj.nextCellRegionId + 1;
        end
    end
    methods(Access=private)
        %% GetLayerHandle Function
        % Returns a layer and system handle from layer id.
        function [layer system] = GetLayerHandle(obj,layerId)
            layer = [];
            system = [];
            for i=1:length(obj.city.systems)
                for j=1:length(obj.city.systems{i}.layers)
                    if layerId == obj.city.systems{i}.layers(j).id
                        system = obj.city.systems{i};
                        layer = system.layers(j);
                        break
                    end
                end
                if ~isempty(layer)
                    break
                end
            end
        end
        
        %% GetNodeRegionHandle Function
        % Returns a node region and system handle from node region id.
        function [nodeRegion system] = GetNodeRegionHandle(obj,nodeRegionId)
            nodeRegion = [];
            system = [];
            for i=1:length(obj.city.systems)
                for j=1:length(obj.city.systems{i}.nodeRegions)
                    if nodeRegionId == obj.city.systems{i}.nodeRegions(j).id
                        system = obj.city.systems{i};
                        nodeRegion = system.nodeRegions(j);
                        break
                    end
                end
                if ~isempty(nodeRegion)
                    break
                end
            end
        end
        
        %% GetEdgeRegionHandle Function
        % Returns an edge region and system handle from edge region id.
        function [edgeRegion system] = GetEdgeRegionHandle(obj,edgeRegionId)
            edgeRegion = [];
            system = [];
            for i=1:length(obj.city.systems)
                for j=1:length(obj.city.systems{i}.edgeRegion)
                    if edgeRegionId == obj.city.systems{i}.edgeRegion(j).id
                        system = obj.city.systems{i};
                        edgeRegion = system.edgeRegion(j);
                        break
                    end
                end
                if ~isempty(edgeRegion)
                    break
                end
            end
        end
    end
end