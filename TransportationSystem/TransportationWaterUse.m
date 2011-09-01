%% TransportationWaterUse Class Definition
% The TransportationWaterUse behavior counts the amount of water used 
% within the transportation system by using the `Water_Use` attribute.
%
% 31-August, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef TransportationWaterUse < Behavior
    properties
        cellWaterUse = containers.Map('KeyType','int32','ValueType','double');
    end
    methods
        %% TransportationWaterUse Constructor
        % Instantiates a new TransportationWaterUse object.
        % 
        % obj = TransportationWaterUse()
        %   obj:            the new TransportationWaterUse object
        function obj = TransportationWaterUse()
            obj = obj@Behavior('Transportation Water Use', ...
                ['Counts the daily amount of water used within the ' ...
                'transportation system.'], ...
                'm^3/day','[0,inf)');
        end
        %% PlotCellWaterUse Function
        % Plots the cell water use behavior in a new figure.
        % 
        % PlotCellWaterUse()
        function PlotCellWaterUse(obj)
            figure
            title(['Transportation Cell Water Use (' obj.units ')'])
            obj.PlotCellValueMap(obj.cellWaterUse)
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
        %   obj:    the TransportationWaterUse object handle
        function val = EvaluateImpl(obj)
            val = 0;
            clear obj.cellWaterUse
            obj.cellWaterUse = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems.name,'Transportation')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        baselineArea = node.GetNodeTypeAttributeValue('Baseline_Area');
                        baselineLength = node.GetNodeTypeAttributeValue('Baseline_Length');
                        waterUse = node.GetNodeTypeAttributeValue('Water_Use');
                        waterVal = 0;
                        if ~isempty(waterUse) && ~isempty(baselineArea)
                            waterVal = node.cell.GetArea()*waterUse/(baselineArea/1e6);
                        elseif ~isempty(waterUse) && ~isempty(baselineLength)
                            waterVal = node.cell.GetArea()*waterUse/(baselineLength/1e3).^2;
                        elseif ~isempty(waterUse)
                            waterVal = waterUse;
                        end
                        if obj.cellWaterUse.isKey(node.cell.id)
                            obj.cellWaterUse(node.cell.id) = obj.cellWaterUse(node.cell.id) + waterVal;
                        else
                            obj.cellWaterUse(node.cell.id) = waterVal;
                        end
                        val = val + waterVal;
                    end
                end
            end
        end
    end
end