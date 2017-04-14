%% RDF Burning Advanced Variables
% This m-file is used to define the RDF burning advanced variables used in
% the ThermalTreatment.m object
%
% 31-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef RDFBurningVariables
    properties
        nat_gas_input;              % The natural gas in m^3/tonne required to burn c/d-RDF
        electricity_input;          % The electricity in kWh/tonne required to burn c/d-RDF
        calorific_value;            % The calorific value in GJ/tonne of c-d/RDF
        hazardous_residues;         % The hazardous residues in tonne/tonne input generated from c/d-RDF burning
        non_hazardous_residues;     % The non-hazardous residues in tonne/tonne input generated from c/d-RDF burning
    end
    methods
        function obj = RDFBurningVariables()
            % Natural Gas Input per RDF type (in m3/tonne)
            obj.nat_gas_input.cRDF = 0;
            obj.nat_gas_input.dRDF = 0;
            
            % Electricity Input per RDF type (in kWh/tonne)
            % Ref: ETSU, 1992, "Production and Combustion of c-RDF for
            % On-Site Power Generation"
            obj.electricity_input.cRDF = 20;
            obj.electricity_input.dRDF = 20;
                        
            % Calorific Value per RDF type (in GJ/tonne)
            % Ref: ETSU, 1992, "Production and Combustion of c-RDF for
            % On-Site Power Generation"
            % These values are refered to in pg 396 of McDougall et al.
            obj.calorific_value.cRDF = 16;
            obj.calorific_value.dRDF = 18;
            
            % Hazardous residues generated per RDF type (in tonne/tonne
            % input)
            % NB. This includes fly ash, filter dust, and gas cleaning
            % residues
            % Ref: ETSU, 1992, "Production and Combustion of c-RDF for
            % On-Site Power Generation"
            obj.hazardous_residues.cRDF = 0.0138;
            obj.hazardous_residues.dRDF = 0.0138;
            
            % Non-hazardous residues generated per waste stream (in
            % tonne/tonne input)
            % NB. This arises from bottom-ash
            % Ref: ETSU, 1992, "Production and Combustion of c-RDF for
            % On-Site Power Generation"
            obj.non_hazardous_residues.cRDF = 0.086;
            obj.non_hazardous_residues.dRDF = 0.086;         
        end
    end
end