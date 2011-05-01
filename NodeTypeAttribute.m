%% NodeTypeAttribute Class Definition
% An instance of the NodeTypeAttribute class is used to describe an aspect 
% of a particular CellType. Each NodeTypeAttribute is given a name and 
% numeric value.
%
% 31-March 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NodeTypeAttribute
    properties
        id;             % unique identifier of node type attribute, integer
        name;           % name of node type attribute, string
        description;    % description of node type attribute, string
        units;          % units of attribute value, string
        bounds;         % allowable bounds on attribute value, string
        value;          % numerical value of cell attribute, double
    end
    methods
        %% NodeTypeAttribute Constructor
        % Create a new instance of a NodeTypeAttribute object and assign 
        % its name and value attributes
        %        
        % obj = NodeTypeAttribute(id, name, description, units, bounds, value)
        %   id:             identifier of the node type attribute (integer)
        %   name:           name of the node type attribute (string)
        %   description:    description of the node type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        %
        % obj = NodeTypeAttribute(name, description, units, bounds, value)
        %   name:           name of the node type attribute (string)
        %   description:    description of the node type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        % 
        % obj = NodeTypeAttribute()
        
        function obj=NodeTypeAttribute(varargin)
            if nargin==6
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
                obj.units = varargin{4};
                obj.bounds = varargin{5};
                obj.value = varargin{6};
            elseif nargin==5
                obj.id = SynthesisTemplate.instance().GetNextNodeTypeAttributeId();
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.units = varargin{3};
                obj.bounds = varargin{4};
                obj.value = varargin{5};
            else
                obj.id = SynthesisTemplate.instance().GetNextNodeTypeAttributeId();
                obj.name = ['Node Type Attribute ' num2str(obj.id)];
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
                obj.value = 0;
            end
        end
    end
end