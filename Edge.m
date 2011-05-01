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
        originNodeId;       % unique identifier of origin node
        destinationNodeId;  % unique identifier of destination node
        typeId;             % unique identifier of edge type
        directed;           % flag for whether edge is directed or not
    end
    methods
        %% Edge Constructor
        % Instantiates a new Edge object with specified origin,
        % destination, type, and directed-ness.
        %
        % obj = Edge(id, originNodeId, destinationNodeId, typeId, directed)
        %   id:                 unique identifier of edge
        %   originNodeId:       unique identifier of origin node
        %   destinationNodeId:  unique identifier of destination node
        %   typeId:             unique idenfitier of edge type
        %   directed:           flag (0 or 1) if edge is directed
        %
        % obj = Edge(originNodeId, destinationNodeId, edgeTypeId, directed)
        %   originNodeId:       unique identifier of origin node
        %   destinationNodeId:  unique identifier of destination node
        %   typeId:             unique idenfitier of edge type
        %   directed:           flag (0 or 1) if edge is directed
        %
        % obj = Edge()
        function obj=Edge(varargin)
            if nargin==5
                obj.id = varargin{1};
                obj.originNodeId = varargin{2};
                obj.destinationNodeId = varargin{3};
                obj.typeId = varargin{4};
                obj.directed = varargin{5};
            elseif nargin==4
                obj.id = SynthesisTemplate.instance().GetNextEdgeId();
                obj.originNodeId = varargin{1};
                obj.destinationNodeId = varargin{2};
                obj.typeId = varargin{3};
                obj.directed = varargin{4};
            else
                obj.id = SynthesisTemplate.instance().GetNextEdgeId();
                obj.originNodeId = 0;
                obj.destinationNodeId = 0;
                obj.typeId = 0;
                obj.directed = 0;
            end
        end
    end
end