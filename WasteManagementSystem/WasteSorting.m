%% WasteSorting Class Definition
% The WasteSorting behavior calculates the breakdown in destination for the
% total amount of collected waste in a city. Depending on how the waste is
% delivered to the waste management system, it can go to either a:
% - Materials recovery facility, where further sorting into recyclables and
% refuse derived fuel is performed
% - Directly to a recyclable material reprocessor
% - Another form of treatment, including sorting for refuse derived fuel,
% direct thermal treatment, or landfill.
% This final fraction is referred to as restwaste.
% The specific distribution of destination for this restwaste is specified
% by the user
%
% 12-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef WasteSorting < Behavior
    properties
        total_residential_waste;    % the total residential waste generated in the city (function input)
        total_commercial_waste;     % the total commercial waste generated in the city (function input)
        waste_to_MRF;               % the comingled waste sent to a materials recovery facility
        recyclables;                % the amount of collected waste able to be sent directly as recyclables
        total_restwaste;            % the total remaining waste
    end
    methods
        %% WasteSorting Constructor
        % Instantiates a new WasteSorting object.
        % 
        % obj = WasteSorting()
        %   obj:                        the new WasteSorting object
        %   total_residential_waste:    the total residential waste generated in the city
        %   total_commercial_waste:     the total commercial waste generated in the city
        %   waste_to_MRF:               the comingled waste sent to a materials recovery facility
        %   recyclables:                the amount of collected waste able to be sent directly as recyclables
        %   total_restwaste:            the total remaining waste

        function obj = WasteSorting()
            obj = obj@Behavior('Initial Breakdown in Waste Destination', ...
                ['Calculates the amount of waste transferred to  ' ...
                'other waste management processes.'], ...
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
            
            % For each building system node, identify residential node and
            % calculate corresponding delivered waste generated per node by stream.
            % Following this, sum all stream values
            
            city = CityNet.instance().city;
            
            % Search for waste system identifier       
            for i = 1:length(city.systems)
                if strcmp(city.systems(i).name,'Waste')==1
                    ind = i;
                    break
                end
            end      
            
            % Search for Waste Sorting Facility Identifier in Waste Layer
            for i = 1:length(city.systems(ind).nodes)
                if strcmp(city.systems(ind).nodes(i).type.name,'Waste Streams Sorting')
                    % Store node ID
                    ind2 = i;
                    break
                end
            end          
            
            sorting = city.systems(ind).nodes(ind2);
                       
            % Extract vector of ratios of residential waste by waste stream
            % Note that structure of ratio vector is [kerbside collection,
            % single material containers, mixed material containers] for
            % all streams with the exception of Paper and Organics.
            % In the paper stream, a fourth term represents the biowaste
            % bin component.
            % Conversely, the organics ratio vector contains only two
            % terms, the first being the biowaste fraction, and the second
            % being from single material containers.
            % It should be noted that the remaining proportion of each
            % waste stream (ie. 1-sum(terms in ratio vector)) i sassumed to
            % be comingled restwaste.
            paperratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioPaper'));
            glassratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioGlass'));
            femetalratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFeMetal'));
            nonfemetalratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioNonFeMetal'));
            filmplasticratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFilmPlastic'));
            rigidplasticratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioRigidPlastic'));
            textilesratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioTextiles'));
            organicsratio = str2num(sorting.GetNodeTypeAttributeValue('wasteCollectionRatioOrganics'));
            
            % Extract proportions of commercial waste transferred to
            % waste management system by waste stream
            % Note that for the paper stream, a vector of value is entered.
            % The first term corresponds to the proportion originating from
            % direct commercial collection, while the second term
            % corresponds to paper collected from commercial biowaste bins.
            % Additionally, any remaining waste is assumed to enter the
            % restwaste stream
            commercialpaperratio = str2num(sorting.GetNodeTypeAttributeValue('CommercialWasteCollectionRatioPaper'));
            commercialglassratio = sorting.GetNodeTypeAttributeValue('CommercialWasteCollectionRatioGlass');
            commercialfemetalratio = sorting.GetNodeTypeAttributeValue('CommercialWasteCollectionRatioFeMetal');
            commercialnonfemetalratio = sorting.GetNodeTypeAttributeValue('CommercialWasteCollectionRatioNonFeMetal');
            commercialfilmplasticratio = sorting.GetNodeTypeAttributeValue('CommercialWasteCollectionRatioFilmPlastic');
            commercialrigidplasticratio = sorting.GetNodeTypeAttributeValue('CommercialWasteCollectionRatioRigidPlastic');
            commercialtextilesratio = sorting.GetNodeTypeAttributeValue('CommercialWasteCollectionRatioTextiles');
            
            % Extract material recovery rates of delivered waste by stream
            % Note that all values here are scalars
            deliveredglassrate = sorting.GetNodeTypeAttributeValue('deliveredGlassRecoveryRate');
            deliveredfemetalrate = sorting.GetNodeTypeAttributeValue('deliveredFeMetalRecoveryRate');
            deliverednonfemetalrate = sorting.GetNodeTypeAttributeValue('deliveredNonFeMetalRecoveryRate');
            deliveredfilmplasticrate = sorting.GetNodeTypeAttributeValue('deliveredFilmPlasticRecoveryRate');
            deliveredrigidplasticrate = sorting.GetNodeTypeAttributeValue('deliveredRigidPlasticRecoveryRate');
            deliveredotherrate = sorting.GetNodeTypeAttributeValue('deliveredOtherRecoveryRate');         
            
            % Determine contamination rates based on if kerbside sorting is
            % performed or not
            % Note that this formulation assumes constant contamination
            % rates for all collection systems used
            if sorting.GetNodeTypeAttributeValue('wasteKerbsideSort') == 1
                OrganicContaminationRate = sorting.GetNodeTypeAttributeValue('sortedOrganicContaminationRate');
                OtherContaminationRate = sorting.GetNodeTypeAttributeValue('sortedOtherContaminationRate');
                InputLostAsResidue = sorting.GetNodeTypeAttributeValue('sortedInputLostAsResidue');
            else
                OrganicContaminationRate = sorting.GetNodeTypeAttributeValue('unsortedOrganicContaminationRate');
                OtherContaminationRate = sorting.GetNodeTypeAttributeValue('unsortedOtherContaminationRate');
                InputLostAsResidue = sorting.GetNodeTypeAttributeValue('unsortedInputLostAsResidue');
            end
            
            %% Calculate Materials Transferred to Materials Recovery
            % Facility by Waste Stream
            % Material transferred to Materials Recovery Facility =
            % Material collected from kerbside + material collected in
            % mixed materials containers + Collected commercial waste
            
            % Paper
            obj.waste_to_MRF.paper = (sum(obj.total_residential_waste.paper*paperratio([1,3]))+...
                commercialpaperratio(1)*obj.total_commercial_waste.paper)*...
                (1-OrganicContaminationRate-OtherContaminationRate);
            
            % Glass
            obj.waste_to_MRF.glass = (sum(obj.total_residential_waste.glass*glassratio([1,3]))+...
                commercialglassratio*obj.total_commercial_waste.glass)*...
                (1-OrganicContaminationRate-OtherContaminationRate);
            
            % Ferrous Metal
            obj.waste_to_MRF.fe_metal = (sum(obj.total_residential_waste.fe_metal*femetalratio([1,3]))+...
                commercialfemetalratio*obj.total_commercial_waste.fe_metal)*...
                (1-OrganicContaminationRate-OtherContaminationRate);
            
            % Non-Ferrous Metal
            obj.waste_to_MRF.nonfe_metal = (sum(obj.total_residential_waste.nonfe_metal*nonfemetalratio([1,3]))+...
                commercialnonfemetalratio*obj.total_commercial_waste.nonfe_metal)*...
                (1-OrganicContaminationRate-OtherContaminationRate);
            
            % Film Plastic
            obj.waste_to_MRF.filmplastic = (sum(obj.total_residential_waste.filmplastic*filmplasticratio([1,3]))+...
                commercialfilmplasticratio*obj.total_commercial_waste.filmplastic)*...
                (1-OrganicContaminationRate-OtherContaminationRate);
                
            % Rigid Plastic
            obj.waste_to_MRF.rigidplastic = (sum(obj.total_residential_waste.rigidplastic*rigidplasticratio([1,3]))+...
                commercialrigidplasticratio*obj.total_commercial_waste.rigidplastic)*...
                (1-OrganicContaminationRate-OtherContaminationRate);
            
            % Textiles
            obj.waste_to_MRF.textiles = (sum(obj.total_residential_waste.textiles*textilesratio([1,3]))+...
                commercialtextilesratio*obj.total_commercial_waste.textiles)*...
                (1-OrganicContaminationRate-OtherContaminationRate);
            
            % Organics
            totalTransferredResidentialWaste = sum([sum(obj.total_residential_waste.paper*paperratio([1,3])),...
                sum(obj.total_residential_waste.glass*glassratio([1,3]))...
                sum(obj.total_residential_waste.fe_metal*femetalratio([1,3])),...
                sum(obj.total_residential_waste.nonfe_metal*nonfemetalratio([1,3])),...
                sum(obj.total_residential_waste.filmplastic*filmplasticratio([1,3])),...
                sum(obj.total_residential_waste.rigidplastic*rigidplasticratio([1,3])),...
                sum(obj.total_residential_waste.textiles*textilesratio([1,3]))]);
                
            totalTransferredCommercialWaste = sum([commercialpaperratio(1)*obj.total_commercial_waste.paper,...
                commercialglassratio*obj.total_commercial_waste.glass,...
                commercialfemetalratio*obj.total_commercial_waste.fe_metal,...
                commercialnonfemetalratio*obj.total_commercial_waste.nonfe_metal,...
                commercialfilmplasticratio*obj.total_commercial_waste.filmplastic,...
                commercialrigidplasticratio*obj.total_commercial_waste.rigidplastic,...
                commercialtextilesratio*obj.total_commercial_waste.textiles]);
                
            obj.waste_to_MRF.organics = (totalTransferredResidentialWaste+...
                totalTransferredCommercialWaste)*OrganicContaminationRate;

            % Other
            obj.waste_to_MRF.other = (totalTransferredResidentialWaste+...
                totalTransferredCommercialWaste)*OtherContaminationRate;

            %% Calculate Materials able to be Sent Directly as Recyclables
            % Material transferred to recycled material reprocessors =
            % Material collected from single material containers + recovery
            % rate of delivered waste * amount of delivered waste
            
            
            
            %% Assign Outputs
            % Assign values to each handle in class
            % Divide by 1000 to put materials in units of tonnes
            val = TotalGlass+TotalFeMetal+TotalNonFeMetal+...
                TotalFilmPlastic+TotalRigidPlastic+TotalGarden+TotalOther;
            obj.glass = TotalGlass;
            obj.fe_metal = TotalFeMetal;
            obj.nonfe_metal = TotalNonFeMetal;
            obj.filmplastic = TotalFilmPlastic;
            obj.rigidplastic = TotalRigidPlastic;
            obj.garden = TotalGarden;
            obj.other = TotalOther;
            
        end
    end
end