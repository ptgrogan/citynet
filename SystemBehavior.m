%% SystemBehavior Class Definition
% A SystemBehavior is an object that assigns behaviors to specific systems.
% It inherits from the Behavior abstract superclass.
%
% 13-June 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef SystemBehavior < Behavior
    properties
        system;     % the system handle to which this behavior belongs
    end
    methods
        %% Behavior Constructor
        % Creates a new instance of a Behavior object.
        %
        % obj = SystemBehavior(system, name, description, units, bounds)
        %   obj:            the new SystemBehavior instance
        %   system:         the system handle to which this behavior belongs
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
            else
                args = {};
            end
            % object initialization: call superconstructor
            obj = obj@Behavior(args{:});
            % post-initialization: set class-specific values
            if nargin==5
                obj.system = varargin{1};
            else
                obj.id = CityNet.instance().GetNextSystemBehaviorId();
                obj.name = ['System Behavior ' num2str(obj.id)];
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
            end
        end
        
        %% Evaluate Function
        % Evaluates the behavior, updates the stored copy, and returns the
        % computed value.
        %
        % val = obj.Evaluate()
        %   obj:    the behavior to evaluate
        %   val:    the evaluated value
        function val = Evaluate(obj)
            val = obj.EvaluateImpl();
            obj.value = val;
        end
    end
    methods(Abstract)
        %% EvaluateImpl Function
        % Implementation that evaluates the behavior and returns the value.
        %
        % val = obj.Evaluate()
        %   obj:    the behavior to evaluate
        %   val:    the evaluated value
        val = EvaluateImpl(obj)
    end
    methods(Access=protected)
        %% SumNodeAttributes Function
        % Supplied function that sums all node attributes of a particular
        % name within the system.
        %
        % val = obj.SumNodeAttributes(attributeName)
        %   val:            the sum of all node attributes
        %   obj:            the system behavior handle
        %   attributeName:  the attribute name for which to sum
        function val = SumNodeAttributes(obj,attributeName)
            val = 0;
            for i=1:length(obj.system.nodes)
                node = obj.system.nodes(i);
                if sum(strcmp({node.type.attributes.name},attributeName))==1
                    val = val + node.type.attributes(strcmp({node.type.attributes.name},attributeName)).value;
                end
            end
        end
        
        %% SumDensityNodeAttributes Function
        % Supplied function that sums all node attributes multiplied by 
        % cell area of a particular name within the system.
        %
        % val = obj.SumDensityNodeAttributes(attributeName)
        %   val:            the sum of all node attributes
        %   obj:            the system behavior handle
        %   attributeName:  the attribute name for which to sum
        function val = SumDensityNodeAttributes(obj,attributeName)
            val = 0;
            for i=1:length(obj.system.nodes)
                node = obj.system.nodes(i);
                if sum(strcmp({node.type.attributes.name},attributeName))==1
                    val = val + node.cell.GetArea()*node.type.attributes( ...
                        strcmp({node.type.attributes.name},attributeName)).value;
                end
            end
        end
    end
end