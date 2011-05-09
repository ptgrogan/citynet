%% NodeRegion Class Definition
% A NodeRegion specifies a spatial area over which nodes should be
% generated. The NodeRegion class was created to be able to specify nodes without
% relying on cellular definitions.
%
% 7-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NodeRegion < AbstractRegion
    properties
        id;                     % unique identifier for node region
        nodeTypeId;             % node type id for assignment
        layerId;                % layer id for assignment
    end
    methods
        %% NodeRegion Constructor
        % Instantiates a new NodeRegion with specified parameters.
        %
        % obj = NodeRegion(id, nodeTypeId, layerIds, verticesX, verticesY)
        %   id:         unique identifier for node region
        %   nodeTypeId: node type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %
        % obj = NodeRegion(nodeTypeId, layerIds, verticesX, verticesY)
        %   nodeTypeId: node type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %
        % obj = NodeRegion()
        function obj = NodeRegion(varargin)
            if nargin == 5
                obj.id = varargin{1};
                obj.nodeTypeId = varargin{2};
                obj.layerId = varargin{3};
                obj.verticesX = varargin{4};
                obj.verticesY = varargin{5};
            elseif nargin == 4
                obj.id = SynthesisTemplate.instance().GetNextNodeRegionId();
                obj.nodeTypeId = varargin{1};
                obj.layerId = varargin{2};
                obj.verticesX = varargin{3};
                obj.verticesY = varargin{4};
            else
                obj.id = SynthesisTemplate.instance().GetNextNodeRegionId();
                obj.nodeTypeId = 0;
                obj.layerId = 0;
                obj.verticesX = 0;
                obj.verticesY = 0;
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
            for i=1:length(synthTemp.city.cells)
                cell = synthTemp.city.cells(i);
                if obj.ContainsCell(cell)
                    system.nodes(end+1) = Node(cell, ...
                        synthTemp.city.layers([synthTemp.city.layers.id]==obj.layerId), ...
                        synthTemp.nodeTypes([synthTemp.nodeTypes.id]==obj.nodeTypeId));
                end
            end
        end
    end
end