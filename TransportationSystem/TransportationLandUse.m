%% TransportationLandUse Class Definition
% The TransportationLandUse behavior counts the amount of land used within
% the transportation system by using the `Land_Use` attribute.
%
% 24-August, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef TransportationLandUse < Behavior
    properties
        cellLandUse = containers.Map('KeyType','int32','ValueType','double');
    end
    methods
        %% NumberResidentsCity Constructor
        % Instantiates a new NumberResidentsCity object.
        % 
        % obj = NumberResidentsCity()
        %   obj:            the new NumberResidentsCity object
        function obj = TransportationLandUse()
            obj = obj@Behavior('Transportation Land Use', ...
                ['Counts the amount of land used within the ' ...
                'transportation system.'], ...
                'km^2','[0,inf)');
        end
        %% PlotCellLandUse Function
        % Plots the cell land use behavior in a new figure.
        % 
        % PlotCellLandUse()
        function PlotCellLandUse(obj)
            figure
            title('Transportation Cell Land Use')
            obj.PlotCellValueMap(obj.cellLandUse)
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
            clear obj.cellLandUse
            obj.cellLandUse = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems.name,'Transportation')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        baselineArea = node.GetNodeTypeAttributeValue('Baseline_Area');
                        baselineLength = node.GetNodeTypeAttributeValue('Baseline_Length');
                        landUse = node.GetNodeTypeAttributeValue('Land_Use');
                        landVal = 0;
                        if ~isempty(landUse) && ~isempty(baselineArea)
                            landVal = node.cell.GetArea()*landUse/baselineArea;
                        elseif ~isempty(landUse) && ~isempty(baselineLength)
                            landVal = node.cell.GetArea()*landUse/baselineLength.^2;
                        elseif ~isempty(landUse)
                            landVal = landUse/(1000^2); % convert m^2 to km^2
                        end
                        if obj.cellLandUse.isKey(node.cell.id)
                            obj.cellLandUse(node.cell.id) = obj.cellLandUse(node.cell.id) + landVal;
                        else
                            obj.cellLandUse(node.cell.id) = landVal;
                        end
                        val = val + landVal;
                    end
                end
            end
        end
    end
end