%% SynthesisTemplate Class Definition
% The SynthesisTemplate is the class of the primary object used to maintain
% state in the synthesis template application. Its attributes include a
% System object which maintains state for cells and edges, and sets of both
% CellType objects and EdgeType objects.
%
% 31-March 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef SynthesisTemplate < Singleton
    properties
        city;               % mutable City object to contain state
        cellTypes;          % mutable object array of CellType objects
        nextCellTypeId;     % next available identifier for cell types
        nextCellTypeAttributeId; % next available identifier for cell type attributes
        nextCellId;         % next available identifier for cells
    end
    methods(Access=private)
        %% SynthesisTemplate Constructor
        % Creates a new instance of a SynthesisTemplate object with a new 
        % city. (Note: this constructor is private to support the 
        % Singleton design pattern, which allows only one active 
        % SynthesisTemplate at a time.)
        %
        % obj = SynthesisTemplate()
        
        function obj = SynthesisTemplate()
            obj.city = City();
            obj.cellTypes = CellType.empty();
            obj.nextCellTypeId = 1;
            obj.nextCellTypeAttributeId = 1;
            obj.nextCellId = 1;
        end
    end
    methods(Static)
        %% Concrete implementation
        % See Singleton superclass.
        function obj = instance()
            persistent uniqueInstance
            if isempty(uniqueInstance)
                obj = SynthesisTemplate();
                uniqueInstance = obj;
            else
                obj = uniqueInstance;
            end
        end
    end
    methods
        %% GetNextCellTypeId Function
        % Gets and increments the next cell type identifier.
        
        function out = GetNextCellTypeId(obj)
            out = obj.nextCellTypeId;
            obj.nextCellTypeId = obj.nextCellTypeId + 1;
        end
        
        %% GetNextCellTypeAttributeId Function
        % Gets and increments the next cell type identifier.
        
        function out = GetNextCellTypeAttributeId(obj)
            out = obj.nextCellTypeAttributeId;
            obj.nextCellTypeAttributeId = obj.nextCellTypeAttributeId + 1;
        end
        
        %% GetNextCellId Function
        % Gets and increments the next cell identifier.
        
        function out = GetNextCellId(obj)
            out = obj.nextCellId;
            obj.nextCellId = obj.nextCellId + 1;
        end
    end
end