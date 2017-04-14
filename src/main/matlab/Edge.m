%% Edge Class Definition
% Instances of the Edge class are used to define connections between nodes
% within a system.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%

classdef Edge < handle
    properties
        id;                 % unique identifier of edge
        origin;             % origin node handle
        destination;        % destination node handle
        type;               % edge type handle
        directed;           % flag for whether edge is directed or not
    end
    methods
        %% Edge Constructor
        % Instantiates a new Edge object with specified origin,
        % destination, type, and directed-ness.
        %
        % obj = Edge(id, origin, destination, type, directed)
        %   id:                 unique identifier of edge
        %   origin:             origin node handle
        %   destination:        destination node handle
        %   type:             	edge type handle
        %   directed:           flag (0 or 1) if edge is directed
        %
        % obj = Edge(origin, destination, edgeType, directed)
        %   origin:             origin node handle
        %   destination:        destination node handle
        %   type:             	edge type handle
        %   directed:           flag (0 or 1) if edge is directed
        %
        % obj = Edge()
        function obj=Edge(varargin)
            if nargin==5
                obj.id = varargin{1};
                obj.origin = varargin{2};
                obj.destination = varargin{3};
                obj.type = varargin{4};
                obj.directed = varargin{5};
            elseif nargin==4
                obj.id = CityNet.instance().GetNextEdgeId();
                obj.origin = varargin{1};
                obj.destination = varargin{2};
                obj.type = varargin{3};
                obj.directed = varargin{4};
            else
                obj.id = CityNet.instance().GetNextEdgeId();
                obj.origin = [];
                obj.destination = [];
                obj.type = [];
                obj.directed = 0;
            end
        end
        
        %% GetEuclideanLength Function
        % Gets the length of an edge using Euclidean distance metric.
        %
        % out = obj.GetEuclideanLength()
        %   out:    the length
        %   obj:    the edge object handle
        function out = GetEuclideanLength(obj)
            out = sqrt(sum((obj.origin.cell.location-obj.destination.cell.location).^2));
        end
        
        %% GetEdgeTypeAttributeValue Function
        % Accesses the edte type attribute value of a specific name.
        %
        % val = obj.GetEdgeTypeAttributeValue(attributeName)
        %   val:            the edge type attribute value
        %   obj:            the edge object handle
        %   attributeName:  the name of the attribute, string
        function val = GetEdgeTypeAttributeValue(obj,attributeName)
            if ~isempty(obj.type.attributes) ...
                && sum(strcmpi({obj.type.attributes.name},attributeName))==1
                val = obj.type.attributes( ...
                    strcmpi({obj.type.attributes.name},attributeName)).value;
            else
                val = [];
            end
        end
    end
end