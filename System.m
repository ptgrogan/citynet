%% System Class Definition
% An instance of the System class contains node and edge information on
% multiple layers.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef System < handle
    properties
        id;                 % unique indentifier, integer
        name;               % layer name, string
        description;        % layer description, string
        nodes;              % mutable object array of Node objects
        edges;              % mutable object array of Edge objects
    end
    methods
        %% System Constructor
        % Instantiates a new System object with specified name and
        % description.
        %
        % obj = System(id, name, description)
        %   id:             unique identifier
        %   name:           system name (string)
        %   description:    system description (string)
        %
        % obj = System(name, description)
        %   name:           system name (string)
        %   description:    system description (string)
        %
        % obj = System()
        
        function obj = System(varargin)
            if nargin==3
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
            elseif nargin==2
                obj.id = SynthesisTemplate.instance().GetNextSystemId();
                obj.name = varargin{1};
                obj.description = varargin{2};
            else
                obj.id = SynthesisTemplate.instance().GetNextSystemId();
                obj.name = ['System ' num2str(obj.id)];
                obj.description = '';
            end
            obj.nodes = Node.empty();
            obj.edges = Edge.empty();
        end
    end
end