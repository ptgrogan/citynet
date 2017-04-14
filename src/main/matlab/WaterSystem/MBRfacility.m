%% MBRfacility Class Definition
%This behavior computes the energy, land and cost requirements for
%a Sea-Water Reverse Osmosis desalination plant whose specifications are
%defined by the user of CityNet.

% 6-September, 2011
% Afreen Siddiqi, siddiqi@mit.edu
%%
classdef MBRfacility < Behavior
    properties
        electricity;            %total electricity use per annum
        landfootprint;          %land area occupied/required by facility
        costOfTreatedWater;    %cost of treated water per unit volume
        
    end
    
    methods
        %% MBRfacility Constructor
        % Instantiates a new MBRfacility object.
        %
        % obj = MBRfacility()
        %   obj:                    the new SWROfacility object
        %   electricity;            %total electricity use per annum
        %   landfootprint;          %land area occupied/required by facility
        %   costOfTreatedWater;    %cost of treated water per unit volume

        function obj = MBRfacility()
            obj = obj@Behavior('MBR facility requirements', ...
                ['Calculates the total electricity, land and specific cost ' ...
                'based on MBR plant parameters'], ...
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
            
            
            city = CityNet.instance().city;
            
            for i=1:length(city.systems)
                 if strcmpi(city.systems(i).name,'Water')
                    for j=1:length(city.systems(i).nodes)
                        
                        node = city.systems(i).nodes(j);
                         if strcmpi(node.type.name,'MBR Plant') 
             
                             %multiplying by 365 to get annual output from daily capacity
 
                            TotalRatedOutput = 365*city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRCapacity');
                            
                            Availability = city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRAvailability');
                            
                            electricityIntensity = city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRspecificEnergy');
                            
                            TotalElectricityRequired = TotalRatedOutput*electricityIntensity;
                            
                            %divide daily capacity with 24 to get hourly capacity and multiply
                            %with land footprint coefficient which is in units of m^2/m^3/hr
                            
                            TotalLandRequired = city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRCapacity')/24*...
                                city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRlandFootprint');
                            
                            capexPerUnitVol = city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRspecificCapex');
                            
                            opexPerUnitVol = city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRspecificOpex');
                            %opexPerUnitVol = 475;
                            
                            discountRate = city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRcapexDiscountRate');
                            
                            plantLife =city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRplantLifeTime');
                            
                            Yp = city.systems(i).nodes(j).GetNodeTypeAttributeValue('MBRelectricityPrice');
                         
                            
                            
                            [specificCostOfWater,specificAnnualCapex,specificAnnualOpex,ElectricityCost]=...
                                computeCostOfWater(TotalRatedOutput, Availability, capexPerUnitVol, opexPerUnitVol, discountRate, plantLife, electricityIntensity,Yp);
               
               
                         end
                    end
                 end
            end
            
            
            %% Assign Outputs
            % Assign values to each handle in class
            
            val = TotalRatedOutput %annual water produced [m^3]
            obj.electricity = TotalElectricityRequired %annual electricity [kWh]
            obj.landfootprint = TotalLandRequired %[m^2]
            obj.costOfTreatedWater = specificCostOfWater; %[$/m^3], total cost of water production
            
            
        end
    end
end