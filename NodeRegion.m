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
        systemId;               % system id for assignment
        nodeTypeId;             % node type id for assignment
        layerId;                % layer id for assignment
    end
    methods
        %% NodeRegion Constructor
        % Instantiates a new NodeRegion with specified parameters.
        %
        % obj = NodeRegion(id, systemId, nodeTypeId, layerIds, verticesX,
        %           verticesY)
        %   id:         unique identifier for node region
        %   systemId:   system id for assignment
        %   edgeTypeId: edge type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %
        % obj = NodeRegion(systemId, nodeTypeId, layerIds, verticesX,
        %           verticesY)
        %   systemId:   system id for assignment
        %   edgeTypeId: edge type id for assignment
        %   layerIds:   array of layer ids for assignment
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %
        % obj = NodeRegion()
        function obj = NodeRegion(varargin)
            if nargin == 6
                obj.id = varargin{1};
                obj.systemId = varargin{2};
                obj.nodeTypeId = varargin{3};
                obj.layerId = varargin{4};
                obj.verticesX = varargin{5};
                obj.verticesY = varargin{6};
            elseif nargin == 5
                obj.id = SynthesisTemplate.GetNextNodeRegionId();
                obj.systemId = varargin{1};
                obj.nodeTypeId = varargin{2};
                obj.layerId = varargin{3};
                obj.verticesX = varargin{4};
                obj.verticesY = varargin{5};
            else
                obj.id = SynthesisTemplate.GetNextNodeRegionId();
                obj.systemId = 0;
                obj.nodeTypeId = 0;
                obj.layerId = 0;
                obj.verticesX = 0;
                obj.verticesY = 0;
            end
        end
        
        %% GenerateNodes Function
        % Generates the nodes within the node region and automatically adds
        % to system definition.
        function GenerateNodes(obj)
            synthTemp = SynthesisTemplate.instance();
            for i=1:length(synthTemp.city.cells)
                cell = synthTemp.city.cells(i);
                if obj.ContainsCell(cell)
                    synthTemp.city.systems{obj.systemId}.nodes(end+1) = Node(...
                        cell, synthTemp.city.layers([synthTemp.city.layers.id]==obj.layerId), ...
                        synthTemp.nodeTypes([synthTemp.nodeTypes.id]==obj.nodeTypeId));
                end
            end
        end
    end
end