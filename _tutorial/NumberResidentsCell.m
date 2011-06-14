%% NumberResidentsCell Class Definition
% The NumberResidentsCell behavior counts the number of residents in a cell
% by using the `residentDensity` attribute.
%
% 14-June, 2011
% Paul Grogan, ptgrogan@mit.edu

classdef NumberResidentsCell < CellBehavior
    methods
        %% NumberResidentsCell Constructor
        % Instantiates a new NumberResidentsCell object.
        % 
        % obj = NumberResidentsCell()
        %   obj:            the new NumberResidentsCell object
        function obj = NumberResidentsCell()
            obj = obj@CellBehavior('Number Residents', ...
                ['Counts the number of residents based on nodal area ' ...
                'and residentDensity attributes.'], ...
                '-','[0,inf)');
        end
    end
    methods(Access=protected)
        %% EvaluateImpl Function
        % Evaluates the behavior for a specified cell. Note: the
        % superclass Evaluate function should be used for evaluation during
        % execution.
        %
        % val = obj.EvaluateImpl(cell)
        %   val:    the evaluated value
        %   obj:    the NumberResidentsCell object handle
        %   cell:   the cell in which this behavior is evaluated
        function val = EvaluateImpl(obj,cell)
            val = obj.SumDensityNodeAttributes(cell,'residentDensity');
        end
    end
end