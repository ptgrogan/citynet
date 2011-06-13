%% SystemBehavior Class Definition
% A SystemBehavior is an object that assigns behaviors to specific systems.
% It inherits from the Behavior abstract superclass.
%
% 13-June 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef SystemBehavior < Behavior
    properties
        id;             % unique identifier of behavior, integer
    end
    methods
        %% Behavior Constructor
        % Creates a new instance of a Behavior object.
        %        
        % obj = SystemBehavior(id, name, description, units, bounds)
        %   obj:            the new SystemBehavior instance
        %   id:             identifier of the system behavior (integer)
        %   name:           name of the behavior (string)
        %   description:    description of the behavior (string)
        %   units:          units of behavior value (string)
        %   bounds:         allowable bounds on behavior value (string)
        %
        % obj = SystemBehavior(name, description, units, bounds)
        %   obj:            the new SystemBehavior instance
        %   name:           name of the system behavior (string)
        %   description:    description of the behavior (string)
        %   units:          units of behavior value (string)
        %   bounds:         allowable bounds on behavior value (string)
        % 
        % obj = SystemBehavior()
        %   obj:            the new SystemBehavior instance
        function obj = SystemBehavior(varargin)
            % pre-initialization: package superconstructor arguments
            if nargin==5
                args{1} = varargin{2};
                args{2} = varargin{3};
                args{3} = varargin{4};
                args{4} = varargin{5};
            elseif nargin==4
                args{1} = varargin{1};
                args{2} = varargin{2};
                args{3} = varargin{3};
                args{4} = varargin{4};
            else
                args = {};
            end
            % object initialization: call superconstructor
            obj = obj@Behavior(args{:});
            % post-initialization: set class-specific values
            if nargin==5
                obj.id = varargin{1};
            elseif nargin==4
                obj.id = CityNet.instance().GetNextSystemBehaviorId();
            else
                obj.id = CityNet.instance().GetNextSystemBehaviorId();
                obj.name = ['System Behavior ' num2str(obj.id)];
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
            end
        end
    end
    methods(Abstract)
        %% Evaluate Function
        % Evaluates the behavior and returns the computed value.
        %
        % val = obj.Evaluate()
        %   obj:    the behavior to evaluate
        %   val:    the evaluated value
        val = Evaluate(obj)
    end
end