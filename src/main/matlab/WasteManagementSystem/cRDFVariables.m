%% Coarse Refuse Derived Fuel Advanced Variables
% This m-file is used to define the cRDF advanced variables used in the
% RDFSorting.m object
%
% 17-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef cRDFVariables
    properties
        fuel;           % The percentage amount of fuel present in each waste stream type
        fe_metal;       % The percentage amount of ferrous metal present in each waste stream type
        nonfe_metal;    % The percentage amount of non ferrous metal present in each waste stream type
        fines;          % The percentage amount of fines present in each waste stream type
        residue;        % The percentage amount of residue present in each waste stream type
    end
    methods
        function obj = cRDFVariables()
            % Fuel Percentage
            obj.fuel.paper = 0.895;
            obj.fuel.glass = 0.287;
            obj.fuel.fe_metal = 0.076;
            obj.fuel.nonfe_metal = 0.315;
            obj.fuel.filmplastic = 0.836;
            obj.fuel.rigidplastic = 0.893;
            obj.fuel.textiles = 0.807;
            obj.fuel.organics = 0.427;
            obj.fuel.other = 0.365;
            
            % Ferrous Metal Percentage
            obj.fe_metal.paper = 0;
            obj.fe_metal.glass = 0.005;
            obj.fe_metal.fe_metal = 0.867;
            obj.fe_metal.nonfe_metal = 0;
            obj.fe_metal.filmplastic = 0;
            obj.fe_metal.rigidplastic = 0.003;
            obj.fe_metal.textiles = 0.008;
            obj.fe_metal.organics = 0.006;
            obj.fe_metal.other = 0.002;
            
            % Non-Ferrous Metal Percentage
            obj.nonfe_metal.paper = 0;
            obj.nonfe_metal.glass = 0;
            obj.nonfe_metal.fe_metal = 0;
            obj.nonfe_metal.nonfe_metal = 0.5;
            obj.nonfe_metal.filmplastic = 0;
            obj.nonfe_metal.rigidplastic = 0;
            obj.nonfe_metal.textiles = 0;
            obj.nonfe_metal.organics = 0;
            obj.nonfe_metal.other = 0;
            
            % Fines Percentage
            obj.fines.paper = 0.088;
            obj.fines.glass = 0.705;
            obj.fines.fe_metal = 0.04;
            obj.fines.nonfe_metal = 0.171;
            obj.fines.filmplastic = 0.045;
            obj.fines.rigidplastic = 0.088;
            obj.fines.textiles = 0.041;
            obj.fines.organics = 0.562;
            obj.fines.other = 0.615;
            
            % Residue Percentage
            obj.residue.paper = 0.017;
            obj.residue.glass = 0.003;
            obj.residue.fe_metal = 0.017;
            obj.residue.nonfe_metal = 0.014;
            obj.residue.filmplastic = 0.119;
            obj.residue.rigidplastic = 0.016;
            obj.residue.textiles = 0.144;
            obj.residue.organics = 0.005;
            obj.residue.other = 0.018;
            
        end
    end
end
            
    
    