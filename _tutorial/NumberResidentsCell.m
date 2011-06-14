classdef NumberResidentsCell < CellBehavior
    methods
        function obj = NumberResidentsCell()
            obj = obj@CellBehavior('Number Residents', ...
                ['Counts the number of residents based on nodal area ' ...
                'and residentDensity attributes.'], ...
                '-','[0,inf)');
        end
        function val = EvaluateImpl(obj,cell)
            val = obj.SumDensityNodeAttributes(cell,'residentDensity');
        end
    end
end