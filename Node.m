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
        cell;           % cell handle
        layer;          % layer handle
        type;           % node type handle
    end
    methods
        %% Node Constructor
        % Instantiates a new Node object with specified cell, layer, and 
        % node type.
        %
        % obj = Node(id, cell, layer, type)
        %   id:             unique identifier of node
        %   cell:           cell handle
        %   layer:          layer handle
        %   type:           node type handle
        %
        % obj = Node(cell, layer, type)
        %   cell:           cell handle
        %   layer:          layer handle
        %   type:           node type handle
        %
        % obj = Node()
        
        function obj=Node(varargin)
            if nargin==4
                obj.id = varargin{1};
                obj.cell = varargin{2};
                obj.layer = varargin{3};
                obj.type = varargin{4};
            elseif nargin==3
                obj.id = SynthesisTemplate.instance().GetNextNodeId();
                obj.cell = varargin{1};
                obj.layer = varargin{2};
                obj.type = varargin{3};
            else
                obj.id = SynthesisTemplate.instance().GetNextNodeId();
                obj.cell = [];
                obj.layer = [];
                obj.type = [];
            end
        end
    end
end