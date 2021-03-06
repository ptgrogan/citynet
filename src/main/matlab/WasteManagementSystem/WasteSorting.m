%% WasteSorting Class Definition
% The WasteSorting behavior calculates the breakdown in destination for the
% total amount of collected waste in a city. Depending on how the waste is
% delivered to the waste management system, it can go to either a:
% - Materials recovery facility, where further sorting into recyclables and
% refuse derived fuel is performed
% - Directly to a recyclable material reprocessor
% - Another form of treatment, including sorting for refuse derived fuel,
% direct thermal treatment, or landfill.
% - Biological treatment - note that only organic waste (ie. biowaste) can
% be sent to this treatment process
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
        total_delivered_waste;      % the total waste in the city delivered directly to the waste management system by residents (function input)
        waste_to_MRF;               % the comingled waste sent to a materials recovery facility
        recyclables;                % the amount of collected waste able to be sent directly as recyclables
        biowaste;                   % the total amount of biowaste generated. This can either be treated biologically or landfilled
        restwaste_to_cRDF;          % the fraction of the total remaining waste going to cRDF sorting
        restwaste_to_dRDF;          % the fraction of the total remaining waste going to dRDF sorting
        restwaste_to_biological;    % the fraction of the total remaining waste going to biological treatment
        restwaste_to_thermal;       % the fraction of the total remaining waste going to thermal treatment
        restwaste_to_landfill;      % the fraction of the total remaining waste going to landfill
    end
    methods
        %% WasteSorting Constructor
        % Instantiates a new WasteSorting object.
        % 
        % obj = WasteSorting()
        %   obj:                        the new WasteSorting object
        %   total_residential_waste:    the total residential waste generated in the city
        %   total_commercial_waste:     the total commercial waste generated in the city
        %   total_delivered_waste:      the total waste in the city delivered directly to the waste management system by residents (function input)
        %   waste_to_MRF:               the comingled waste sent to a materials recovery facility
        %   recyclables:                the amount of collected waste able to be sent directly as recyclables
        %   biowaste:                   the total amount of biowaste generated. This can either be treated biologically, thermally, or landfilled
        %   restwaste_to_cRDF:          the fraction of the total remaining waste going to cRDF sorting
        %   restwaste_to_dRDF:          the fraction of the total remaining waste going to dRDF sorting
        %   restwaste_to_biological:    the fraction of the total remaining waste going to biological treatment
        %   restwaste_to_thermal:       the fraction of the total remaining waste going to thermal treatment
        %   restwaste_to_landfill:      the fraction of the total remaining waste going to landfill

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
            
            % Set city identifier
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
            paperratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioPaperKerbside'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioPaperSingleMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioPaperMixedMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioPaperBiowaste')];
            glassratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioGlassKerbside'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioGlassSingleMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioGlassMixedMaterials')];
            femetalratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFeMetalKerbside'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFeMetalSingleMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFeMetalMixedMaterials')];
            nonfemetalratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioNonFeMetalKerbside'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioNonFeMetalSingleMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioNonFeMetalMixedMaterials')];
            filmplasticratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFilmPlasticKerbside'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFilmPlasticSingleMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioFilmPlasticMixedMaterials')];
            rigidplasticratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioRigidPlasticKerbside'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioRigidPlasticSingleMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioRigidPlasticMixedMaterials')];
            textilesratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioTextilesKerbside'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioTextilesSingleMaterials'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioTextilesMixedMaterials')];
            organicsratio = [sorting.GetNodeTypeAttributeValue('wasteCollectionRatioOrganicsBiowaste'),...
                sorting.GetNodeTypeAttributeValue('wasteCollectionRatioOrganicsSingleMaterials')];
            
            % Extract proportions of commercial waste transferred to
            % waste management system by waste stream
            % Note that for the paper stream, a vector of value is entered.
            % The first term corresponds to the proportion originating from
            % direct commercial collection, while the second term
            % corresponds to paper collected from commercial biowaste bins.
            % Additionally, any remaining waste is assumed to enter the
            % restwaste stream
            commercialpaperratio = [sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioPaperDirect'),...
                sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioPaperBiowaste')];
            commercialglassratio = sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioGlass');
            commercialfemetalratio = sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioFeMetal');
            commercialnonfemetalratio = sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioNonFeMetal');
            commercialfilmplasticratio = sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioFilmPlastic');
            commercialrigidplasticratio = sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioRigidPlastic');
            commercialtextilesratio = sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioTextiles');
            commercialorganicsratio = sorting.GetNodeTypeAttributeValue('commercialWasteCollectionRatioOrganics');
            
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
            else
                OrganicContaminationRate = sorting.GetNodeTypeAttributeValue('unsortedOrganicContaminationRate');
                OtherContaminationRate = sorting.GetNodeTypeAttributeValue('unsortedOtherContaminationRate');
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
            % Material collected from single material containers +
            % Delivered waste recovery rate * Delivered waste
            
            % Paper
            % Note that paper is not part of the delivered waste stream
            obj.recyclables.paper = obj.total_residential_waste.paper*paperratio(2);
            
            % Glass
            obj.recyclables.glass = obj.total_residential_waste.glass*glassratio(2)+...
                obj.total_delivered_waste.glass*deliveredglassrate;
                
            % Ferrous Metal
            obj.recyclables.fe_metal = obj.total_residential_waste.fe_metal*femetalratio(2)+...
                obj.total_delivered_waste.fe_metal*deliveredfemetalrate;
            
            % Non-Ferrous Metal
            obj.recyclables.nonfe_metal = obj.total_residential_waste.nonfe_metal*nonfemetalratio(2)+...
                obj.total_delivered_waste.nonfe_metal*deliverednonfemetalrate;
            
            % Film Plastic Metal
            obj.recyclables.filmplastic = obj.total_residential_waste.filmplastic*filmplasticratio(2)+...
                obj.total_delivered_waste.filmplastic*deliveredfilmplasticrate;
            
            % Rigid Plastic Metal
            obj.recyclables.rigidplastic = obj.total_residential_waste.rigidplastic*rigidplasticratio(2)+...
                obj.total_delivered_waste.rigidplastic*deliveredrigidplasticrate;
            
            % Textiles
            % Note that textiles are not part of the delivered waste stream
            obj.recyclables.textiles = obj.total_residential_waste.textiles*textilesratio(2);

            %% Calculate Total Biowaste by Stream
            % Note that biowaste is derived only from residential and
            % commercial biowaste bins. Hence the only form of waste is 
            % paper, film and rigid plastics (from plastic contamination) 
            % and organic material
            % Because these make up differing parts of the biowaste input,
            % the calculation of the contribution of each stream is
            % different
            % Note that here, the residential and commercial biowaste bin
            % plastic contamination rates are assumed to be the same
            
            % Paper
            % This contribution comes from the paper content of the biowaste 
            % bins (minus film and rigid plastic contamination)
            obj.biowaste.paper = (obj.total_residential_waste.paper*paperratio(4)+...
                obj.total_commercial_waste.paper*commercialpaperratio(2))*...
                (1-sorting.GetNodeTypeAttributeValue('biowasteFilmPlasticContamination')-...
                sorting.GetNodeTypeAttributeValue('biowasteRigidPlasticContamination'));
            
            % Film Plastic
            % This contribution comes from the film plastic contribution to
            % the total biowaste bin content
            obj.biowaste.filmplastic = ((obj.total_residential_waste.paper*paperratio(4)+...
                obj.total_residential_waste.organics*organicsratio(1))+...
                (obj.total_commercial_waste.paper*paperratio(2)+...
                obj.total_commercial_waste.organics*commercialorganicsratio))*...
                sorting.GetNodeTypeAttributeValue('biowasteFilmPlasticContamination');
                      
            % Rigid Plastic
            % This contribution comes from the rigid plastic contribution to
            % the total biowaste bin content
            obj.biowaste.rigidplastic = ((obj.total_residential_waste.paper*paperratio(4)+...
                obj.total_residential_waste.organics*organicsratio(1))+...
                (obj.total_commercial_waste.paper*paperratio(2)+...
                obj.total_commercial_waste.organics*commercialorganicsratio))*...
                sorting.GetNodeTypeAttributeValue('biowasteRigidPlasticContamination');
            
            % Organics
            % This contribution comes from the organic content of the 
            % biowaste bins (minus film and rigid plastic contamination) as
            % well as garden waste directly delivered to the waste
            % management system by residents, and organic material found in
            % single material containers
            obj.biowaste.organics = (obj.total_residential_waste.organics*organicsratio(1)+...
                obj.total_commercial_waste.organics*commercialorganicsratio)*...
                (1-sorting.GetNodeTypeAttributeValue('biowasteFilmPlasticContamination')-...
                sorting.GetNodeTypeAttributeValue('biowasteRigidPlasticContamination'))+...
                obj.total_delivered_waste.garden+...
                obj.total_residential_waste.organics*organicsratio(2);
            
            %% Calculate Total Restwaste by Stream, as well as breakdown of restwaste by destination from sorting facility
            % Total Restwaste = Total waste input - (waste to MRF +
            % recyclables + biowaste)
            %
            % For the case of restwaste to thermal and landfill, the
            % contribution of delivered waste residues is also added for
            % the glass, fe_metal, nonfe_metal, filmplastic, rigidplastic,
            % and other waste streams
            %
            %   restwaste_to_cRDF:              the fraction of the total remaining waste going to cRDF sorting
            %   restwaste_to_dRDF:              the fraction of the total remaining waste going to dRDF sorting
            %   restwaste_to_biological:        the fraction of the total remaining waste going to biological treatment
            %   restwaste_to_thermal:           the fraction of the total remaining waste going to thermal treatment
            %   restwaste_to_landfill:          the fraction of the total remaining waste going to landfill
                        
            % Extract restwaste destination ratio values
            % Vector format is [cRDF dRDF biological treatment, thermal
            % treatment]. The remainder is sent to landfill
            restwastedistribution = [sorting.GetNodeTypeAttributeValue('restwasteDestinationRatiocRDF'),...
                sorting.GetNodeTypeAttributeValue('restwasteDestinationRatiodRDF'),...
                sorting.GetNodeTypeAttributeValue('restwasteDestinationRatioBiological'),...
                sorting.GetNodeTypeAttributeValue('restwasteDestinationRatioThermal')];
                
            % Paper
            totalrestwastepaper = (obj.total_residential_waste.paper+...
                obj.total_commercial_waste.paper)-...
                (obj.waste_to_MRF.paper+obj.recyclables.paper+...
                obj.biowaste.paper);
            
            obj.restwaste_to_cRDF.paper = restwastedistribution(1)*totalrestwastepaper;
            obj.restwaste_to_dRDF.paper = restwastedistribution(2)*totalrestwastepaper;
            obj.restwaste_to_biological.paper = restwastedistribution(3)*totalrestwastepaper;
            obj.restwaste_to_thermal.paper = restwastedistribution(4)*totalrestwastepaper;
            obj.restwaste_to_landfill.paper = (1-sum(restwastedistribution(1:4)))*totalrestwastepaper;
            
            % Glass
            totalrestwasteglass = (obj.total_residential_waste.glass+...
                obj.total_commercial_waste.glass+...
                obj.total_delivered_waste.glass)-...
                (obj.waste_to_MRF.glass+obj.recyclables.glass);
            
            obj.restwaste_to_cRDF.glass = restwastedistribution(1)*totalrestwasteglass;
            obj.restwaste_to_dRDF.glass = restwastedistribution(2)*totalrestwasteglass;
            obj.restwaste_to_biological.glass = restwastedistribution(3)*totalrestwasteglass;
            obj.restwaste_to_thermal.glass = restwastedistribution(4)*totalrestwasteglass+...
                obj.total_delivered_waste.glass*(1-deliveredglassrate)*...
                sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration');       
            obj.restwaste_to_landfill.glass = (1-sum(restwastedistribution(1:4)))*totalrestwasteglass+...
                obj.total_delivered_waste.glass*(1-deliveredglassrate)*...
                (1-sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration'));
            
            % Ferrous Metal
            totalrestwastefe_metal = (obj.total_residential_waste.fe_metal+...
                obj.total_commercial_waste.fe_metal+...
                obj.total_delivered_waste.fe_metal)-...
                (obj.waste_to_MRF.fe_metal+obj.recyclables.fe_metal);
                        
            obj.restwaste_to_cRDF.fe_metal = restwastedistribution(1)*totalrestwastefe_metal;
            obj.restwaste_to_dRDF.fe_metal = restwastedistribution(2)*totalrestwastefe_metal;
            obj.restwaste_to_biological.fe_metal = restwastedistribution(3)*totalrestwastefe_metal;
            obj.restwaste_to_thermal.fe_metal = restwastedistribution(4)*totalrestwastefe_metal+...
                obj.total_delivered_waste.fe_metal*(1-deliveredfemetalrate)*...
                sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration');
            obj.restwaste_to_landfill.fe_metal = (1-sum(restwastedistribution(1:4)))*totalrestwastefe_metal+...
                obj.total_delivered_waste.fe_metal*(1-deliveredfemetalrate)*...
                (1-sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration'));
            
            % Non-Ferrous Metal
            totalrestwastenonfe_metal = (obj.total_residential_waste.nonfe_metal+...
                obj.total_commercial_waste.nonfe_metal+...
                obj.total_delivered_waste.nonfe_metal)-...
                (obj.waste_to_MRF.nonfe_metal+obj.recyclables.nonfe_metal);
            
            obj.restwaste_to_cRDF.nonfe_metal = restwastedistribution(1)*totalrestwastenonfe_metal;
            obj.restwaste_to_dRDF.nonfe_metal = restwastedistribution(2)*totalrestwastenonfe_metal;
            obj.restwaste_to_biological.nonfe_metal = restwastedistribution(3)*totalrestwastenonfe_metal;
            obj.restwaste_to_thermal.nonfe_metal = restwastedistribution(4)*totalrestwastenonfe_metal+...
                obj.total_delivered_waste.nonfe_metal*(1-deliverednonfemetalrate)*...
                sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration');           
            obj.restwaste_to_landfill.nonfe_metal = (1-sum(restwastedistribution(1:4)))*totalrestwastenonfe_metal+...
                obj.total_delivered_waste.nonfe_metal*(1-deliverednonfemetalrate)*...
                (1-sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration')); 
                        
            % Film Plastic
            totalrestwastefilmplastic = (obj.total_residential_waste.filmplastic+...
                obj.total_commercial_waste.filmplastic+...
                obj.total_delivered_waste.filmplastic)-...
                (obj.waste_to_MRF.filmplastic+obj.recyclables.filmplastic+...
                obj.biowaste.filmplastic);
            
            obj.restwaste_to_cRDF.filmplastic = restwastedistribution(1)*totalrestwastefilmplastic;
            obj.restwaste_to_dRDF.filmplastic = restwastedistribution(2)*totalrestwastefilmplastic;
            obj.restwaste_to_biological.filmplastic = restwastedistribution(3)*totalrestwastefilmplastic;
            obj.restwaste_to_thermal.filmplastic = restwastedistribution(4)*totalrestwastefilmplastic+...
                obj.total_delivered_waste.filmplastic*(1-deliveredfilmplasticrate)*...
                sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration'); 
            obj.restwaste_to_landfill.filmplastic = (1-sum(restwastedistribution(1:4)))*totalrestwastefilmplastic+...
                obj.total_delivered_waste.filmplastic*(1-deliveredfilmplasticrate)*...
                (1-sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration'));
            
            % Rigid Plastic
            totalrestwasterigidplastic = (obj.total_residential_waste.rigidplastic+...
                obj.total_commercial_waste.rigidplastic+...
                obj.total_delivered_waste.rigidplastic)-...
                (obj.waste_to_MRF.rigidplastic+obj.recyclables.rigidplastic+...
                obj.biowaste.rigidplastic);
            
            obj.restwaste_to_cRDF.rigidplastic = restwastedistribution(1)*totalrestwasterigidplastic;
            obj.restwaste_to_dRDF.rigidplastic = restwastedistribution(2)*totalrestwasterigidplastic;
            obj.restwaste_to_biological.rigidplastic = restwastedistribution(3)*totalrestwasterigidplastic;
            obj.restwaste_to_thermal.rigidplastic = restwastedistribution(4)*totalrestwasterigidplastic+...
                obj.total_delivered_waste.rigidplastic*(1-deliveredrigidplasticrate)*...
                sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration'); 
            obj.restwaste_to_landfill.rigidplastic = (1-sum(restwastedistribution(1:4)))*totalrestwasterigidplastic+...
                obj.total_delivered_waste.rigidplastic*(1-deliveredrigidplasticrate)*...
                (1-sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration'));
            
            % Textiles
            totalrestwastetextiles = (obj.total_residential_waste.textiles+...
                obj.total_commercial_waste.textiles)-...
                (obj.waste_to_MRF.textiles+obj.recyclables.textiles);
            
            obj.restwaste_to_cRDF.textiles = restwastedistribution(1)*totalrestwastetextiles;
            obj.restwaste_to_dRDF.textiles = restwastedistribution(2)*totalrestwastetextiles;
            obj.restwaste_to_biological.textiles = restwastedistribution(3)*totalrestwastetextiles;
            obj.restwaste_to_thermal.textiles = restwastedistribution(4)*totalrestwastetextiles;
            obj.restwaste_to_landfill.textiles = (1-sum(restwastedistribution(1:4)))*totalrestwastetextiles;
            
            % Organics
            totalrestwasteorganics = (obj.total_residential_waste.organics+...
                obj.total_commercial_waste.organics+...
                obj.total_delivered_waste.garden)-...
                (obj.waste_to_MRF.organics+obj.biowaste.organics);
            
            obj.restwaste_to_cRDF.organics = restwastedistribution(1)*totalrestwasteorganics;
            obj.restwaste_to_dRDF.organics = restwastedistribution(2)*totalrestwasteorganics;
            obj.restwaste_to_biological.organics = restwastedistribution(3)*totalrestwasteorganics;
            obj.restwaste_to_thermal.organics = restwastedistribution(4)*totalrestwasteorganics;
            obj.restwaste_to_landfill.organics = (1-sum(restwastedistribution(1:4)))*totalrestwasteorganics;
            
            % Other
            totalrestwasteother = (obj.total_residential_waste.other+...
                obj.total_commercial_waste.other+...
                obj.total_delivered_waste.other)-...
                (obj.waste_to_MRF.other);
            
            obj.restwaste_to_cRDF.other = restwastedistribution(1)*totalrestwasteother;
            obj.restwaste_to_dRDF.other = restwastedistribution(2)*totalrestwasteother;
            obj.restwaste_to_biological.other = restwastedistribution(3)*totalrestwasteother;
            obj.restwaste_to_thermal.other = restwastedistribution(4)*totalrestwasteother+...
                obj.total_delivered_waste.other*(1-deliveredotherrate)*...
                sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration');
            obj.restwaste_to_landfill.other = (1-sum(restwastedistribution(1:4)))*totalrestwasteother+...
                obj.total_delivered_waste.other*(1-deliveredotherrate)*...
                (1-sorting.GetNodeTypeAttributeValue('deliveredWasteResidueTreatmentIncineration'));
            
            %% Assign Value to val
            val = [];
            
        end
    end
end