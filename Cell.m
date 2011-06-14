%% Cell Class Definition
% A Cell object represents the unit of analysis. It is defined by a
% location (upper left-hand corner), and dimensions along the x- and
% y-axes.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef Cell < handle
    properties
        id;                 % unique indentifier, integer
        location;           % (x,y) location of upper left-hand corner, km
        dimensions;         % (width,height) of cell, km
    end
    methods
        %% Cell Constructor
        % Instantiates a new Cell object with specified location and 
        % dimensions.
        %
        % obj = Cell(id, location, dimensions)
        %   id:         unique identifier
        %   location:   (x,y) location of the upper left-hand corner (km)
        %   dimensions: (width,height) of the cell (km)
        %
        % obj = Cell(location, dimensions)
        %   location:   (x,y) location of the upper left-hand corner (km)
        %   dimensions: (width,height) of the cell (km)
        %
        % obj = Cell()
        function obj = Cell(varargin)
            if nargin==3
                obj.id = varargin{1};
                obj.location = varargin{2};
                obj.dimensions = varargin{3};
            elseif nargin==2
                obj.id = CityNet.instance().GetNextCellId();
                obj.location = varargin{1};
                obj.dimensions = varargin{2};
            else
                obj.id = CityNet.instance().GetNextCellId();
                obj.location = [0,0];
                obj.dimensions = [0,0];
            end
        end
        
        %% ContainsPoint Function
        % Returns whether this cell contains the point (coordX, coordY).
        % (Assumes the cell is rectangular.)
        %
        % out = ContainsPoint(coordX,coordY)
        %   coordX:     the x coordinate of the point to test
        %   coordY:     the y coordinate of the point to test
        %   out:        1 if this cell contains the point, 0 otherwise
        function out = ContainsPoint(obj,coordX,coordY)
            [Vx Vy] = obj.GetVertices();
            out = coordX>=min(Vx)&&coordX<=max(Vx)&&coordY>=min(Vy)&&coordY<=max(Vy);
        end
        
        %% IntersectsLine Function
        % Returns whether this cell intersects (or contains) a polyline.
        %
        % out = IntersectsLine(x1,y1,x2,y2)
        %   x1:         x-coordinate of line origin
        %   y1:         y-coordinate of line origin
        %   x2:         x-coordinate of line destination
        %   y2:         y-coordinate of line destination
        %   out:        1 if this cell contains the point, 0 otherwise
        function out = IntersectsLine(obj,x1,y1,x2,y2)
            out = 0;
            [Vx Vy] = obj.GetVertices();
            x3 = min(Vx);
            x4 = max(Vx);
            y3 = min(Vy);
            y4 = max(Vy);
            % y = (x-x1)*(y2-y1)/(x2-x1)+y1
            % x = (y-y1)*(x2-x1)/(y2-y1)+x1
            if obj.ContainsPoint(x1,y1) || obj.ContainsPoint(x2,y2)
                % special case: line contained in cell
                out = 1;
            elseif x1==x2 
                % special case: vertical line: x = C
                if (min(y1,y2) <= y3 && max(y1,y2) >= y3) || ...
                        (min(y1,y2) <= y4 && max(y1,y2) >= y4)
                    out = 1;
                end
            elseif y1==y2
                % special case: horizontal line: y = C
                if (min(x1,x2) <= x3 && max(x1,x2) >= x3) || ...
                        (min(x1,x2) <= x4 && max(x1,x2) >= x4)
                    out = 1;
                end
            else
                % general case: intersection point with wall within bounds
                intY1 = (x3-x1)*(y2-y1)/(x2-x1)+y1;
                intY2 = (x4-x1)*(y2-y1)/(x2-x1)+y1;
                intX1 = (y3-y1)*(x2-x1)/(y2-y1)+x1;
                intX2 = (y4-y1)*(x2-x1)/(y2-y1)+x1;
                if intY1 >= y3 && intY1 <= y4 && x3 >= min(x1,x2) && x3 <= max(x1,x2) || ...
                        intY2 >= y3 && intY2 <= y4 && x4 >= min(x1,x2) && x4 <= max(x1,x2) || ...
                        intX1 >= x3 && intX1 <= x4 && y3 >= min(y1,y2) && y3 <= max(y1,y2) || ...
                        intX2 >= x3 && intX2 <= x4 && y4 >= min(y1,y2) && y4 <= max(y1,y2)
                    out = 1;
                end
            end
        end
        
        %% GetVertices Function
        % Gets the x- and y-vertices of the cell in counter-clockwise
        % order.
        %
        % [Vx Vy] = GetVertices()
        %   Vx:     the x-coordinates of vertices
        %   Vy:     the y-coordinates of vertices
        function [Vx Vy] = GetVertices(obj)
            Vx = [obj.location(1) obj.location(1) obj.location(1)+obj.dimensions(1) obj.location(1)+obj.dimensions(1)];
            Vy = [obj.location(2) obj.location(2)+obj.dimensions(2) obj.location(2)+obj.dimensions(2) obj.location(2)];
        end
        
        %% GetArea Function
        % Returns the area covered by the cell.
        %
        % area = obj.GetArea()
        %   area:   the area covered by the cell
        %   obj:    the cell handle
        function area = GetArea(obj)
            area = obj.dimensions(1)*obj.dimensions(2);
        end
    end
end