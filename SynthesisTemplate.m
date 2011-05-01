%% SynthesisTemplate Class Definition
% The SynthesisTemplate is the class of the primary object used to maintain
% state in the synthesis template application. Its attributes include a
% System object which maintains state for cells and edges, and sets of both
% NodeType objects and EdgeType objects.
%
% 31-March 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef SynthesisTemplate < Singleton
    properties
        city;               % mutable City object to contain state
        nodeTypes;          % mutable object array of NodeType objects
        nextNodeTypeId;     % next available identifier for node types
        nextNodeTypeAttributeId; % next available identifier for node type attributes
        nextCellId;         % next available identifier for cells
        nextLayerId;        % next available identifier for layers
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
            obj.nodeTypes = NodeType.empty();
            obj.nextNodeTypeId = 1;
            obj.nextNodeTypeAttributeId = 1;
            obj.nextCellId = 1;
            obj.nextLayerId = 1;
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
        %% GetNextNodeTypeId Function
        % Gets and increments the next node type identifier.
        function out = GetNextNodeTypeId(obj)
            out = obj.nextNodeTypeId;
            obj.nextNodeTypeId = obj.nextNodeTypeId + 1;
        end
        
        %% GetNextNodeTypeAttributeId Function
        % Gets and increments the next node type identifier.
        function out = GetNextNodeTypeAttributeId(obj)
            out = obj.nextNodeTypeAttributeId;
            obj.nextNodeTypeAttributeId = obj.nextNodeTypeAttributeId + 1;
        end
        
        %% GetNextCellId Function
        % Gets and increments the next cell identifier.
        function out = GetNextCellId(obj)
            out = obj.nextCellId;
            obj.nextCellId = obj.nextCellId + 1;
        end
        
        %% GetNextLayerId Function
        % Gets and increments the next cell identifier.
        function out = GetNextLayerId(obj)
            out = obj.nextLayerId;
            obj.nextLayerId = obj.nextLayerId + 1;
        end
    end
end