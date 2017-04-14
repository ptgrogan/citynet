%% NumberResidentsCity Class Definition
% The NumberResidentsCell behavior counts the number of residents in a city
% by using the `residentDensity` attribute.
%
% 14-June, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NumberResidentsCity < Behavior
    methods
        %% NumberResidentsCity Constructor
        % Instantiates a new NumberResidentsCity object.
        % 
        % obj = NumberResidentsCity()
        %   obj:            the new NumberResidentsCity object
        function obj = NumberResidentsCity()
            obj = obj@Behavior('Number Residents', ...
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
        function val = EvaluateImpl(obj)
            val = 0;
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                for i=1:length(city.systems(s).nodes)
                    resDen = city.systems(s).nodes(i).GetNodeTypeAttributeValue('residentDensity');
                    if ~isempty(resDen)
                        val = val + city.systems(s).nodes(i).cell.GetArea()*resDen;
                    end
                end
            end
        end
    end
end