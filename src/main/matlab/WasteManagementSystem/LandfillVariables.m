%% Landfill Advanced Variables
% This m-file is used to define the landfill advanced variables used in
% the Landfill.m object
%
% 7-September, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef LandfillVariables
    properties
        gas_energy_potential;       % Landfill gas energy potential (in MJ/m^3)
        landfill_gas;               % The gas in m^3/tonne produced by each waste stream when landfilled
        leachate_solid_waste;       % The solid waste generated from leachate treatment in (tonnes/m^3 leachate treated)
        leachate_generation;        % The leachate in m^3/tonne produced by each waste stream when landfilled
        landfill_volume;            % The volume contribution (in m^3/tonne) of each waste stream to the landfill
    end
    methods
        function obj = LandfillVariables()
            % Landfill gas energy potential (MJ/m^3)
            obj.gas_energy_potential = 18;
            
            % Landfill Gas Generated per Waste Stream (in m^3/tonne)
            obj.landfill_gas.paper = 250;
            obj.landfill_gas.glass = 0;
            obj.landfill_gas.metal = 0;
            obj.landfill_gas.plastic = 0;
            obj.landfill_gas.textiles = 250;
            obj.landfill_gas.organics = 250;
            obj.landfill_gas.other = 0;
            obj.landfill_gas.compost = 100;
            obj.landfill_gas.bottom_ash = 0; 
            
            % Solid Waste generated from Leachate Treatment (in tonnes/m^3
            % leachate treated)
            obj.leachate_solid_waste = 0.015;
            
            % Leachate Generated per Waste Stream (in m^3/tonne)
            obj.leachate_generation.paper = 0.15;
            obj.leachate_generation.glass = 0.15;
            obj.leachate_generation.metal = 0.15;
            obj.leachate_generation.plastic = 0.15;
            obj.leachate_generation.textiles = 0.15;
            obj.leachate_generation.organics = 0.15;
            obj.leachate_generation.other = 0.15;
            obj.leachate_generation.compost = 0.15;
            obj.leachate_generation.bottom_ash = 0.15;
            obj.leachate_generation.hazardous = 0.15;
            
            % Landfill Volume Contribution per Waste Stream (m^3/tonne)
            obj.landfill_volume.paper = 1.05;
            obj.landfill_volume.glass = 0.51;
            obj.landfill_volume.fe_metal = 0.32;
            obj.landfill_volume.nonfe_metal = 0.93;
            obj.landfill_volume.filmplastic = 1.04;
            obj.landfill_volume.rigidplastic = 1.04;
            obj.landfill_volume.textiles = 1.43;
            obj.landfill_volume.organics = 1.11;
            obj.landfill_volume.other = 1.11;
            obj.landfill_volume.compost = 0.77;
            obj.landfill_volume.bottom_ash = 0.67;
            obj.landfill_volume.hazardous = 1.67;
            obj.landfill_volume.industrial_energy = 0.67;
            obj.landfill_volume.leachate_waste = 1.11;
        end
    end
end   