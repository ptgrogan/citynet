%% PPDF Burning Advanced Variables
% This m-file is used to define the PPDF burning advanced variables used in
% the ThermalTreatment.m object
%
% 31-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef PPDFBurningVariables
    properties
        nat_gas_input;              % The natural gas in m^3/tonne required to burn PPDF
        electricity_input;          % The electricity in kWh/tonne required to burn PPDF
        calorific_value;            % The calorific value in GJ/tonne of PPDF
        hazardous_residues;         % The hazardous residues in tonne/tonne input generated from PPDF burning
        non_hazardous_residues;     % The non-hazardous residues in tonne/tonne input generated from PPDF burning
    end
    methods
        function obj = PPDFBurningVariables()
            % Natural Gas Input per PPDF type (in m3/tonne)
            obj.nat_gas_input.paper = 0;
            obj.nat_gas_input.plastic = 0;
            
            % Electricity Input per PPDF type (in kWh/tonne)
            % Ref: ETSU, 1992, "Production and Combustion of c-RDF for
            % On-Site Power Generation"
            obj.electricity_input.paper = 20;
            obj.electricity_input.plastic = 20;
                        
            % Calorific Value per PPDF type (in GJ/tonne)
            % Ref: Barton, J., 1986, "The Application of Mechanical Sorting
            % Technology in Waste Reclamation: Options and Constraints"
            obj.calorific_value.paper = 10.5;
            obj.calorific_value.plastic = 28.5;
            
            % Hazardous residues generated per RDF type (in tonne/tonne
            % input)
            % NB. This includes fly ash, filter dust, and gas cleaning
            % residues
            % Ref: ETSU, 1992, "Production and Combustion of c-RDF for
            % On-Site Power Generation"
            obj.hazardous_residues.paper = 0.0138;
            obj.hazardous_residues.plastic = 0.0138;
            
            % Non-hazardous residues generated per waste stream (in
            % tonne/tonne input)
            % NB. This arises from bottom-ash
            % Ref: ETSU, 1992, "Production and Combustion of c-RDF for
            % On-Site Power Generation"
            obj.non_hazardous_residues.paper = 0.084;
            obj.non_hazardous_residues.plastic = 0.075;                        
        end
    end
end