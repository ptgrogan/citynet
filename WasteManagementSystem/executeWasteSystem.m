%% Execute Waste Management System
% Script for executing waste management system model
% This script runs the various functions related to CityNet's Waste
% Management System model. It can be used for testing and evaluation purposes.

% Spetember 7, 2011
% Sydney Do, sydneydo@mit.edu

%%

clear classes
close all
clc
addpath('..')

%% define synthesis properties
cityNet = CityNet.instance();
SpreadsheetReader.ReadTemplate('synthesisTutorial_completev3.xls');

cityNet.GenerateCells();
cityNet.GenerateNodes();
cityNet.GenerateEdges();

%% Execute Residential, Commercial, and Delivered waste demand functions
% Residential Waste
res = TotalResidentialWaste;
res.Evaluate;

% Commercial Waste
comm = TotalCommercialWaste;
comm.Evaluate;

% Delivered Waste
delivered = TotalDeliveredWaste;
delivered.Evaluate;

%% Execute Initial Waste Sorting Function
% All delivered inputs go to this function
sort = WasteSorting;

% Waste input from demand models
sort.total_residential_waste = res;
sort.total_commercial_waste = comm;
sort.total_delivered_waste = delivered;

% Evaluate WasteSorting function
sort.Evaluate;

%% Execute Materials Recovery Facility Sorting Function
mrf = MRFSorting;

% Preliminary sorted wastes assigned to the MRF facility are sent here
mrf.waste_to_MRF = sort.waste_to_MRF;

% Evaluate MRFSorting function
mrf.Evaluate;

%% Execute Refuse Derived Fuel Sorting Function
rdf = RDFSorting;
% Preliminary sorted wastes assigned to cRDF and dRDF sorting are sent here
rdf.restwaste_to_cRDF = sort.restwaste_to_cRDF;
rdf.restwaste_to_dRDF = sort.restwaste_to_dRDF;

% Evaluate RDFSorting function
rdf.Evaluate;

%% Execute Biological Treatment Function
bio = BiologicalTreatment;

% Input from preliminary wastes assigned to biological treatment
bio.biowaste = sort.biowaste;
bio.restwaste_to_biological = sort.restwaste_to_biological;

% Input fines assigned from RDF Sorting
bio.fines_to_biological = rdf.fines_to_biological;

% Evaluate BiologicalTreatment function
bio.Evaluate;

%% Execute Thermal Treatment Function
thermal = ThermalTreatment;

% Input from preliminary waste assigned to thermal treatment
thermal.restwaste_to_thermal = sort.restwaste_to_thermal;

% Input from Materials Recovery Facility
thermal.mrf_recyclables_to_PPDF = mrf.recyclables_to_PPDF;
thermal.mrf_residues_to_thermal = mrf.residues_to_thermal;

% Input from Refuse Derived Fuel Sorting
thermal.cRDF_extracted = rdf.cRDF_extracted;
thermal.dRDF_extracted = rdf.dRDF_extracted;

% Input from the biological treatment process
thermal.bio_residues_to_thermal = bio.residues_to_thermal;

% Evaluate ThermalTreatment function
thermal.Evaluate;

%% Aggregate materials sent to landfill
landfill = Landfill;

% Input from preliminary waste assigned to landfill
landfill.restwaste_to_landfill = sort.restwaste_to_landfill;
landfill.mrf_residues_to_landfill = mrf.residues_to_landfill;
landfill.rdf_screened_materials_to_landfill = rdf.screened_materials_to_landfill;
landfill.rdf_fines_to_landfill = rdf.fines_to_landfill;
landfill.rdf_residues_to_landfill = rdf.residues_to_landfill;
landfill.bio_residues_to_landfill = bio.residues_to_landfill;
landfill.thermal_non_hazardous_residues_to_landfill = thermal.non_hazardous_residues_to_landfill;
landfill.thermal_hazardous_residues_to_landfill = thermal.hazardous_residues_to_landfill;

% Evaluate Landfill function
landfill.Evaluate;

%% Aggregate materials recovered
materials = Materials;

% Materials recovered from the preliminary sorting process
materials.recyclables = sort.recyclables;

% Materials recovered from the Materials Recovery Facility
materials.mrf_recyclables_extracted = mrf.recyclables_extracted;

% Metals recovered from the Refuse Derived Fuel sorting process
materials.rdf_fe_metal_recovered = rdf.fe_metal_recovered;
materials.rdf_nonfe_metal_recovered = rdf.nonfe_metal_recovered;

% Materials recovered during the presorting process during biological
% treatment
materials.bio_presort_materials_recovered = bio.presort_materials_recovered;

% Compost extracted from the biological treatment process
materials.bio_compost_extracted = bio.compost_extracted;

% Biogas extracted from the biological treatment process
materials.bio_biogas_extracted = bio.biogas_extracted;

% Evaluate Materials function
materials.Evaluate;