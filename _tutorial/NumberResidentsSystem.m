classdef NumberResidentsSystem < SystemBehavior
    methods
        function obj = NumberResidentsSystem()
            obj = obj@SystemBehavior('Number Residents', ...
                ['Counts the number of residents based on nodal area ' ...
                'and residentDensity attributes.'], ...
                '-','[0,inf)');
        end
        function val = EvaluateImpl(obj,system)
            val = obj.SumDensityNodeAttributes(system,'residentDensity');
        end
    end
end