%% EdgeTypeAttribute Class Definition
% An instance of the EdgeTypeAttribute class is used to describe an aspect 
% of a particular EdgeType. Each EdgeTypeAttribute is given a name and 
% numeric value.
%
% 31-March 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef EdgeTypeAttribute
    properties
        id;             % unique identifier of edge type attribute, integer
        name;           % name of edge type attribute, string
        description;    % description of edge type attribute, string
        units;          % units of attribute value, string
        bounds;         % allowable bounds on attribute value, string
        value;          % numerical value of cell attribute, double
    end
    methods
        %% EdgeTypeAttribute Constructor
        % Create a new instance of a EdgeTypeAttribute object and assign 
        % its name and value attributes
        %        
        % obj = EdgeTypeAttribute(id, name, description, units, bounds, value)
        %   id:             identifier of the edge type attribute (integer)
        %   name:           name of the edge type attribute (string)
        %   description:    description of the edge type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        %
        % obj = EdgeTypeAttribute(name, description, units, bounds, value)
        %   name:           name of the edge type attribute (string)
        %   description:    description of the edge type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        % 
        % obj = EdgeTypeAttribute()
        
        function obj=EdgeTypeAttribute(varargin)
            if nargin==6
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
                obj.units = varargin{4};
                obj.bounds = varargin{5};
                obj.value = varargin{6};
            elseif nargin==5
                obj.id = SynthesisTemplate.instance().GetNextEdgeTypeAttributeId();
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.units = varargin{3};
                obj.bounds = varargin{4};
                obj.value = varargin{5};
            else
                obj.id = SynthesisTemplate.instance().GetNextEdgeTypeAttributeId();
                obj.name = ['Edge Type Attribute ' num2str(obj.id)];
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
                obj.value = 0;
            end
        end
    end
end