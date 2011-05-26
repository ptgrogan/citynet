%% CellRegion Class Definition
% A CellRegion specifies a spatial area over which cells should be
% generated (meshed). There are several options to define the type and 
% density of cells generated within the region.
% 
% The CellRegion class was created to be able to specify cells without
% relying on tedious individual cell definitions.
%
% 25-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef CellRegion < AbstractRegion
    properties
        id;                 % unique identifier for cell region
        gridSize;           % 2x1 vector to define grid size (rows,cols)
    end
    methods
        %% CellRegion Constructor
        % Instantiates a new CellRegion with specified parameters.
        %
        % obj = CellRegion(id, verticesX, verticesY, gridSize, square)
        %   id:         unique identifier for cell region
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   gridSize:   2x1 vector to define grid size (rows,cols)
        %
        % obj = CellRegion(verticesX, verticesY, gridSize, square)
        %   verticesX:  array of x-coordinate vertices (counter-clockwise)
        %   verticesY:  array of y-coordinate vertices (counter-clockwise)
        %   gridSize:   2x1 vector to define grid size (rows,cols)
        %
        % obj = CellRegion()
        function obj = CellRegion(varargin)
            if nargin == 4
                obj.id = varargin{1};
                obj.verticesX = varargin{2};
                obj.verticesY = varargin{3};
                obj.gridSize = varargin{4};
            elseif nargin == 3
                obj.id = SynthesisTemplate.instance().GetNextCellRegionId();
                obj.verticesX = varargin{1};
                obj.verticesY = varargin{2};
                obj.gridSize = varargin{3};
            else
                obj.id = SynthesisTemplate.instance().GetNextCellRegionId();
                obj.gridSize = [0,0];
            end
        end
        
        %% GenerateCells Function
        % Generates the cells within the cell region and automatically adds
        % to system definition.
        %
        % GenerateCells(city)
        %   city: the city within which to generate cells
        function GenerateCells(obj,city)
            x = linspace(min(obj.verticesX),max(obj.verticesX),obj.gridSize(2)+1);
            y = linspace(min(obj.verticesY),max(obj.verticesY),obj.gridSize(1)+1);
            for j=1:length(y)-1
                for i=1:length(x)-1
                    city.cells(end+1) = Cell([x(i),y(j)],[x(i+1)-x(i),y(j+1)-y(j)]);
                end
            end
        end
    end
end