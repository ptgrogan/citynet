%% Node Class Definition
% Instances of the Node class are used to define contents of a particular
% city cell and layer within a system.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%

classdef Node < handle
    properties
        id;             % unique identifier of node
        cellId;         % unique identifier of cell
        layerId;        % unique identifier of layer
        typeId;         % unique identifier of node type
    end
    methods
        %% Node Constructor
        % Instantiates a new Node object with specified cell, layer, and 
        % node type.
        %
        % obj = Node(id, cellId, layerId, typeId)
        %   id:             unique identifier of node
        %   cellId:         unique identifier of cell
        %   layerId:        unique identifier of layer
        %   typeId:         unique idenfitier of node type
        %
        % obj = Node(cellId, layerId, typeId)
        %   cellId:         unique identifier of cell
        %   layerId:        unique identifier of layer
        %   typeId:         unique idenfitier of node type
        %
        % obj = Node()
        
        function obj=Node(varargin)
            if nargin==4
                obj.id = varargin{1};
                obj.cellId = varargin{2};
                obj.layerId = varargin{3};
                obj.typeId = varargin{4};
            elseif nargin==3
                obj.id = SynthesisTemplate.instance().GetNextNodeId();
                obj.cellId = varargin{1};
                obj.layerId = varargin{2};
                obj.typeId = varargin{3};
            else
                obj.id = SynthesisTemplate.instance().GetNextNodeId();
                obj.cellId = 0;
                obj.layerId = 0;
                obj.typeId = 0;
            end
        end
    end
end