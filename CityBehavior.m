%% CityBehavior Class Definition
% A CityBehavior is an object that assigns behaviors to the city (i.e. 
% across several systems). It inherits from the Behavior abstract superclass.
%
% 14-June 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef CityBehavior < Behavior
    methods
        %% CityBehavior Constructor
        % Creates a new instance of a CityBehavior object.
        %
        % obj = CityBehavior(name, description, units, bounds)
        %   obj:            the new CityBehavior instance
        %   name:           name of the behavior (string)
        %   description:    description of the behavior (string)
        %   units:          units of behavior value (string)
        %   bounds:         allowable bounds on behavior value (string)
        % 
        % obj = CityBehavior()
        %   obj:            the new CityBehavior instance
        function obj = CityBehavior(varargin)
            % pre-initialization: package superconstructor arguments
            if nargin==4
                args{1} = varargin{1};
                args{2} = varargin{2};
                args{3} = varargin{3};
                args{4} = varargin{4};
            else
                args{1} = 'New City Behavior';
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
        % val = obj.Evaluate(city)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        %   city:   the city in which to evaluate the behavior
        function val = Evaluate(obj,city)
            val = obj.EvaluateImpl(city);
            obj.value = val;
        end
    end
    methods(Abstract)
        %% EvaluateImpl Function
        % Implementation that evaluates the behavior and returns the value.
        %
        % val = obj.Evaluate(city)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        %   city:   the city in which to evaluate the behavior
        val = EvaluateImpl(obj,city)
    end
    methods(Access=protected,Static)
        %% SumNodeAttributes Function
        % Supplied function that sums all node attributes of a particular
        % name within the city.
        %
        % val = SumNodeAttributes(city,attributeName)
        %   val:            the sum of all node attributes
        %   city:           the city in which to sum the attributes
        %   attributeName:  the attribute name for which to sum
        function val = SumNodeAttributes(city,attributeName)
            val = 0;
            for s=1:length(city.systems)
                for i=1:length(city.systems(s).nodes)
                    node = systems(s).nodes(i);
                    if sum(strcmp({node.type.attributes.name},attributeName))==1
                        val = val + node.type.attributes(strcmp({node.type.attributes.name},attributeName)).value;
                    end
                end
            end
        end
        
        %% SumDensityNodeAttributes Function
        % Supplied function that sums all node attributes multiplied by 
        % cell area of a particular name within the city.
        %
        % val = obj.SumDensityNodeAttributes(city,attributeName)
        %   val:            the sum of all node attributes
        %   city:           the city in which to sum the attributes
        %   attributeName:  the attribute name for which to sum
        function val = SumDensityNodeAttributes(city,attributeName)
            val = 0;
            for s=1:length(city.systems)
                for i=1:length(city.systems(s).nodes)
                    node = city.systems(s).nodes(i);
                    if sum(strcmp({node.type.attributes.name},attributeName))==1
                        val = val + node.cell.GetArea()*node.type.attributes( ...
                            strcmp({node.type.attributes.name},attributeName)).value;
                    end
                end
            end
        end
    end
end