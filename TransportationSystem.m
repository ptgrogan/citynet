%% TransportationSystem Class Definition
% An instance of the System class specifically for the Transporatation
% components. Includes the base properties of the System class, though the
% shortest path algorithm accesses the speed attribute of edges for
% computation.
%
% 2-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef TransportationSystem < System
    methods
        %% TransportationSystem Constructor
        % Instantiates a new TransportationSystem object with specified 
        % name and description.
        %
        % obj = TransportationSystem(id, name, description)
        %   id:             unique identifier
        %   name:           system name (string)
        %   description:    system description (string)
        function obj = TransportationSystem(varargin)
            obj = obj@System(varargin{:});
        end
        
        %% GetShortestPath Function
        % Gets the path (set of edges) corresponding to the the shortest
        % path between an origin and destination cell IDs. Uses Djikstra's
        % algorithm with Euclidean distances and assumed speeds for edge 
        % weights.
        %
        % path = GetShortestPath(obj,originCellId,destinationCellId)
        %   originId:       origin node identifier
        %   destinationId:  destination node identifier
        function path = GetShortestPath(obj,originId,destinationId)
            lengths = zeros(length(obj.edges),1);
            for i=1:length(obj.edges)
                speed = obj.edges(i).type.attributes(strcmp([obj.edges(i).type.attributes.name],'Speed')).value;
                lengths(i) = sqrt(sum((obj.edges(i).origin.cell.location-obj.edges(i).destination.cell.location).^2))/speed;
            end
            path = obj.GetShortestPathWithEdgeLengths(originId,destinationId,lengths);
        end
        
        %% GetPathDuration Function
        % Gets the duration (minutes) of a path (set of edges). Uses 
        % Euclidean distances for edge lengths.
        function out = GetPathDuration(obj,path)
            out = 0;
            for i=1:length(path)
                edge = obj.edges([obj.edges.id]==path(i));
                speed = edge.type.attributes(strcmp([edge.type.attributes.name],'Speed')).value;
                out = out + sqrt(sum((edge.origin.cell.location-edge.destination.cell.location).^2))/speed*60;
            end
        end
    end
end