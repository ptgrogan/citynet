classdef AbstractRegion < handle
    properties
        verticesX;              % list of vertices, x-coordinates
        verticesY;              % list of vertices, y-coordinates
    end
    methods
        function out = ContainsCell(obj,cell)
            [cVx cVy] = cell.GetVertices();
            [ix iy] = polybool('intersection', obj.verticesX, obj.verticesY, cVx, cVy);
            out = sum(inpolygon(obj.verticesX,obj.verticesY,cVx,cVy))>0 || ...
                polyarea(ix,iy)/(cell.dimensions(1)*cell.dimensions(2)) > SynthesisTemplate.instance().minIntersectionArea;
        end
        function out = ContainsVertices(obj,verticesX, verticesY)
            out = sum(inpolygon(obj.verticesX,obj.verticesY,verticesX,verticesY))>0 || ...
                polyarea(ix,iy)/polyarea(verticesX,verticesY) > SynthesisTemplate.instance().minIntersectionArea;
        end
    end
end