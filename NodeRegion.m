%% NodeRegion Class Definition
% A NodeRegion specifies a spatial area over which nodes should be
% generated. The NodeRegion class was created to be able to specify nodes without
% relying on cellular definitions.
%
% 7-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NodeRegion < AbstractRegion
    properties(Constant)
        UNDEFINED = 0;      % type not defined
        POLYGON = 1;        % generates nodes within a polygonal region
        POLYLINE = 2;       % generates nodes along a polyline
        POLYPOINT = 3;      % generates nodes at polypoints
    end
    properties
        id;                 % unique identifier for node region
        nodeTypeId;         % node type id for assignment
        layerId;            % layer id for assignment
        type;               % type of node generation in region
        description;        % description of region, string
    end
    methods
        %% NodeRegion Constructor
        % Instantiates a new NodeRegion with specified parameters.
        %
        % obj = NodeRegion(id, nodeTypeId, layerIds, verticesX, verticesY, 
        %           type, description)
        %   id:         unique identifier for node region
        %   nodeTypeId: node type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   type:       constant parameter to define type of generation
        %   description:description of the region, string
        %
        % obj = NodeRegion(nodeTypeId, layerIds, verticesX, verticesY, 
        %           type, description)
        %   nodeTypeId: node type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   type:       constant parameter to define type of generation
        %   description:description of the region, string
        %
        % obj = NodeRegion()
        function obj = NodeRegion(varargin)
            if nargin == 7
                obj.id = varargin{1};
                obj.nodeTypeId = varargin{2};
                obj.layerId = varargin{3};
                obj.verticesX = varargin{4};
                obj.verticesY = varargin{5};
                obj.type = varargin{6};
                obj.description = varargin{7};
            elseif nargin == 6
                obj.id = SynthesisTemplate.instance().GetNextNodeRegionId();
                obj.nodeTypeId = varargin{1};
                obj.layerId = varargin{2};
                obj.verticesX = varargin{3};
                obj.verticesY = varargin{4};
                obj.type = varargin{5};
                obj.description = varargin{6};
            else
                obj.id = SynthesisTemplate.instance().GetNextNodeRegionId();
                obj.nodeTypeId = 0;
                obj.layerId = 0;
                obj.verticesX = 0;
                obj.verticesY = 0;
                obj.type = NodeRegion.UNDEFINED;
                obj.description = '';
            end
        end
        
        %% GenerateNodes Function
        % Generates the nodes within the node region and automatically adds
        % to system definition.
        %
        % GenerateNodes(system)
        %   system: the system within which to generate nodes
        function GenerateNodes(obj,system)
            synthTemp = SynthesisTemplate.instance();
            if obj.type==NodeRegion.POLYGON
                for i=1:length(synthTemp.city.cells)
                    cell = synthTemp.city.cells(i);
                    if obj.ContainsCell(cell)
                        obj.CreateNode(system,cell);
                    end
                end
            elseif obj.type==NodeRegion.POLYLINE
                for i=1:length(synthTemp.city.cells)
                    cell = synthTemp.city.cells(i);
                    for p=1:length(obj.verticesX)-1
                        x1 = obj.verticesX(p);
                        y1 = obj.verticesY(p);
                        x2 = obj.verticesX(p+1);
                        y2 = obj.verticesY(p+1);
                        if cell.IntersectsLine(x1,y1,x2,y2)
                            obj.CreateNode(system,cell);
                            break;
                        end
                    end
                end
            elseif obj.type==NodeRegion.POLYPOINT
                for i=1:length(synthTemp.city.cells)
                    cell = synthTemp.city.cells(i);
                    for p=1:length(obj.verticesX)
                        x = obj.verticesX(p);
                        y = obj.verticesY(p);
                        if cell.ContainsPoint(x,y);
                            obj.CreateNode(system,cell);
                            break;
                        end
                    end
                end
            end
        end
    end
    methods(Access=private)
        %% CreateNode Function
        % Creates a new node at a cell within a system.
        function CreateNode(obj,system,cell)
            synthTemp = SynthesisTemplate.instance();
            system.nodes(end+1) = Node(cell, ...
                synthTemp.city.layers([synthTemp.city.layers.id]==obj.layerId), ...
                synthTemp.nodeTypes([synthTemp.nodeTypes.id]==obj.nodeTypeId));
        end
    end
end