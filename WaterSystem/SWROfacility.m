%% SWROfacility Class Definition
%This behavior computes the energy, land and cost requirements for
%a Sea-Water Reverse Osmosis desalination plant whose specifications are
%defined by the user of CityNet.

% 1-September, 2011
% Afreen Siddiqi, siddiqi@mit.edu
%%
classdef SWROfacility < Behavior
    properties
        electricity;            %total electricity use per annum
        landfootprint;          %land area occupied/required by facility
        capex;                  %total capital costs for the plant
        opex;                   %annual operational costs
        costOfProducedWater;    %cost of produced water per unit volume
    end
    
    methods
        %% SWROfacility Constructor
        % Instantiates a new SWROfacility object.
        %
        % obj = SWROfacility()
        %   obj:                    the new SWROfacility object
        %   electricity;            %total electricity use per annum
        %   landfootprint;          %land area occupied/required by facility
        %   capex;                  %total capital costs for the plant
        %   opex;                   %annual operational costs
        %   costOfProducedWater;    %cost of produced water per unit volume

        function obj = SWROfacility()
            obj = obj@Behavior('SWRO facility behavior', ...
                ['Calculates the total energy, land and cost ' ...
                'based on SWRO facility specifications'], ...
                'n/a','[0,inf)');
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
        %   obj:    the TotalResidentialWater object handle
        function val = EvaluateImpl(obj)
            
                        
            % Initialize values
            TotalRatedOutput = 0;
            TotalElectricityRequired = 0;
            TotalLandRequired = 0;
            TotalCapex = 0;
            TotalOpex = 0;
            TotalCostOfWater = 0;
            
            city = CityNet.instance().city;
            
            for i=1:length(city.systems)
                 if strcmpi(city.systems(i).name,'Water')
                    for j=1:length(city.systems(i).nodes)
                        
                        node = city.systems(i).nodes(j);
                         if strcmpi(node.type.name,'SWRO Plant') %|| strcmpi(node.type.name,'Desal Plant 2')
             
                             %multiplying by 365 to get annual output from
                             %daily capacity ***need to add in availability
                             %factor here that is also used in cost
                             %function***
                            TotalRatedOutput = 365*city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROCapacity');
                            
                            electricityIntensity = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROspecificEnergy');
                            
                            TotalElectricityRequired = TotalRatedOutput*electricityIntensity;
                            
                            TotalLandRequired = TotalRatedOutput*...
                                city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROlandFootprint');
                            
                            capexPerUnitVol = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROspecificCapex');
                            discountRate = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROcapexDiscountRate');
                            plantLife =city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROplantLifeTime');
                            Yp = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROelectricityPrice');
                         
                            specificCostOfWater =0; specificCapex=0; specificOpex=0;
                            [specificCostOfWater,specificCapex,specificOpex,ElectricityCost]=...
                                computeCostOfWater(capexPerUnitVol, TotalRatedOutput, discountRate, plantLife, electricityIntensity,Yp)
               
               
                         end
                    end
                 end
            end
            
            
            %% Assign Outputs
            % Assign values to each handle in class
            
            val = TotalRatedOutput;
            obj.electricity = TotalElectricityRequired;
            obj.landfootprint = TotalLandRequired; %***need to check units whether to use daily or annual here*
            obj.capex = TotalRatedOutput*specificCapex;
            obj.opex = TotalRatedOutput*specificOpex;
            obj.costOfProducedWater = specificCostOfWater;
            
            
        end
    end
end