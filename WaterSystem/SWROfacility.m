%% SWROfacility Class Definition
%This behavior computes the energy, land and cost requirements for
%a Sea-Water Reverse Osmosis desalination plant whose specifications are
%defined by the user of CityNet.

% 1-September, 2011
% Afreen Siddiqi, siddiqi@mit.edu

%References: 
%[1] Desalination and Advanced Water Treatment: Economics and Financing by Corrado Sommariva, 2010
%[2] Desalination: A National Perspective, National Research Council, 2008
%%
classdef SWROfacility < Behavior
    properties
        electricity;            %total electricity use per annum
        landfootprint;          %land area occupied/required by facility
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
        %   costOfProducedWater;    %cost of produced water per unit volume

        function obj = SWROfacility()
            obj = obj@Behavior('SWRO facility requirements', ...
                ['Calculates the total electricity, land and specific cost ' ...
                'based on SWRO plant parameters'], ...
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
            TotalCostOfWater = 0;
            
            city = CityNet.instance().city;
            
            for i=1:length(city.systems)
                 if strcmpi(city.systems(i).name,'Water')
                    for j=1:length(city.systems(i).nodes)
                        
                        node = city.systems(i).nodes(j);
                         if strcmpi(node.type.name,'SWRO Plant') 
             
                             %multiplying by 365 to get annual output from daily capacity
 
                            TotalRatedOutput = 365*city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROCapacity');
                            
                            Availability = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROAvailability');
                            
                            electricityIntensity = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROspecificEnergy');
                            
                            TotalElectricityRequired = TotalRatedOutput*electricityIntensity;
                            
                            %divide daily capacity with 24 to get hourly capacity and multiply
                            %with land footprint coefficient which is in units of m^2/m^3/hr
                            
                            TotalLandRequired = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROCapacity')/24*...
                                city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROlandFootprint');
                            
                            capexPerUnitVol = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROspecificCapex');
                            
                            %Reference: [2], Table 6-2
                            PartsAndMaintenance = 0.03; %[$/m^3]
                            Chemicals = 0.07; %[$/m^3]
                            Labor = 0.1; %[$/m^3]
                            Membranes = 0.03;  %[$/m^3]
                            opexPerUnitVol = PartsAndMaintenance+Chemicals+Labor+Membranes; %[$/m^3]

                            discountRate = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROcapexDiscountRate');
                            plantLife =city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROplantLifeTime');
                            Yp = city.systems(i).nodes(j).GetNodeTypeAttributeValue('SWROelectricityPrice');
                         
                            specificCostOfWater =0; %specificCapex=0; specificOpex=0;
                            
                            [specificCostOfWater,specificAnnualCapex,specificAnnualOpex,ElectricityCost]=...
                                computeCostOfWater(TotalRatedOutput, Availability, capexPerUnitVol, opexPerUnitVol, discountRate, plantLife, electricityIntensity,Yp);
               
               
                         end
                    end
                 end
            end
            
            
            %% Assign Outputs
            % Assign values to each handle in class
            
            val = TotalRatedOutput; %annual water produced [m^3]
            obj.electricity = TotalElectricityRequired; %annual electricity [kWh]
            obj.landfootprint = TotalLandRequired; %[m^2]
            obj.costOfProducedWater = specificCostOfWater; %[$/m^3], total cost of water production
            
            
        end
    end
end