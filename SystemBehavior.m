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
        %% SystemBehavior Constructor
        % Creates a new instance of a SystemBehavior object.
        %
        % obj = SystemBehavior(name, description, units, bounds)
        %   obj:            the new SystemBehavior instance
        %   name:           name of the behavior (string)
        %   description:    description of the behavior (string)
        %   units:          units of behavior value (string)
        %   bounds:         allowable bounds on behavior value (string)
        % 
        % obj = SystemBehavior()
        %   obj:            the new SystemBehavior instance
        function obj = SystemBehavior(varargin)
            % pre-initialization: package superconstructor arguments
            if nargin==4
                args{1} = varargin{1};
                args{2} = varargin{2};
                args{3} = varargin{3};
                args{4} = varargin{4};
            else
                args{1} = 'New System Behavior';
                args{2} = '';
                args{3} = '-';
                args{4} = '[0,0]';
            end
            % object initialization: call superconstructor
            obj = obj@Behavior(args{:});
            % post-initialization: set class-specific values
            if nargin==4
            else
            end
        end
        
        %% Evaluate Function
        % Evaluates the behavior, updates the stored copy, and returns the
        % computed value.
        %
        % val = obj.Evaluate(system)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        %   system: the system in which to evaluate the behavior
        function val = Evaluate(obj,system)
            val = obj.EvaluateImpl(system);
            obj.value = val;
        end
    end
    methods(Abstract)
        %% EvaluateImpl Function
        % Implementation that evaluates the behavior and returns the value.
        %
        % val = obj.Evaluate(system)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        %   system: the system in which to evaluate the behavior
        val = EvaluateImpl(obj,system)
    end
    methods(Static,Access=protected)
        %% SumNodeAttributes Function
        % Supplied function that sums all node attributes of a particular
        % name within the system.
        %
        % val = SumNodeAttributes(system,attributeName)
        %   val:            the sum of all node attributes
        %   system:         the system in which to sum the attributes
        %   attributeName:  the attribute name for which to sum
        function val = SumNodeAttributes(system,attributeName)
            val = 0;
            for i=1:length(system.nodes)
                node = system.nodes(i);
                if sum(strcmp({node.type.attributes.name},attributeName))==1
                    val = val + node.type.attributes(strcmp({node.type.attributes.name},attributeName)).value;
                end
            end
        end
        
        %% SumDensityNodeAttributes Function
        % Supplied function that sums all node attributes multiplied by 
        % cell area of a particular name within the system.
        %
        % val = SumDensityNodeAttributes(system,attributeName)
        %   val:            the sum of all node attributes
        %   system:         the system in which to sum the attributes
        %   attributeName:  the attribute name for which to sum
        function val = SumDensityNodeAttributes(system,attributeName)
            val = 0;
            for i=1:length(system.nodes)
                node = system.nodes(i);
                if sum(strcmp({node.type.attributes.name},attributeName))==1
                    val = val + node.cell.GetArea()*node.type.attributes( ...
                        strcmp({node.type.attributes.name},attributeName)).value;
                end
            end
        end
    end
end