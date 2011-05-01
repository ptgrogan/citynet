%% EdgeType Class Definition
% An instance of the EdgeType class is used to define a classification of 
% edges to be used in the synthesis template. Each EdgeType is given a 
% name, rgbColor (values for red, green, and blue between 0 and 1 to define
% the display color in the rendering), and a set of EdgeTypeAttribute objects.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef EdgeType < handle
    properties
        id;                     % unique identifier
        name;                   % name of edge type, string
        description;            % description of the edge type, string
        rgbColor;               % red-green-blue color, 3x1 double
        attributes;             % mutable object array of EdgeTypeAttribute objects
    end
    methods
        %% EdgeType Constructor
        % Create a new instance of a EdgeType object and assign its id,
        % name, description, and rgbColor attributes
        %
        % obj = EdgeType(id, name, description, rgbColor)
        %   id:             identifier of the edge type (integer)
        %   name:           name of the edge type (string)
        %   description:    description of the edge type (string)
        %   rgbColor:       color to display (3x1 double)
        %
        % obj = EdgeType(name, description, rgbColor)
        %   name:           name of the edge type (string)
        %   description:    description of the edge type (string)
        %   rgbColor:       color to display (3x1 double)
        % 
        % obj = EdgeType()
        
        function obj = EdgeType(varargin)
            if nargin==4
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
                obj.rgbColor = varargin{4};
            elseif nargin==3
                obj.id = SynthesisTemplate.instance().GetNextEdgeTypeId();
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.rgbColor = varargin{3};
            else
                obj.id = SynthesisTemplate.instance().GetNextEdgeTypeId();
                obj.name = ['Edge Type ' num2str(obj.id)];
                obj.description = '';
                obj.rgbColor = [0 0 0];
            end
            obj.attributes = EdgeTypeAttribute.empty();
        end
    end
end