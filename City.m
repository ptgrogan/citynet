%% City Class Definition
% A City object contains the synthesis definition of each of its subsystems
% (e.g. water, energy, transportation, urban, waste) as well as other
% high-level definitions including cells, zones, etc.).
%
% 1-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef City < handle
    properties
        name;       % name of city, string
        latitude;   % latitude of city local coordinate frame origin (degrees)
        longitude;  % longitude of city local coordinate frame origin (degrees)
        rotation;   % rotation of city local coordinate frame from cardinal (degrees counterclockwise)
        imagePath;          % path to city image
        imageVerticesX;     % x-coordinate vertices
        imageVerticesY;     % y-coordinate vertices
        cells;      % mutable object array of Cell objects
        layers;     % mutable object array of Layer objects
        systems;    % mutable cell array of System objects
        % note: systems must be a cell array to enable polymorphism, i.e.
        % subclasses of System to be stored within the same data structure
    end
    properties(Access=private,Transient=true)
        image;              % lazy-loaded image
        imageX;             % lazy-loaded imageX
        imageMap;           % lazy-loaded imageMap
    end
    methods
        %% City Constructor
        % Instantiates a new City object with specified name.
        %
        % obj = City(name)
        %   name:       name of the city
        %
        % obj = City()
        
        function obj = City(varargin)
            if nargin==1
                obj.name = varargin{1};
            else
                obj.name = 'New City';
            end
            obj.latitude = 0;
            obj.longitude = 0;
            obj.rotation = 0;
            obj.imagePath = '';
            obj.imageVerticesX = [0 0 0 0];
            obj.imageVerticesY = [0 0 0 0];
            obj.imageX = [];
            obj.imageMap = [];
            
            obj.cells = Cell.empty();
            obj.layers = Layer.empty();
            obj.systems = {};
        end
        
        %% HasImage Method
        % Determines whether the city has an image to display.
        %
        %   out = HasImage()
        function out = HasImage(obj)
            out = ~isempty(obj.GetImage());
        end
        
        %% GetImage Method
        % Lazy-loading method to access city image.
        %
        %   out = GetImage()
        %       out: the image
        function out = GetImage(obj)
            if isempty(obj.imageX) && ischar(obj.imagePath)
                obj.image = imread(obj.imagePath);
            end
            out = obj.image;
        end
    end
end