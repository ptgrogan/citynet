%% Behavior Class Definition
% A behavior is a dynamic property which is the product of analysis. Unlike
% an attribute, its value must be evaluated before being used.
%
% 13-June 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef Behavior < handle
    properties
        name;           % name of behavior, string
        description;    % description of behavior, string
        units;          % units of behavior value, string
        bounds;         % allowable bounds on behavior value, string
        value;          % lazy-loaded value of the behavior, double
    end
    methods
        %% Behavior Constructor
        % Creates a new instance of a Behavior object.
        %        
        % obj = Behavior(name, description, units, bounds)
        %   obj:            the new Behavior instance
        %   name:           name of the behavior (string)
        %   description:    description of the behavior (string)
        %   units:          units of behavior value (string)
        %   bounds:         allowable bounds on behavior value (string)
        % 
        % obj = Behavior()
        %   obj:            the new Behavior instance
        function obj = Behavior(varargin)
            if nargin==4
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.units = varargin{3};
                obj.bounds = varargin{4};
            else
                obj.name = 'New Behavior';
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
            end
        end
    end
    methods(Sealed)
        %% Evaluate Function
        % Evaluates the behavior, updates the stored copy, and returns the
        % computed value.
        %
        % val = obj.Evaluate(city)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        function val = Evaluate(obj)
            val = obj.EvaluateImpl();
            obj.value = val;
        end
    end
    methods(Abstract,Access=protected)
        %% EvaluateImpl Function
        % Implementation that evaluates the behavior and returns the value.
        %
        % val = obj.Evaluate(city)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        val = EvaluateImpl(obj)
    end
end