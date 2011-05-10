%% WaterSystem Class Definition
% An instance of the System class specifically for the water system.
%
% 9-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef WaterSystem < System
    methods
        %% WaterSystem Constructor
        % Instantiates a new WaterSystem object with specified name and 
        % description.
        %
        % obj = WaterSystem(id, name, description)
        %   id:             unique identifier
        %   name:           system name (string)
        %   description:    system description (string)
        function obj = WaterSystem(varargin)
            obj = obj@System(varargin{:});
        end
    end
end