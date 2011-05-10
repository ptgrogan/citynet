%% AbstractRegion Class Definition
% An AbstractRegion provides generic methods to the NodeRegion and
% EdgeRegion classes.
%
% 7-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef AbstractRegion < handle
    properties
        verticesX;              % list of vertices, x-coordinates
        verticesY;              % list of vertices, y-coordinates
    end
    methods
        
        %% ContainsCell Function
        % Determines whether a region contains a specific cell.
        %
        % out = ContainsCell(obj,cell)
        %   cell:   the cell to check
        function out = ContainsCell(obj,cell)
            [cVx cVy] = cell.GetVertices();
            out = obj.ContainsVertices(cVx,cVy);
        end
        
        %% ContainsVertices Function
        % Determines whether a region contains the area enclosed by a set
        % of vertices.
        %
        % out = ContainsVertices(obj,verticesX,verticesY)
        %   verticesX:  the x-coordinates of the vertices to check
        %   verticesY:  the y-coordinates of the vertices to check
        function out = ContainsVertices(obj,verticesX,verticesY)
            [ix iy] = polybool('intersection',obj.verticesX,obj.verticesY,verticesX,verticesY);
            out = sum(inpolygon(obj.verticesX,obj.verticesY,verticesX,verticesY))>0 || ...
                polyarea(ix,iy)/polyarea(verticesX,verticesY) > SynthesisTemplate.instance().minIntersectionFraction;
        end
    end
end