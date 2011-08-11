%% TotalCommercialWaste Class Definition
% The TotalCommercialWaste behavior calculates the total amount of
% commercial waste generated by the city by specific waste stream. 
% The waste streams are as follows:
% - Paper
% - Glass
% - Ferrous Metal
% - Non-Ferrous Metal
% - Film Plastic
% - Rigid Plastic
% - Textiles
% - Organics
% - Other
% Attribute IDs 29 to 40 are used in this calculation
%
% 10-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef TotalCommercialWaste < Behavior
    properties
        paper;          % the total paper input to the waste management system
        glass;          % the total glass input to the waste management system
        fe_metal;       % the total ferrous metal input to the waste management system
        nonfe_metal;    % the total non-ferrous metal input to the waste management system
        filmplastic;    % the total film plastic input to the waste management system
        rigidplastic;   % the total rigid plastic input to the waste management system
        textiles;       % the total paper input to the waste management system
        organics;       % the total paper input to the waste management system
        other;          % the total paper input to the waste management system
    end
    methods
        %% TotalCommercialWaste Constructor
        % Instantiates a new TotalCommercialWaste object.
        % 
        % obj = TotalCommercialWaste()
        %   obj:            the new TotalCommercialWaste object
        %   paper:          % the total paper input to the waste management system
        %   glass:          % the total glass input to the waste management system
        %   fe_metal:       % the total ferrous metal input to the waste management system
        %   nonfe_metal:    % the total non-ferrous metal input to the waste management system
        %   filmplastic:    % the total film plastic input to the waste management system
        %   rigidplastic:   % the total rigid plastic input to the waste management system
        %   textiles:       % the total paper input to the waste management system
        %   organics:       % the total paper input to the waste management system
        %   other:          % the total paper input to the waste management system
        function obj = TotalCommercialWaste()
            obj = obj@Behavior('Total Commercial Waste per Year', ...
                ['Calculates the total commercial waste generated ' ...
                'in the city by waste stream.'], ...
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
            
            % For each building system node, identify commercial node and
            % calculate corresponding waste generated per node by stream.
            % Following this, sum all stream values
            
            city = CityNet.instance().city;
            
            % Search for building system identifier       
            for i = 1:length(city.systems)
                if strcmp(city.systems(i).name,'Building')==1
                    ind = i;
                    break
                end
            end      
                      
            % Initialize waste values
            TotalWaste = 0;
            TotalPaper = 0;
            TotalGlass = 0;
            TotalFeMetal = 0;
            TotalNonFeMetal = 0;
            TotalFilmPlastic = 0;
            TotalRigidPlastic = 0;
            TotalTextiles = 0;
            TotalOrganics = 0;
            TotalOther = 0;
            
            % For each building system node
            for j = 1:length(city.systems(ind).nodes)
                % If the node is a commercial node
                if strcmp(city.systems(ind).nodes(j).type.name,'Commercial')==1
                    % Determine amount of waste generated in node by stream
                    
                    % Total Waste per node = Node Area x Waste per Area
                    totalnodalwaste = city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteDensity');
                                      
                    TotalWaste = TotalWaste + totalnodalwaste;
                
                    % Determine Individual Waste Fractions by Node
                    TotalPaper = TotalPaper + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWastePaperPercent');
                    
                    TotalGlass = TotalGlass + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteGlassPercent');
                    
                    TotalFeMetal = TotalFeMetal + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteMetalPercent')*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteFerrousPercent');
            
                    TotalNonFeMetal = TotalNonFeMetal + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteMetalPercent')*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteNonFerrousPercent');
                    
                    TotalFilmPlastic = TotalFilmPlastic + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWastePlasticsPercent')*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteFilmPlasticPercent');
            
                    TotalRigidPlastic = TotalRigidPlastic + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWastePlasticsPercent')*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteRigidPlasticPercent');
                    
                    TotalTextiles = TotalTextiles + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteTextilesPercent');
                    
                    TotalOrganics = TotalOrganics + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteOrganicsPercent');
                    
                    TotalOther = TotalOther + totalnodalwaste*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('commercialWasteOtherPercent');
            
                end
            end
                       
            %% Assign Outputs
            % Assign values to each handle in class
            val = TotalWaste;
            obj.paper = TotalPaper;
            obj.glass = TotalGlass;
            obj.fe_metal = TotalFeMetal;
            obj.nonfe_metal = TotalNonFeMetal;
            obj.filmplastic = TotalFilmPlastic;
            obj.rigidplastic = TotalRigidPlastic;
            obj.textiles = TotalTextiles;
            obj.organics = TotalOrganics;
            obj.other = TotalOther;
            
        end
    end
end