%% TransportationFixedExpense Class Definition
% The TransportationFixedExpense behavior counts the amount of energy used 
% within the transportation system by using the `Fixed_Expense` attribute.
%
% 31-August, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef TransportationFixedExpense < Behavior
    properties
        cellFixedExpense = containers.Map('KeyType','int32','ValueType','double');
    end
    methods
        %% TransportationFixedExpense Constructor
        % Instantiates a new TransportationFixedExpense object.
        % 
        % obj = TransportationFixedExpense()
        %   obj:            the new TransportationFixedExpense object
        function obj = TransportationFixedExpense()
            obj = obj@Behavior('Transportation Fixed Expense', ...
                ['Counts the cost of fixed expenses for the ' ...
                'transportation system.'], ...
                '$','[0,inf)');
        end
        %% PlotCellFixedExpense Function
        % Plots the cell fixed expense behavior in a new figure.
        % 
        % obj.PlotCellFixedExpense()
        function PlotCellFixedExpense(obj)
            figure
            title(['Transportation Cell Fixed Expense (' obj.units ')'])
            obj.PlotCellValueMap(obj.cellFixedExpense)
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
        %   obj:    the TransportationFixedExpense object handle
        function val = EvaluateImpl(obj)
            val = 0;
            clear obj.cellFixedExpense
            obj.cellFixedExpense = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems(s).name,'Transportation')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        baselineArea = node.GetNodeTypeAttributeValue('Baseline_Area');
                        baselineLength = node.GetNodeTypeAttributeValue('Baseline_Length');
                        fixedExpense = node.GetNodeTypeAttributeValue('Fixed_Expense');
                        fixedExpenseVal = 0;
                        if ~isempty(fixedExpense) && ~isempty(baselineArea)
                            fixedExpenseVal = node.cell.GetArea()*fixedExpense/(baselineArea/1e6);
                        elseif ~isempty(fixedExpense) && ~isempty(baselineLength)
                            fixedExpenseVal = node.cell.GetArea()*fixedExpense/(baselineLength/1e3).^2;
                        elseif ~isempty(fixedExpense)
                            fixedExpenseVal = fixedExpense;
                        end
                        if obj.cellFixedExpense.isKey(node.cell.id)
                            obj.cellFixedExpense(node.cell.id) = obj.cellFixedExpense(node.cell.id) + fixedExpenseVal;
                        else
                            obj.cellFixedExpense(node.cell.id) = fixedExpenseVal;
                        end
                        val = val + fixedExpenseVal;
                    end
                end
            end
        end
    end
end