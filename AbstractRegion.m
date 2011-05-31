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
            try
                [ix iy] = polybool('intersection',obj.verticesX,obj.verticesY,verticesX,verticesY);
                out = polyarea(ix,iy)/polyarea(verticesX,verticesY) > SynthesisTemplate.instance().minIntersectionFraction;
            catch
                % if the user does not have the mapping toolbox installed,
                % the previous line will not execute... as a hack, let's
                % estimate the intersection area by sampling the cell
                % area... this requires that the verticesX and verticesY
                % provided to this function describe a rectangle
                num_samples = 16;
                [samplesX,samplesY] = meshgrid( ...
                    linspace(min(verticesX),max(verticesX),sqrt(num_samples)), ...
                    linspace(min(verticesY),max(verticesY),sqrt(num_samples)));
                samplesX = reshape(samplesX,num_samples,1);
                samplesY = reshape(samplesY,num_samples,1);
                intersectionFraction = sum(inpolygon(samplesX,samplesY,obj.verticesX,obj.verticesY))/num_samples;
                out = intersectionFraction > SynthesisTemplate.instance().minIntersectionFraction;
            end
        end
    end
end