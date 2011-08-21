%% Residential WasteWater Class Definition
% This behavior calculates the wastewater from the residentialwater 
%output generated by the object as defined in TotalResidentialWater.m

%
% August, 21 2011
% Afreen Siddiqi, siddiqi@mit.edu
%%
classdef ResidentialWasteWater < Behavior
    properties
        greywater;      % the greywater (water containing no human waste) generated from residential nodes
        blackwater;
        totalResidentialWaterBehavior; %handle of object created in TotalResidentialWater.m
    end
    methods
        %% ResidentialWasteWater Constructor
        % Instantiates a new ResidentialWasteWater object.
        % 
        % obj = ResidentialWasteWater()
        %   obj:            the new ResidentialWasteWater object
        %   greywater:      the greywater (with no human waste) volume
        %   blackwater:     the blackwater (from toilets) volume
        
        function obj = ResidentialWasteWater()
            obj = obj@Behavior('Total Residential WasteWater Generated per Year', ...
                ['Calculates the residential greywater and blackwater generated volume'], ...
                'cubic meters','[0,inf)');
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
        %   obj:    the ResidentialWasteWater object handle
        function val = EvaluateImpl(obj) 
            
            obj.totalResidentialWaterBehavior.Evaluate(); %get the values of object associated with handle using the evaluate method

            TotalGreyWater = obj.totalResidentialWaterBehavior.kitchen*0.98+...
                obj.totalResidentialWaterBehavior.faucets+...
                obj.totalResidentialWaterBehavior.shower+...
                obj.totalResidentialWaterBehavior.laundry;
            
            TotalBlackWater = obj.totalResidentialWaterBehavior.toilets;
                        
            %% Assign Total Extracted Recycles to val
            val = TotalGreyWater+TotalBlackWater;
            obj.greywater = TotalGreyWater;
            obj.blackwater = TotalBlackWater;
            
        end
    end
end