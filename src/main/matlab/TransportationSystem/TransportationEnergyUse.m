%% TransportationEnergyUse Class Definition
% The TransportationEnergyUse behavior counts the amount of energy used 
% within the transportation system by using the `Energy_Use` attribute.
%
% 31-August, 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef TransportationEnergyUse < Behavior
    properties
        cellEnergyUse = containers.Map('KeyType','int32','ValueType','double');
    end
    methods
        %% TransportationEnergyUse Constructor
        % Instantiates a new TransportationEnergyUse object.
        % 
        % obj = TransportationEnergyUse()
        %   obj:            the new TransportationEnergyUse object
        function obj = TransportationEnergyUse()
            obj = obj@Behavior('Transportation Energy Use', ...
                ['Counts the daily amount of energy used within the ' ...
                'transportation system.'], ...
                'kW-hr/day','[0,inf)');
        end
        %% PlotCellEnergyUse Function
        % Plots the cell energy use behavior in a new figure.
        % 
        % obj.PlotCellEnergyUse()
        function PlotCellEnergyUse(obj)
            figure
            title(['Transportation Cell Energy Use (' obj.units ')'])
            obj.PlotCellValueMap(obj.cellEnergyUse)
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
        %   obj:    the TransportationEnergyUse object handle
        function val = EvaluateImpl(obj)
            val = 0;
            clear obj.cellEnergyUse
            obj.cellEnergyUse = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems(s).name,'Transportation')
                    for i=1:length(city.systems(s).nodes)
                        node = city.systems(s).nodes(i);
                        baselineArea = node.GetNodeTypeAttributeValue('Baseline_Area');
                        baselineLength = node.GetNodeTypeAttributeValue('Baseline_Length');
                        energyUse = node.GetNodeTypeAttributeValue('Energy_Use');
                        energyVal = 0;
                        if ~isempty(energyUse) && ~isempty(baselineArea)
                            energyVal = node.cell.GetArea()*energyUse/(baselineArea/1e6);
                        elseif ~isempty(energyUse) && ~isempty(baselineLength)
                            energyVal = node.cell.GetArea()*energyUse/(baselineLength/1e3).^2;
                        elseif ~isempty(energyUse)
                            energyVal = energyUse;
                        end
                        if obj.cellEnergyUse.isKey(node.cell.id)
                            obj.cellEnergyUse(node.cell.id) = obj.cellEnergyUse(node.cell.id) + energyVal;
                        else
                            obj.cellEnergyUse(node.cell.id) = energyVal;
                        end
                        val = val + energyVal;
                    end
                end
            end
        end
    end
end