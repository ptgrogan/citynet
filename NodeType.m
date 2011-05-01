%% NodeType Class Definition
% An instance of the NodeType class is used to define a classification of 
% nodes to be used in the synthesis template. Each NodeType is given a 
% name, rgbColor (values for red, green, and blue between 0 and 1 to define
% the display color in the rendering), and a set of NodeTypeAttribute objects.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NodeType < handle
    properties
        id;                     % unique identifier
        name;                   % name of node type, string
        description;            % description of the node type, string
        rgbColor;               % red-green-blue color, 3x1 double
        attributes;             % mutable object array of NodeTypeAttribute objects
    end
    methods
        %% NodeType Constructor
        % Create a new instance of a NodeType object and assign its id,
        % name, description, and rgbColor attributes
        %
        % obj = NodeType(id, name, description, rgbColor)
        %   id:             identifier of the node type (integer)
        %   name:           name of the node type (string)
        %   description:    description of the node type (string)
        %   rgbColor:       color to display (3x1 double)
        %
        % obj = NodeType(name, description, rgbColor)
        %   name:           name of the node type (string)
        %   description:    description of the node type (string)
        %   rgbColor:       color to display (3x1 double)
        % 
        % obj = NodeType()
        
        function obj = NodeType(varargin)
            if nargin==4
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
                obj.rgbColor = varargin{4};
            elseif nargin==3
                obj.id = SynthesisTemplate.instance().GetNextNodeTypeId();
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.rgbColor = varargin{3};
            else
                obj.id = SynthesisTemplate.instance().GetNextNodeTypeId();
                obj.name = ['Node Type ' num2str(obj.id)];
                obj.description = '';
                obj.rgbColor = [0 0 0];
            end
            obj.attributes = NodeTypeAttribute.empty();
        end
    end
end