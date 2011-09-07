%% BiologicalTreatment Class Definition
% The BiologicalTreatment behavior calculates the useable compost and biogas extracted 
% from the restwaste output generated by the WasteSorting.m and RDFSorting.m object
% In addition, the amount of residues is calculated, along with the amount
% going to either thermal treatment or landfill (as set by the user)
%
% 25-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef BiologicalTreatment < Behavior
    properties
        biowaste;                       % the biowaste input from the waste sorting process (function input)
        restwaste_to_biological;        % the additional restwaste added from the sorting process (function input)
        fines_to_biological;            % the input arising from fines generated during the RDF sorting process (function input)
        total_biological_input;         % aggregates the biological treatment inputs from other processes
        presort_materials_recovered;    % the materials recovered from the input stream presorting process
        compost_extracted;              % the amount of dRDF extracted from the RDF Sorting process
        biogas_extracted;               % the fraction of the process input that is considered rogue and not able to be processed. This is sent directly to landfill
        residues_to_thermal;            % residues from the biological treatment process sent to thermal treatment
        residues_to_landfill;           % residues generated from RDF sorting process. These are automatically sent to landfill
    end
    methods
        %% BiologicalTreatment Constructor
        % Instantiates a new BiologicalTreatment object.
        % 
        % obj = BiologicalTreatment()
        %   obj:                            the new BiologicalTreatment object
        %   biowaste;                       the biowaste input from the waste sorting process
        %   restwaste_to_biological;        the additional restwaste added from the sorting process
        %   fines_to_biological;            the input arising from fines generated during the RDF sorting process
        %   total_biological_input;         aggregates the biological treatment inputs from other processes
        %   presort_materials_recovered;    the materials recovered from the input stream presorting process
        %   compost_extracted;              the amount of dRDF extracted from the RDF Sorting process
        %   biogas_extracted;               the fraction of the process input that is considered rogue and not able to be processed. This is sent directly to landfill
        %   residues_to_thermal;            residues from the biological treatment process sent to thermal treatment
        %   residues_to_landfill;           residues generated from RDF sorting process. These are automatically sent to landfill
        
        function obj = BiologicalTreatment()
            obj = obj@Behavior('Biological Treatment', ...
                'Calculates the outputs of the biological treatment (both composting and biogasification) process', ...
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
                if strcmp(city.systems(ind).nodes(i).type.name,'Biological Treatment')
                    % Store node ID
                    ind2 = i;
                    break
                end
            end          
            
            % Set Materials Sorting Facility Identifier
            biological = city.systems(ind).nodes(ind2);
                  
            %% Determine total input to biological treatment
            % Paper stream
            obj.total_biological_input.paper = obj.biowaste.paper+obj.restwaste_to_biological.paper+...
                obj.fines_to_biological.paper;
            
            % Glass stream
            obj.total_biological_input.glass = obj.restwaste_to_biological.glass+...
                obj.fines_to_biological.glass;
            
            % Ferrous metal stream
            obj.total_biological_input.fe_metal = obj.restwaste_to_biological.fe_metal+...
                obj.fines_to_biological.fe_metal;
                     
            % Non-Ferrous metal stream
            obj.total_biological_input.nonfe_metal = obj.restwaste_to_biological.nonfe_metal+...
                obj.fines_to_biological.nonfe_metal;
            
            % Film plastic stream
            obj.total_biological_input.filmplastic = obj.biowaste.filmplastic+...
                obj.restwaste_to_biological.filmplastic+obj.fines_to_biological.filmplastic;
            
            % Rigid plastic stream
            obj.total_biological_input.rigidplastic = obj.biowaste.rigidplastic+...
                obj.restwaste_to_biological.rigidplastic+obj.fines_to_biological.rigidplastic;
            
            % Textiles stream
            obj.total_biological_input.textiles = obj.restwaste_to_biological.textiles+...
                obj.fines_to_biological.textiles;
            
            % Organics stream
            obj.total_biological_input.organics = obj.biowaste.organics+...
                obj.restwaste_to_biological.organics+obj.fines_to_biological.organics;
            
            % Other stream
            obj.total_biological_input.other = obj.restwaste_to_biological.other+...
                obj.fines_to_biological.other;
            
            %% Determine amount of materials extracted from presort of process input streams
            % Here, the recovered materials from both composting and
            % biogasification input streams are combined
            
            % Glass stream:
            obj.presort_materials_recovered.glass = obj.total_biological_input.glass*...
                (biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentGlass')+...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentGlass'));
            
            % Ferrous metal stream:
            obj.presort_materials_recovered.fe_metal = obj.total_biological_input.fe_metal*...
                (biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentFeMetal')+...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentFeMetal'));
            
            % Non-ferrous metal stream:
            obj.presort_materials_recovered.nonfe_metal = obj.total_biological_input.nonfe_metal*...
                (biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentNonFeMetal')+...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentNonFeMetal'));
                        
            % Film plastic stream:
            obj.presort_materials_recovered.filmplastic = obj.total_biological_input.filmplastic*...
                (biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentFilmPlastic')+...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentFilmPlastic'));
            
            % Rigid plastic stream:
            obj.presort_materials_recovered.rigidplastic = obj.total_biological_input.rigidplastic*...
                (biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentRigidPlastic')+...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentRigidPlastic'));
            
            % Textiles stream:
            obj.presort_materials_recovered.textiles = obj.total_biological_input.textiles*...
                (biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentTextiles')+...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentTextiles'));
            
            %% Determine amount of compost extracted from composting process
            obj.compost_extracted = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                (obj.total_biological_input.paper*...
                (1-biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionPaper'))+...
                obj.total_biological_input.organics*...
                (1-biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionOrganics')))*...
                (1-biological.GetNodeTypeAttributeValue('compostingProcessMassLoss'))*...
                biological.GetNodeTypeAttributeValue('compostingFractionMarketable');
            
            %% Determine amount of biogas extracted from biogasification process
            obj.biogas_extracted = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                (obj.total_biological_input.paper*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionPaper'))+...
                obj.total_biological_input.organics*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionOrganics')))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationProcessMassLoss'))*...
                biological.GetNodeTypeAttributeValue('biogasificationFractionMarketable');
            
            %% Determine amount of residues from the presorting and biological 
            % treatment processes sent to thermal treatment for incineration
            % (for both composting and biogasification)
            % Note that process residues only arise from the paper and
            % organic waste streams, whereas presort residues arise for all
            % waste streams
            
            % Paper stream
            obj.residues_to_thermal.paper = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.paper*...
                biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionPaper')*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.paper*...
                (1-biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionPaper'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingFractionMarketable'))*...
                biological.GetNodeTypeAttributeValue('compostingProcessResiduestoIncineration')+...% Composting process residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.paper*...
                biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionPaper')*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration')+...% Biogasification presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.paper*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionPaper'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationFractionMarketable'))*...
                biological.GetNodeTypeAttributeValue('biogasificationProcessResiduestoIncineration');   % Biogasification process residue
            
            % Glass stream
            obj.residues_to_thermal.glass = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.glass*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentGlass'))*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.glass*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentGlass'))*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration');   % Biogasification presort residue
            
            % Ferrous metal stream
            obj.residues_to_thermal.fe_metal = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.fe_metal*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentFeMetal'))*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.fe_metal*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentFeMetal'))*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration');   % Biogasification presort residue
            
            % Non-ferrous metal stream
            obj.residues_to_thermal.nonfe_metal = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.nonfe_metal*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentNonFeMetal'))*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.nonfe_metal*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentNonFeMetal'))*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration');   % Biogasification presort residue
            
            % Film plastic stream
            obj.residues_to_thermal.filmplastic = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.filmplastic*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentFilmPlastic'))*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.filmplastic*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentFilmPlastic'))*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration');   % Biogasification presort residue
            
            % Rigid plastic stream
            obj.residues_to_thermal.rigidplastic = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.rigidplastic*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentRigidPlastic'))*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.rigidplastic*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentRigidPlastic'))*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration');   % Biogasification presort residue
                
            % Textiles stream
            obj.residues_to_thermal.textiles = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.textiles*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentTextiles'))*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.textiles*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentTextiles'))*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration');   % Biogasification presort residue

            % Organics stream
            obj.residues_to_thermal.organics = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.organics*...
                biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionOrganics')*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.organics*...
                (1-biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionOrganics'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingFractionMarketable'))*...
                biological.GetNodeTypeAttributeValue('compostingProcessResiduestoIncineration')+...% Composting process residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.organics*...
                biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionOrganics')*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration')+...% Biogasification presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.organics*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionOrganics'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationFractionMarketable'))*...
                biological.GetNodeTypeAttributeValue('biogasificationProcessResiduestoIncineration');   % Biogasification process residue
            
            % Other stream
            obj.residues_to_thermal.other = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.other*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentOther'))*...
                biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration')+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.other*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentOther'))*...
                biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration');   % Biogasification presort residue
            
            % Compost stream
            % This final stream arises from the fraction of compost and
            % biogas produced that is not marketable
            obj.residues_to_thermal.compost = obj.compost_extracted*...
                (1-biological.GetNodeTypeAttributeValue('compostingFractionMarketable'))*...
                 biological.GetNodeTypeAttributeValue('compostingProcessResiduestoIncineration')+...% Composting process residues to thermal treatment
                 obj.biogas_extracted*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationFractionMarketable'))*...
                 biological.GetNodeTypeAttributeValue('biogasificationProcessResiduestoIncineration');  % Biogasification process residues to thermal treatment         
            
            %% Determine amount of residues from the presorting and biological 
            % treatment processes sent to landfill
            % (for both composting and biogasification)
            % This also includes the fraction of the total input stream to
            % biological treatment sent directly to landfill
            % Note that process residues only arise from the paper and
            % organic waste streams, whereas presort residues arise for all
            % waste streams
            
            % Paper stream
            obj.residues_to_landfill.paper = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.paper*...
                biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionPaper')*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.paper*...
                (1-biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionPaper'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingFractionMarketable'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingProcessResiduestoIncineration'))+...% Composting process residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.paper*...
                biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionPaper')*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...% Biogasification presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.paper*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionPaper'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationFractionMarketable'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationProcessResiduestoIncineration'))+...   % Biogasification process residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.paper;   % Component of input stream sent directly to landfill
            
            % Glass stream
            obj.residues_to_landfill.glass = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.glass*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentGlass'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.glass*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentGlass'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...   % Biogasification presort residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.glass;   % Component of input stream sent directly to landfill
            
            % Ferrous metal stream
            obj.residues_to_thermal.fe_metal = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.fe_metal*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentFeMetal'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.fe_metal*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentFeMetal'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...   % Biogasification presort residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.fe_metal;   % Component of input stream sent directly to landfill
            
            % Non-ferrous metal stream
            obj.residues_to_landfill.nonfe_metal = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.nonfe_metal*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentNonFeMetal'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.nonfe_metal*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentNonFeMetal'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...   % Biogasification presort residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.nonfe_metal;   % Component of input stream sent directly to landfill
            
            % Film plastic stream
            obj.residues_to_landfill.filmplastic = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.filmplastic*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentFilmPlastic'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.filmplastic*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentFilmPlastic'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...   % Biogasification presort residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.filmplastic;   % Component of input stream sent directly to landfill
            
            % Rigid plastic stream
            obj.residues_to_landfill.rigidplastic = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.rigidplastic*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentRigidPlastic'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.rigidplastic*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentRigidPlastic'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...   % Biogasification presort residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.rigidplastic;   % Component of input stream sent directly to landfill
                
            % Textiles stream
            obj.residues_to_landfill.textiles = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.textiles*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentTextiles'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.textiles*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentTextiles'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...   % Biogasification presort residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.textiles;   % Component of input stream sent directly to landfill

            % Organics stream
            obj.residues_to_landfill.organics = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.organics*...
                biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionOrganics')*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.organics*...
                (1-biological.GetNodeTypeAttributeValue('compostingNonbiodegradableFractionOrganics'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingFractionMarketable'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingProcessResiduestoIncineration'))+...% Composting process residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.organics*...
                biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionOrganics')*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...% Biogasification presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.organics*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationNonbiodegradableFractionOrganics'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationProcessMassLoss'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationFractionMarketable'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationProcessResiduestoIncineration'))+...   % Biogasification process residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.organics;   % Component of input stream sent directly to landfill
            
            % Other stream
            obj.residues_to_landfill.other = biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')*...
                obj.total_biological_input.other*...
                (1-biological.GetNodeTypeAttributeValue('compostingPresortRecoverPercentOther'))*...
                (1-biological.GetNodeTypeAttributeValue('compostingSortingResiduestoIncineration'))+...% Composting presort residue
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification')*...
                obj.total_biological_input.other*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationPresortRecoverPercentOther'))*...
                (1-biological.GetNodeTypeAttributeValue('biogasificationSortingResiduestoIncineration'))+...   % Biogasification presort residue
                (1-biological.GetNodeTypeAttributeValue('biologicalInputFractiontoComposting')-...
                biological.GetNodeTypeAttributeValue('biologicalInputFractiontoBiogasification'))*...
                obj.total_biological_input.other;   % Component of input stream sent directly to landfill

            %% Assign null value to val
            val = [];
            
        end
    end
end