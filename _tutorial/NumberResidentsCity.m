%% NumberResidentsCity Class Definition
% The NumberResidentsCell behavior counts the number of residents in a city
% by using the `residentDensity` attribute.
%
% 14-June, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NumberResidentsCity < CityBehavior
    methods
        %% NumberResidentsCity Constructor
        % Instantiates a new NumberResidentsCity object.
        % 
        % obj = NumberResidentsCity()
        %   obj:            the new NumberResidentsCity object
        function obj = NumberResidentsCity()
            obj = obj@CityBehavior('Number Residents', ...
                ['Counts the number of residents based on nodal area ' ...
                'and residentDensity attributes.'], ...
                '-','[0,inf)');
        end
    end
    methods(Access=protected)
        %% EvaluateImpl Function
        % Evaluates the behavior for a specified city. Note: the
        % superclass Evaluate function should be used for evaluation during
        % execution.
        %
        % val = obj.EvaluateImpl(city)
        %   val:    the evaluated value
        %   obj:    the NumberResidentsCity object handle
        %   city:   the city in which this behavior is evaluated
        function val = EvaluateImpl(obj,city)
            val = obj.SumDensityNodeAttributes(city,'residentDensity');
        end
    end
end