%% NumberResidentsCell Class Definition
% The NumberResidentsCell behavior counts the number of residents in a cell
% by using the `residentDensity` attribute.
%
% 14-June, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef NumberResidentsCell < Behavior
    properties
        cell;   % the cell handle in which to evaluate the behavior
    end
    methods
        %% NumberResidentsCell Constructor
        % Instantiates a new NumberResidentsCell object.
        % 
        % obj = NumberResidentsCell()
        %   obj:            the new NumberResidentsCell object
        function obj = NumberResidentsCell()
            obj = obj@Behavior('Number Residents', ...
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
        function val = EvaluateImpl(obj)
            val = 0;
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                for i=1:length(city.systems(s).nodes)
                    node = city.systems(s).nodes(i);
                    if node.cell==obj.cell
                        resDen = node.GetNodeTypeAttributeValue('residentDensity');
                        if ~isempty(resDen)
                            val = val + node.cell.GetArea()*resDen;
                        end
                    end
                end
            end
        end
    end
end