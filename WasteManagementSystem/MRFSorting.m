%% MRFSorting Class Definition
% The MRFSorting behavior calculates the useable recyclable materials
% available and extracted from the waste_to_MRF output generated by the
% WasteSorting.m object
% In addition, the amount of residues is calculated, along with the amount
% going to either thermal treatment or landfill (as set by the user)
%
% 17-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef MRFSorting < Behavior
    properties
        waste_to_MRF;               % the comingled waste sent to a materials recovery facility after the waste is sorted (function input)
        recyclables_extracted;      % the output of the MRF process. That is, useable recyclable materials ready to be sent to reprocessors
        recyclables_to_PPDF;        % the output of the MRF process sent to Paper and Plastic Derived Fuel
        residues_to_thermal;        % the amount of residues generated by the MRF process which are sent for thermal treatment
        residues_to_landfill;       % the amount of residues generated by the MRF process which are sent to landfill
    end
    methods
        %% MRFSorting Constructor
        % Instantiates a new MRFSorting object.
        % 
        % obj = MRFSorting()
        %   obj:                        the new MRFSorting object
        %   waste_to_MRF:               the comingled waste sent to a materials recovery facility after the waste is sorted
        %   recyclables_extracted:      the output of the MRF process. That is, useable recyclable materials ready to be sent to reprocessors
        %   recyclables_to_PPDF:        the output of the MRF process sent to Paper and Plastic Derived Fuel
        %   residues_to_thermal:        the amount of residues generated by the MRF process which are sent for thermal treatment
        %   residues_to_landfill:       the amount of residues generated by the MRF process which are sent to landfill
        
        function obj = MRFSorting()
            obj = obj@Behavior('Materials Recovery Facility', ...
                ['Calculates the outputs of the materials recovery process'], ...
                'tonnes','[0,inf)');
        end
    end
    methods(Access=protected)
        %% EvaluateImpl Function
        % Evaluates the behavior for a specified city. Note: the
        % superclass Evaluate function should be used for evaluation during
        % execution.
        %
        % val = obj.EvaluateImpl(city)
        %   val:    the evaluated value
        %   obj:    the TotalResidentialWaste object handle
        function val = EvaluateImpl(obj)      
            
            % Set city identifier
            city = CityNet.instance().city;
            
            % Search for waste system identifier       
            for i = 1:length(city.systems)
                if strcmp(city.systems(i).name,'Waste')==1
                    ind = i;
                    break
                end
            end      
            
            % Search for Materials Sorting Facility Identifier in Waste Layer
            for i = 1:length(city.systems(ind).nodes)
                if strcmp(city.systems(ind).nodes(i).type.name,'Waste Streams Sorting')
                    % Store node ID
                    ind2 = i;
                    break
                end
            end          
            
            % Set Materials Sorting Facility Identifier
            sorting = city.systems(ind).nodes(ind2);
            
            % Determine contamination rates based on if kerbside sorting is
            % performed or not
            % Note that this formulation assumes constant contamination
            % rates for all collection systems used
            if sorting.GetNodeTypeAttributeValue('wasteKerbsideSort') == 1
                MRFInputLostAsResidue = sorting.GetNodeTypeAttributeValue('sortedInputLostAsResidue');
            else
                MRFInputLostAsResidue = sorting.GetNodeTypeAttributeValue('unsortedInputLostAsResidue');
            end
            
            %% Calculate residues generated through MRF process and the amounts sent to thermal treatment and landfill
            
            % Paper
            paperresidues = MRFInputLostAsResidue*obj.waste_to_MRF.paper;
            obj.residues_to_thermal.paper = paperresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.paper = paperresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
            
            % Glass
            glassresidues = MRFInputLostAsResidue*obj.waste_to_MRF.glass;
            obj.residues_to_thermal.glass = glassresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.glass = glassresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
                        
            % Ferrous Metal
            femetalresidues = MRFInputLostAsResidue*obj.waste_to_MRF.fe_metal;
            obj.residues_to_thermal.fe_metal = femetalresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.fe_metal = femetalresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
                        
            % Non-ferrous Metal
            nonfemetalresidues = MRFInputLostAsResidue*obj.waste_to_MRF.nonfe_metal;
            obj.residues_to_thermal.nonfe_metal = nonfemetalresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.nonfe_metal = nonfemetalresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
            
            % Film Plastic
            filmplasticresidues = MRFInputLostAsResidue*obj.waste_to_MRF.filmplastic;
            obj.residues_to_thermal.filmplastic = filmplasticresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.filmplastic = filmplasticresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
            
            % Rigid Plastic
            rigidplasticresidues = MRFInputLostAsResidue*obj.waste_to_MRF.rigidplastic;
            obj.residues_to_thermal.rigidplastic = rigidplasticresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.rigidplastic = rigidplasticresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
            
            % Textiles
            textilesresidues = MRFInputLostAsResidue*obj.waste_to_MRF.textiles;
            obj.residues_to_thermal.textiles = textilesresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.textiles = textilesresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
            
            % Organics
            organicsresidues = MRFInputLostAsResidue*obj.waste_to_MRF.organics;
            obj.residues_to_thermal.organics = organicsresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.organics = organicsresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
            
            % Other
            otherresidues = MRFInputLostAsResidue*obj.waste_to_MRF.other;
            obj.residues_to_thermal.other = otherresidues*sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent');
            obj.residues_to_landfill.other = otherresidues*...
                (1-sorting.GetNodeTypeAttributeValue('mrfResidueIncinerationPercent'));
            
            %% Calculate recyclable and PPDF output from MRF process
            
            % Paper
            obj.recyclables_extracted.paper = (obj.waste_to_MRF.paper-paperresidues)*...
                sorting.GetNodeTypeAttributeValue('mrfRecyclingPercentPaper');
            obj.recyclables_to_PPDF.paper = (obj.waste_to_MRF.paper-paperresidues)*...
                (1-sorting.GetNodeTypeAttributeValue('mrfRecyclingPercentPaper'));
            
            % Glass
            obj.recyclables_extracted.glass = obj.waste_to_MRF.glass-glassresidues;
            
            % Ferrous Metal
            obj.recyclables_extracted.fe_metal = obj.waste_to_MRF.fe_metal-femetalresidues;
            
            % Non-Ferrous Metal
            obj.recyclables_extracted.nonfe_metal = obj.waste_to_MRF.nonfe_metal-nonfemetalresidues;
            
            % Film Plastic
            obj.recyclables_extracted.filmplastic = (obj.waste_to_MRF.filmplastic-filmplasticresidues)*...
                sorting.GetNodeTypeAttributeValue('mrfRecyclingPercentPlastic');
            obj.recyclables_to_PPDF.filmplastic = (obj.waste_to_MRF.filmplastic-filmplasticresidues)*...
                (1-sorting.GetNodeTypeAttributeValue('mrfRecyclingPercentPlastic'));
            
            % Rigid Plastic
            obj.recyclables_extracted.rigidplastic = (obj.waste_to_MRF.rigidplastic-rigidplasticresidues)*...
                sorting.GetNodeTypeAttributeValue('mrfRecyclingPercentPlastic');
            obj.recyclables_to_PPDF.rigidplastic = (obj.waste_to_MRF.rigidplastic-rigidplasticresidues)*...
                (1-sorting.GetNodeTypeAttributeValue('mrfRecyclingPercentPlastic'));
            
            % Textiles
            obj.recyclables_extracted.textiles = obj.waste_to_MRF.textiles-textilesresidues;
            
            % Organics
            obj.recyclables_extracted.organics = obj.waste_to_MRF.organics-organicsresidues;
            
            % Other
            obj.recyclables_extracted.other = obj.waste_to_MRF.other-otherresidues;
                        
            %% Assign Total Extracted Recycles to val
            val = sum([obj.recyclables_extracted.paper,obj.recyclables_extracted.glass,...
                obj.recyclables_extracted.fe_metal,obj.recyclables_extracted.nonfe_metal,...
                obj.recyclables_extracted.filmplastic,obj.recyclables_extracted.rigidplastic,...
                obj.recyclables_extracted.textiles,obj.recyclables_extracted.organics,...
                obj.recyclables_extracted.other])
            
        end
    end
end