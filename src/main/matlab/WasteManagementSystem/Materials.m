%% Materials Class Definition
% The Materials behavior calculates the useable energy extracted
% from incineration, RDF burning, and PPDF burning. In addition, the amount
% of recovered ferrous metal is also calculated.
% The inputs to this object arise from the WasteSorting.m, MRFSorting.m,
% RDFSorting.m, and BiologicalTreatment.m objects.
% In addition, the amount of non-hazardous and hazardous residues is
% calculated. This residue is in turn sent to landfill
%
% 7-September, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef Materials < Behavior
    properties
        recyclables;                        % the recyclable material extracted from the preliminary sorting process (function input)
        mrf_recyclables_extracted;          % recyclables extracted from the materials recovery process (function input)
        rdf_fe_metal_recovered;             % ferrous metal recovered from the refuse derived material sorting process (function input)
        rdf_nonfe_metal_recovered;          % non-ferrous metal recovered from the refuse derived material sorting process (function input)
        bio_presort_materials_recovered;	% materials recovered from the presort process performed during biological treatment (function input)
        bio_compost_extracted;              % compost extracted from the biological treatment process (function input)
        bio_biogas_extracted;               % biogas extracted from the biological treatment process (function input)
        paper_recovered;                    % paper recovered from the waste management process
        glass_recovered;                    % glass recovered from the waste management process
        fe_metal_recovered;                 % ferrous metal recovered from the waste management process
        nonfe_metal_recovered;              % non-ferrous recovered from the waste management process
        filmplastic_recovered;              % film plastic recovered from the waste management process
        rigidplastic_recovered;             % rigid plastic recovered from the waste management process
        textiles_recovered;                 % textiles recovered from the waste management process
        compost_recovered;                  % compost extracted from the waste management process
        biogas_extracted;                   % biogas extracted from the waste management process
    end
    methods
        %% Materials Constructor
        % Instantiates a new Materials object.
        % 
        % obj = Materials()
        %   obj:                                the new Materials object
        %   recyclables:                        the recyclable material extracted from the preliminary sorting process (function input)
        %   mrf_recyclables_extracted:          recyclables extracted from the materials recovery process (function input)
        %   rdf_fe_metal_recovered:             ferrous metal recovered from the refuse derived material sorting process (function input)
        %   rdf_nonfe_metal_recovered:          non-ferrous metal recovered from the refuse derived material sorting process (function input)
        %   bio_presort_materials_recovered:	materials recovered from the presort process performed during biological treatment (function input)
        %   bio_compost_extracted:              compost extracted from the biological treatment process (function input)
        %   bio_biogas_extracted:               biogas extracted from the biological treatment process (function input)
        %   paper_recovered:                    paper recovered from the waste management process
        %   glass_recovered:                    glass recovered from the waste management process
        %   fe_metal_recovered:                 ferrous metal recovered from the waste management process
        %   nonfe_metal_recovered:              non-ferrous recovered from the waste management process
        %   filmplastic_recovered:              film plastic recovered from the waste management process
        %   rigidplastic_recovered:             rigid plastic recovered from the waste management process
        %   textiles_recovered:                 textiles recovered from the waste management process
        %   compost_recovered:                  compost extracted from the waste management process
        %   biogas_extracted:                   biogas extracted from the waste management process
        
        function obj = Materials()
            obj = obj@Behavior('Materials', ...
                'Calculates the amount of materials recovered from the waste management process', ...
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
            
            %% Determine the amount of materials recovered by waste stream
            
            % Paper Stream
            obj.paper_recovered = obj.recyclables.paper+...
                obj.mrf_recyclables_extracted.paper+...
                obj.rdf_fe_metal_recovered.paper+...
                obj.rdf_nonfe_metal_recovered.paper;
                
            % Glass Stream
            obj.glass_recovered = obj.recyclables.glass+...
                obj.mrf_recyclables_extracted.glass+...
                obj.rdf_fe_metal_recovered.glass+...
                obj.rdf_nonfe_metal_recovered.glass+...
                obj.bio_presort_materials_recovered.glass;
            
            % Ferrous Metal Stream
            obj.fe_metal_recovered = obj.recyclables.fe_metal+...
                obj.mrf_recyclables_extracted.fe_metal+...
                obj.rdf_fe_metal_recovered.fe_metal+...
                obj.rdf_nonfe_metal_recovered.fe_metal+...
                obj.bio_presort_materials_recovered.fe_metal;
            
            % Non-ferrous Metal Stream
            obj.nonfe_metal_recovered = obj.recyclables.nonfe_metal+...
                obj.mrf_recyclables_extracted.nonfe_metal+...
                obj.rdf_fe_metal_recovered.nonfe_metal+...
                obj.rdf_nonfe_metal_recovered.nonfe_metal+...
                obj.bio_presort_materials_recovered.nonfe_metal;
            
            % Film Plastic Stream
            obj.filmplastic_recovered = obj.recyclables.filmplastic+...
                obj.mrf_recyclables_extracted.filmplastic+...
                obj.rdf_fe_metal_recovered.filmplastic+...
                obj.rdf_nonfe_metal_recovered.filmplastic+...
                obj.bio_presort_materials_recovered.filmplastic;
            
            % Rigid Plastic Stream
            obj.rigidplastic_recovered = obj.recyclables.rigidplastic+...
                obj.mrf_recyclables_extracted.rigidplastic+...
                obj.rdf_fe_metal_recovered.rigidplastic+...
                obj.rdf_nonfe_metal_recovered.rigidplastic+...
                obj.bio_presort_materials_recovered.rigidplastic;
            
            % Textiles Stream
            obj.textiles_recovered = obj.recyclables.textiles+...
                obj.mrf_recyclables_extracted.textiles+...
                obj.rdf_fe_metal_recovered.textiles+...
                obj.rdf_nonfe_metal_recovered.textiles+...
                obj.bio_presort_materials_recovered.textiles;
            
            % Compost and Organics Stream
            % NB. Here, compost and organics are treated as one
            obj.compost_recovered = obj.mrf_recyclables_extracted.organics+...
                obj.rdf_fe_metal_recovered.organics+...
                obj.rdf_nonfe_metal_recovered.organics+...
                obj.bio_compost_extracted;
            
            % Biogas Extracted
            obj.biogas_extracted = obj.bio_biogas_extracted;
                    
            %% Assign null value to val
            val = [];
            
        end
    end
end