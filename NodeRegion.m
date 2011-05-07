classdef NodeRegion < handle
    properties
        id;                     % unique identifier for node region
        systemId;               % system id for assignment
        nodeTypeId;             % node type id for assignment
        layerId;                % layer id for assignment
        verticesX;              % list of vertices, x-coordinates
        verticesY;              % list of vertices, y-coordinates
        lowerBoundArea;         % lower bound on intersection area for node
    end
    methods
        function obj = NodeRegion(varargin)
            if nargin == 7
                obj.id = varargin{1};
                obj.systemId = varargin{2};
                obj.nodeTypeId = varargin{3};
                obj.layerId = varargin{4};
                obj.verticesX = varargin{5};
                obj.verticesY = varargin{6};
                obj.lowerBoundArea = varargin{7};
            elseif nargin == 6
                obj.id = SynthesisTemplate.GetNextNodeRegionId();
                obj.systemId = varargin{1};
                obj.nodeTypeId = varargin{2};
                obj.layerId = varargin{3};
                obj.verticesX = varargin{4};
                obj.verticesY = varargin{5};
                obj.lowerBoundArea = varargin{6};
            else
                obj.id = SynthesisTemplate.GetNextNodeRegionId();
                obj.systemId = 0;
                obj.nodeTypeId = 0;
                obj.layerId = 0;
                obj.verticesX = 0;
                obj.verticesY = 0;
                obj.lowerBoundArea = 0;
            end
        end
        function GenerateNodes(obj)
            synthTemp = SynthesisTemplate.instance();
            for i=1:length(synthTemp.city.cells)
                cell = synthTemp.city.cells(i);
                % find the coordinates of the intersection area between the
                % node region and the existing cell
                [cVx cVy] = cell.GetVertices();
                [ix iy] = polybool('intersection', obj.verticesX, obj.verticesY, cVx, cVy);
                % if vertex in cell area or intersected area is larger than
                % lower bound, add node
                if sum(inpolygon(obj.verticesX,obj.verticesY,cVx,cVy))>0 || ...
                    polyarea(ix,iy)/(cell.dimensions(1)*cell.dimensions(2)) > obj.lowerBoundArea
                    synthTemp.city.systems{obj.systemId}.nodes(end+1) = Node(...
                        cell, synthTemp.city.layers([synthTemp.city.layers.id]==obj.layerId), ...
                        synthTemp.nodeTypes([synthTemp.nodeTypes.id]==obj.nodeTypeId));
                end
            end
        end
    end
end