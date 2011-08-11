%% TotalDeliveredWaste Class Definition
% The TotalDeliveredWaste behavior calculates the total amount of
% bulky waste delivered by residents to the waste management system by
% waste stream
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
% Attribute IDs 41 to 47 are used in this calculation
%
% 11-August, 2011
% Sydney Do, sydneydo@mit.edu
%%
classdef TotalDeliveredWaste < Behavior
    properties
        glass;          % the total delivered glass input to the waste management system
        fe_metal;       % the total delivered ferrous metal input to the waste management system
        nonfe_metal;    % the total delivered non-ferrous metal input to the waste management system
        filmplastic;    % the total delivered film plastic input to the waste management system
        rigidplastic;   % the total delivered rigid plastic input to the waste management system
        garden;         % the total delivered garden waste input to the waste management system
        other;          % the total delivered other input to the waste management system
    end
    methods
        %% TotalDeliveredWaste Constructor
        % Instantiates a new TotalDeliveredWaste object.
        % 
        % obj = TotalDeliveredWaste()
        %   obj:            the new TotalResidentialWaste object
        %   glass:          % the total glass input to the waste management system
        %   fe_metal:       % the total ferrous metal input to the waste management system
        %   nonfe_metal:    % the total non-ferrous metal input to the waste management system
        %   filmplastic:    % the total film plastic input to the waste management system
        %   rigidplastic:   % the total rigid plastic input to the waste management system
        %   garden:         % the total garden waste input to the waste management system
        %   other:          % the total other input to the waste management system
        function obj = TotalDeliveredWaste()
            obj = obj@Behavior('Total Delivered Waste per Year', ...
                ['Calculates the total residential waste delivered ' ...
                'to the waste management system by residents by waste stream.'], ...
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
            
            % Search for building system identifier       
            for i = 1:length(city.systems)
                if strcmp(city.systems(i).name,'Building')==1
                    ind = i;
                    break
                end
            end      
            
            % Initialize waste values
            TotalGlass = 0;
            TotalFeMetal = 0;
            TotalNonFeMetal = 0;
            TotalFilmPlastic = 0;
            TotalRigidPlastic = 0;
            TotalGarden = 0;
            TotalOther = 0;
            
            % For each building system node
            for j = 1:length(city.systems(ind).nodes)
                % If the node is a residential node
                if strcmp(city.systems(ind).nodes(j).type.name,'Residential')==1
                    % Determine amount of waste generated in node by stream
                    % Total Waste per node = Node Area x Waste per Area
                    
                    % Determine Individual Waste Fractions by Node
                    TotalGlass = TotalGlass + city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('deliveredGlassDensity');
                                                    
                    TotalFeMetal = TotalFeMetal + city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('deliveredFeMetalDensity');
            
                    TotalNonFeMetal = TotalNonFeMetal + city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('deliveredNonFeMetalDensity');
                    
                    TotalFilmPlastic = TotalFilmPlastic + city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('deliveredFilmPlasticDensity');
            
                    TotalRigidPlastic = TotalRigidPlastic + city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('deliveredRigidPlasticDensity');
                                      
                    TotalGarden = TotalGarden + city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('deliveredGardenDensity');
                    
                    TotalOther = TotalOther + city.systems(ind).nodes(j).cell.GetArea()*...
                        city.systems(ind).nodes(j).GetNodeTypeAttributeValue('deliveredOtherDensity');
            
                end
            end
            
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