classdef NumberResidentsCity < CityBehavior
    methods
        function obj = NumberResidentsCity()
            obj = obj@CityBehavior('Number Residents', ...
                ['Counts the number of residents based on nodal area ' ...
                'and residentDensity attributes.'], ...
                '-','[0,inf)');
        end
        function val = EvaluateImpl(obj,city)
            val = obj.SumDensityNodeAttributes(city,'residentDensity');
        end
    end
end