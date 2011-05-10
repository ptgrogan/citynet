%% EnergySystem Class Definition
% An instance of the System class specifically for the energy system.
%
% 9-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef EnergySystem < System
    methods
        %% EnergySystem Constructor
        % Instantiates a new EnergySystem object with specified name and 
        % description.
        %
        % obj = EnergySystem(id, name, description)
        %   id:             unique identifier
        %   name:           system name (string)
        %   description:    system description (string)
        function obj = EnergySystem(varargin)
            obj = obj@System(varargin{:});
        end
    end
end