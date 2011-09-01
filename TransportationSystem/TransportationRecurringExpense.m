%% TransportationRecurringExpense Class Definition
% The TransportationRecurringExpense behavior counts the recurring cost 
% for the transportation system by using the `Recurring_Expense` attribute.
%
% 31-August, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef TransportationRecurringExpense < Behavior
    properties
        cellRecurringExpense = containers.Map('KeyType','int32','ValueType','double');
    end
    methods
        %% TransportationRecurringExpense Constructor
        % Instantiates a new TransportationRecurringExpense object.
        % 
        % obj = TransportationRecurringExpense()
        %   obj:            the new TransportationRecurringExpense object
        function obj = TransportationRecurringExpense()
            obj = obj@Behavior('Transportation Recurring Expense', ...
                ['Counts the annual cost of recurring expenses for ' ...
                'the transportation system.'], ...
                '$/year','[0,inf)');
        end
        %% PlotCellRecurringExpense Function
        % Plots the cell recurring expense behavior in a new figure.
        % 
        % obj.PlotCellRecurringExpense()
        function PlotCellRecurringExpense(obj)
            figure
            title(['Transportation Cell Recurring Expense (' obj.units ')'])
            obj.PlotCellValueMap(obj.cellRecurringExpense)
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
            clear obj.cellRecurringExpense
            obj.cellRecurringExpense = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems.name,'Transportation')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        baselineArea = node.GetNodeTypeAttributeValue('Baseline_Area');
                        baselineLength = node.GetNodeTypeAttributeValue('Baseline_Length');
                        recurringExpense = node.GetNodeTypeAttributeValue('Recurring_Expense');
                        recurringExpenseVal = 0;
                        if ~isempty(recurringExpense) && ~isempty(baselineArea)
                            recurringExpenseVal = node.cell.GetArea()*recurringExpense/(baselineArea/1e6);
                        elseif ~isempty(recurringExpense) && ~isempty(baselineLength)
                            recurringExpenseVal = node.cell.GetArea()*recurringExpense/(baselineLength/1e3).^2;
                        elseif ~isempty(recurringExpense)
                            recurringExpenseVal = recurringExpense;
                        end
                        if obj.cellRecurringExpense.isKey(node.cell.id)
                            obj.cellRecurringExpense(node.cell.id) = obj.cellRecurringExpense(node.cell.id) + recurringExpenseVal;
                        else
                            obj.cellRecurringExpense(node.cell.id) = recurringExpenseVal;
                        end
                        val = val + recurringExpenseVal;
                    end
                end
            end
        end
    end
end