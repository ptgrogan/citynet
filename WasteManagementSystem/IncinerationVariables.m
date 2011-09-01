%% Incineration Advanced Variables
% This m-file is used to define the incineration advanced variables used in
% the ThermalTreatment.m object
%
% 31-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef IncinerationVariables
    properties
        nat_gas_input;              % The natural gas in m^3/tonne required to incinerate each waste stream (assumed to be 0.23m^3/tonne for all waste streams in pg 395 of McDougall et al.)
        electricity_input;          % The electricity in kWh/tonne required to incinerate each waste stream (assumed to be 70kWh/tonne for all waste streams in pg 395 of McDougall et al.)
        calorific_value;            % The calorific value in GJ/tonne of various waste streams
        hazardous_residues;         % The hazardous residues in tonne/tonne input generated from various waste streams during the incineration process
        non_hazardous_residues;     % The non-hazardous residues in tonne/tonne input generated from various waste streams during the incineration process
        fe_metal_recovery_percent;  % The fraction of ferrous metal recovered from bottom ash
    end
    methods
        function obj = IncinerationVariables()
            % Natural Gas Input per Waste Stream (in m3/tonne)
            obj.nat_gas_input.paper = 0.23;
            obj.nat_gas_input.glass = 0.23;
            obj.nat_gas_input.fe_metal = 0.23;
            obj.nat_gas_input.nonfe_metal = 0.23;
            obj.nat_gas_input.filmplastic = 0.23;
            obj.nat_gas_input.rigidplastic = 0.23;
            obj.nat_gas_input.textiles = 0.23;
            obj.nat_gas_input.organics = 0.23;
            obj.nat_gas_input.other = 0.23;
            obj.nat_gas_input.compost = 0.23;
            
            % Electricity Input per Waste Stream (in kWh/tonne)
            obj.electricity_input.paper = 70;
            obj.electricity_input.glass = 70;
            obj.electricity_input.fe_metal = 70;
            obj.electricity_input.nonfe_metal = 70;
            obj.electricity_input.filmplastic = 70;
            obj.electricity_input.rigidplastic = 70;
            obj.electricity_input.textiles = 70;
            obj.electricity_input.organics = 70;
            obj.electricity_input.other = 70;
            obj.electricity_input.compost = 70;
            
            % Calorific Value per Waste Stream (in GJ/tonne)
            % A negative sign indicates an effective negative contribution
            % to overall calorific value of the waste mix
            obj.calorific_value.paper = 10.5;
            obj.calorific_value.glass = -0.5;
            obj.calorific_value.fe_metal = -0.5;
            obj.calorific_value.nonfe_metal = -0.5;
            obj.calorific_value.filmplastic = 25;
            obj.calorific_value.rigidplastic = 28;
            obj.calorific_value.textiles = 13.5;
            obj.calorific_value.organics = 3.7;
            obj.calorific_value.other = 4;
            obj.calorific_value.compost = 7.4;
            
            % Hazardous residues generated per waste stream (in tonne/tonne
            % input)
            % NB. This includes fly ash, filter dust, and gas cleaning
            % residues (from pg 401 of McDougall et al.). IWM-2 assumes
            % that for each tonne of MSW incinerated, 20kg of filter dust
            % and 12kg of sludge residues from the gas-scrubbing system are
            % produced (their sum is 0.032tonne/tonne input)
            obj.hazardous_residues.paper = 0.032;
            obj.hazardous_residues.glass = 0.032;
            obj.hazardous_residues.fe_metal = 0.032;
            obj.hazardous_residues.nonfe_metal = 0.032;
            obj.hazardous_residues.filmplastic = 0.032;
            obj.hazardous_residues.rigidplastic = 0.032;
            obj.hazardous_residues.textiles = 0.032;
            obj.hazardous_residues.organics = 0.032;
            obj.hazardous_residues.other = 0.032;
            obj.hazardous_residues.compost = 0.032;
            
            % Non-hazardous residues generated per waste stream (in
            % tonne/tonne input)
            % NB. This arises from bottom-ash
            % Data is referenced from Barton, J. (1986) "The Application of
            % Mechanical Sorting Technology in Waste Reclamation: Options
            % and Constraints"
            obj.non_hazardous_residues.paper = 0.084;
            obj.non_hazardous_residues.glass = 0.9;
            obj.non_hazardous_residues.fe_metal = 0.85;
            obj.non_hazardous_residues.nonfe_metal = 0.9;
            obj.non_hazardous_residues.filmplastic = 0.09;
            obj.non_hazardous_residues.rigidplastic = 0.06;
            obj.non_hazardous_residues.textiles = 0.075;
            obj.non_hazardous_residues.organics = 0.077;
            obj.non_hazardous_residues.other = 0.42;
            obj.non_hazardous_residues.compost = 0.154;
            
            % Ferrous metal recovery percentage from bottom ash
            % From Stessel, R.I., 1996, "Recycling and resource recovery
            % engineering: Principles of waste processing"
            obj.fe_metal_recovery_percent = 0.9;
        end
    end
end   