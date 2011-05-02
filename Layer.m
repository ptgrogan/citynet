%% Layer Class Definition
% A Layer object represents a logical differentiation between objects that
% may exist at the same areal location.
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef Layer < handle
    properties
        id;                 % unique indentifier, integer
        name;               % layer name, string
        description;        % layer description, string
        displayHeight;      % layer display height, double
    end
    methods
        %% Layer Constructor
        % Instantiates a new Layer object with specified name and
        % description.
        %
        % obj = Layer(id, name, description)
        %   id:             unique identifier
        %   name:           layer name (string)
        %   description:    layer description (string)
        %   displayHeight:  layer display height (double)
        %
        % obj = Layer(name, description)
        %   name:           layer name (string)
        %   description:    layer description (string)
        %   displayHeight:  layer display height (double)
        %
        % obj = Layer()
        
        function obj = Layer(varargin)
            if nargin==4
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
                obj.displayHeight = varargin{4};
            elseif nargin==3
                obj.id = SynthesisTemplate.instance().GetNextLayerId();
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.displayHeight = varargin{3};
            else
                obj.id = SynthesisTemplate.instance().GetNextLayerId();
                obj.name = ['Layer ' num2str(obj.id)];
                obj.description = '';
                obj.displayHeight = obj.id;
            end
        end
    end
end