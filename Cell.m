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
                obj.id = SynthesisTemplate.instance().GetNextCellId();
                obj.location = varargin{1};
                obj.dimensions = varargin{2};
            else
                obj.id = SynthesisTemplate.instance().GetNextCellId();
                obj.location = [0,0];
                obj.dimensions = [0,0];
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
    end
end