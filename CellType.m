%% CellType Class Definition
% An instance of the CellType class is used to define a classification of 
% cells to be used in the synthesis template. Each CellType is given a 
% name, rgbColor (values for red, green, and blue between 0 and 1 to define
% the display color in the rendering), and a set of CellAttribute objects.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef CellType < handle
    properties
        id;                     % unique identifier
        name;                   % name of cell type, string
        description;            % description of the cell type, string
        rgbColor;               % red-green-blue color, 3x1 double
        attributes;             % mutable object array of CellTypeAttribute objects
    end
    methods
        %% CellType Constructor
        % Create a new instance of a CellType object and assign its id,
        % name, description, and rgbColor attributes
        %
        % obj = CellType(id, name, description, rgbColor)
        %   id:             identifier of the cell type (integer)
        %   name:           name of the cell type (string)
        %   description:    description of the cell type (string)
        %   rgbColor:       color to display (3x1 double)
        %
        % obj = CellType(name, description, rgbColor)
        %   name:           name of the cell type (string)
        %   description:    description of the cell type (string)
        %   rgbColor:       color to display (3x1 double)
        % 
        % obj = CellType()
        
        function obj = CellType(varargin)
            if nargin==4
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
                obj.rgbColor = varargin{4};
            elseif nargin==3
                obj.id = SynthesisTemplate.instance().GetNextCellTypeId();
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.rgbColor = varargin{3};
            else
                obj.id = SynthesisTemplate.instance().GetNextCellTypeId();
                obj.name = 'New Cell Type';
                obj.description = '';
                obj.rgbColor = [0 0 0];
            end
            obj.attributes = CellTypeAttribute.empty();
        end
    end
end