%% NumberResidentsSystem Class Definition
% The NumberResidentsSystem behavior counts the number of residents in a 
% system by using the `residentDensity` attribute.
%
% 14-June, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NumberResidentsSystem < SystemBehavior
    methods
        %% NumberResidentsSystem Constructor
        % Instantiates a new NumberResidentsSystem object.
        % 
        % obj = NumberResidentsSystem()
        %   obj:            the new NumberResidentsSystem object
        function obj = NumberResidentsSystem()
            obj = obj@SystemBehavior('Number Residents', ...
                ['Counts the number of residents based on nodal area ' ...
                'and residentDensity attributes.'], ...
                '-','[0,inf)');
        end
    end
    methods(Access=protected)
        %% EvaluateImpl Function
        % Evaluates the behavior for a specified system. Note: the
        % superclass Evaluate function should be used for evaluation during
        % execution.
        %
        % val = obj.EvaluateImpl(city)
        %   val:    the evaluated value
        %   obj:    the NumberResidentsSystem object handle
        %   system:   the system in which this behavior is evaluated
        function val = EvaluateImpl(obj,system)
            val = obj.SumDensityNodeAttributes(system,'residentDensity');
        end
    end
end