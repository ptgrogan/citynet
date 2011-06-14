%% CellBehavior Class Definition
% A CellBehavior is an object that assigns behaviors to cells. It inherits 
% from the Behavior abstract superclass.
%
% 14-June 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef CellBehavior < Behavior
    methods
        %% CellBehavior Constructor
        % Creates a new instance of a CellBehavior object.
        %
        % obj = CellBehavior(name, description, units, bounds)
        %   obj:            the new CellBehavior instance
        %   name:           name of the behavior (string)
        %   description:    description of the behavior (string)
        %   units:          units of behavior value (string)
        %   bounds:         allowable bounds on behavior value (string)
        % 
        % obj = CellBehavior()
        %   obj:            the new CellBehavior instance
        function obj = CellBehavior(varargin)
            % pre-initialization: package superconstructor arguments
            if nargin==4
                args{1} = varargin{1};
                args{2} = varargin{2};
                args{3} = varargin{3};
                args{4} = varargin{4};
            else
                args{1} = 'New Cell Behavior';
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
        % val = obj.Evaluate(cell)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        %   cell:   the cell in which to evaluate the behavior
        function val = Evaluate(obj,cell)
            val = obj.EvaluateImpl(cell);
            obj.value = val;
        end
    end
    methods(Abstract)
        %% EvaluateImpl Function
        % Implementation that evaluates the behavior and returns the value.
        %
        % val = obj.Evaluate(cell)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        %   cell:   the cell in which to evaluate the behavior
        val = EvaluateImpl(obj,cell)
    end
    methods(Access=protected,Static)
        %% SumNodeAttributes Function
        % Supplied function that gets a node attribute of a particular
        % name within the cell.
        %
        % val = obj.SumNodeAttributes(cell,attributeName)
        %   val:            the sum of all node attributes
        %   cell:           the system behavior handle
        %   attributeName:  the attribute name for which to sum
        function val = SumNodeAttributes(cell,attributeName)
            val = 0;
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                for i=1:length(city.systems(s).nodes)
                    node = city.systems(s).nodes(i);
                    if node.cell==cell && ...
                            sum(strcmp({node.type.attributes.name},attributeName))==1
                        val = val + node.type.attributes(strcmp({node.type.attributes.name},attributeName)).value;
                    end
                end
            end
        end
        
        %% SumDensityNodeAttributes Function
        % Supplied function that sums all node attributes multiplied by 
        % cell area of a particular name.
        %
        % val = obj.SumDensityNodeAttributes(cell,attributeName)
        %   val:            the sum of all node attributes
        %   obj:            the system behavior handle
        %   attributeName:  the attribute name for which to sum
        function val = SumDensityNodeAttributes(cell,attributeName)
            val = 0;
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                for i=1:length(city.systems(s).nodes)
                    node = city.systems(s).nodes(i);
                    if node.cell==cell && ...
                            sum(strcmp({node.type.attributes.name},attributeName))==1
                        val = val + node.cell.GetArea()*node.type.attributes( ...
                            strcmp({node.type.attributes.name},attributeName)).value;
                    end
                end
            end
        end
    end
end