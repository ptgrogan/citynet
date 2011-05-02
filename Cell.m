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
    end
end