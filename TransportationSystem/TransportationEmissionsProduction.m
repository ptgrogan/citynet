%% TransportationEmissionsProduction Class Definition
% The TransportationEmissionsProduction behavior counts the amount of 
% emissions produced by the transportation system by using the 
% `Emissions_Production` attribute.
%
% 31-August, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef TransportationEmissionsProduction < Behavior
    properties
        cellEmissionsProduction = containers.Map('KeyType','int32','ValueType','double');
    end
    methods
        %% TransportationEmissionsProduction Constructor
        % Instantiates a new TransportationEmissionsProduction object.
        % 
        % obj = TransportationEmissionsProduction()
        %   obj:       the new TransportationEmissionsProduction object
        function obj = TransportationEmissionsProduction()
            obj = obj@Behavior('Transportation Emissions Production', ...
                ['Counts the daily amount of emissions produced by the ' ...
                'transportation system.'], ...
                'kg/day','[0,inf)');
        end
        %% PlotCellWaterUse Function
        % Plots the cell emissions production behavior in a new figure.
        % 
        % PlotCellEmissionsProduction()
        function PlotCellEmissionsProduction(obj)
            figure
            title(['Transportation Cell Emissions Production (' obj.units ')'])
            obj.PlotCellValueMap(obj.cellEmissionsProduction)
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
        %   obj:    the TransportationEmissionsProduction object handle
        function val = EvaluateImpl(obj)
            val = 0;
            clear obj.cellEmissionsProduction
            obj.cellEmissionsProduction = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems.name,'Transportation')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        baselineArea = node.GetNodeTypeAttributeValue('Baseline_Area');
                        baselineLength = node.GetNodeTypeAttributeValue('Baseline_Length');
                        emissionsProduction = node.GetNodeTypeAttributeValue('Emissions_Production');
                        emissionsVal = 0;
                        if ~isempty(emissionsProduction) && ~isempty(baselineArea)
                            emissionsVal = node.cell.GetArea()*emissionsProduction/(baselineArea/1e6);
                        elseif ~isempty(emissionsProduction) && ~isempty(baselineLength)
                            emissionsVal = node.cell.GetArea()*emissionsProduction/(baselineLength/1e3).^2;
                        elseif ~isempty(emissionsProduction)
                            emissionsVal = emissionsProduction;
                        end
                        if obj.cellEmissionsProduction.isKey(node.cell.id)
                            obj.cellEmissionsProduction(node.cell.id) = obj.cellEmissionsProduction(node.cell.id) + emissionsVal;
                        else
                            obj.cellEmissionsProduction(node.cell.id) = emissionsVal;
                        end
                        val = val + emissionsVal;
                    end
                end
            end
        end
    end
end