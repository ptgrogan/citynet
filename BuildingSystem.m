%% BuildingSystem Class Definition
% An instance of the System class specifically for the building system.
%
% 9-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef BuildingSystem < System
    methods
        %% BuildingSystem Constructor
        % Instantiates a new BuildingSystem object with specified name and 
        % description.
        %
        % obj = BuildingSystem(id, name, description)
        %   id:             unique identifier
        %   name:           system name (string)
        %   description:    system description (string)
        function obj = BuildingSystem(varargin)
            obj = obj@System(varargin{:});
        end
    end
end