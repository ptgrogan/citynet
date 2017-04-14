%% Behavior Class Definition
% A behavior is a dynamic property which is the product of analysis. Unlike
% an attribute, its value must be evaluated before being used.
%
% 13-June 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef Behavior < handle
    properties
        name;           % name of behavior, string
        description;    % description of behavior, string
        units;          % units of behavior value, string
        bounds;         % allowable bounds on behavior value, string
        value;          % lazy-loaded value of the behavior, double
    end
    methods
        %% Behavior Constructor
        % Creates a new instance of a Behavior object.
        %        
        % obj = Behavior(name, description, units, bounds)
        %   obj:            the new Behavior instance
        %   name:           name of the behavior (string)
        %   description:    description of the behavior (string)
        %   units:          units of behavior value (string)
        %   bounds:         allowable bounds on behavior value (string)
        % 
        % obj = Behavior()
        %   obj:            the new Behavior instance
        function obj = Behavior(varargin)
            if nargin==4
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.units = varargin{3};
                obj.bounds = varargin{4};
            else
                obj.name = 'New Behavior';
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
            end
        end
    end
    methods(Static)
        %% PlotCellValueMap Function
        % Plots the values of a cell map (id->value) in the current figure.
        % Note: requires a containers.Map object for use. For example,
        % cellValueMap = ...
        %         containers.Map('KeyType','int32','ValueType','double');
        % cellValueMap(1) = 5.3; % sets cell id 1 to a value of 5.3
        %        
        % PlotCellValueMap(cellValueMap)
        %   cellValueMap:   the cell id-value map
        function PlotCellValueMap(cellValueMap)
            city = CityNet.instance().city;
            xlabel(['x (' city.distanceUnits ')'])
            ylabel(['y (' city.distanceUnits ')'])
            hold on
            if ischar(city.imagePath) && ~strcmp(city.imagePath,'')
                imagesc([min(city.imageVerticesX) max(city.imageVerticesX)], ...
                [min(city.imageVerticesY) max(city.imageVerticesY)], ...
                city.GetImage())
            end
            cmap = colormap(jet(128));
            for i=1:length(city.cells)
                [cVx cVy] = city.cells(i).GetVertices();
                cind = 1;
                if max(cell2mat(cellValueMap.values)) > 0 ...
                        && cellValueMap.isKey(city.cells(i).id)
                    cind = round(length(cmap)*cellValueMap(city.cells(i).id)/max(cell2mat(cellValueMap.values)));
                end
                if cind>0
                    patch(cVx, cVy, [1 1 1], 'FaceColor', cmap(cind,:));
                else
                    patch(cVx, cVy, [1 1 1], 'FaceColor', cmap(1,:));
                end
            end
            if max(cell2mat(cellValueMap.values)) > 0
                caxis([0 max(cell2mat(cellValueMap.values))])
            else
                caxis([0 1])
            end
            colorbar
            axis ij square tight
            hold off
        end
    end
    methods(Sealed)
        %% Evaluate Function
        % Evaluates the behavior, updates the stored copy, and returns the
        % computed value.
        %
        % val = obj.Evaluate(city)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        function val = Evaluate(obj)
            val = obj.EvaluateImpl();
            obj.value = val;
        end
    end
    methods(Abstract,Access=protected)
        %% EvaluateImpl Function
        % Implementation that evaluates the behavior and returns the value.
        %
        % val = obj.Evaluate(city)
        %   val:    the evaluated value
        %   obj:    the behavior to evaluate
        val = EvaluateImpl(obj)
    end
end