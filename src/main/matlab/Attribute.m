%% Attribute Class Definition
% An instance of the Attribute class is used to describe an aspect 
% of a particular object. It serves as a superclass to both
% NodeTypeAttribute and EdgeTypeAttribute.
%
% 13-June 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef Attribute < handle
    properties
        name;           % name of attribute, string
        description;    % description of attribute, string
        units;          % units of attribute value, string
        bounds;         % allowable bounds on attribute value, string
        value;          % numerical value of attribute, double
    end
    methods
        %% Attribute Constructor
        % Create a new instance of an Attribute object.
        %
        % obj = Attribute(name, description, units, bounds, value)
        %   obj:            the new Attribute instance
        %   name:           name of the node type attribute (string)
        %   description:    description of the node type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        % 
        % obj = Attribute()
        %   obj:            the new Attribute instance
        
        function obj=Attribute(varargin)
            if nargin==5
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.units = varargin{3};
                obj.bounds = varargin{4};
                obj.value = varargin{5};
            else
                obj.name = 'New Attribute';
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
                obj.value = 0;
            end
        end
    end
end