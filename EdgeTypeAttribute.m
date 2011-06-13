%% EdgeTypeAttribute Class Definition
% An instance of the EdgeTypeAttribute class is used to describe an aspect 
% of a particular EdgeType. Each EdgeTypeAttribute is given a name and 
% numeric value.
%
% 31-March 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef EdgeTypeAttribute < Attribute
    properties
        id;             % unique identifier of edge type attribute, integer
    end
    methods
        %% EdgeTypeAttribute Constructor
        % Create a new instance of a EdgeTypeAttribute object and assign 
        % its name and value attributes
        %        
        % obj = EdgeTypeAttribute(id, name, description, units, bounds, value)
        %   obj:            the new EdgeTypeAttribute instance
        %   id:             identifier of the edge type attribute (integer)
        %   name:           name of the edge type attribute (string)
        %   description:    description of the edge type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        %
        % obj = EdgeTypeAttribute(name, description, units, bounds, value)
        %   obj:            the new EdgeTypeAttribute instance
        %   name:           name of the edge type attribute (string)
        %   description:    description of the edge type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        % 
        % obj = EdgeTypeAttribute()
        
        function obj=EdgeTypeAttribute(varargin)
            % pre-initialization: package superconstructor arguments
            if nargin==6
                args{1} = varargin{2};
                args{2} = varargin{3};
                args{3} = varargin{4};
                args{4} = varargin{5};
                args{5} = varargin{6};
            elseif nargin==5
                args{1} = varargin{1};
                args{2} = varargin{2};
                args{3} = varargin{3};
                args{4} = varargin{4};
                args{5} = varargin{5};
            else
                args = {};
            end
            % object initialization: call superconstructor
            obj = obj@Attribute(args{:});
            % post-initialization: set class-specific values
            if nargin==6
                obj.id = varargin{1};
            elseif nargin==5
                obj.id = CityNet.instance().GetNextEdgeTypeAttributeId();
            else
                obj.id = CityNet.instance().GetNextEdgeTypeAttributeId();
                obj.name = ['Edge Type Attribute ' num2str(obj.id)];
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
                obj.value = 0;
            end
        end
    end
end