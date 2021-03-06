%% Commercial WasteWater Class Definition
% This behavior calculates the wastewater from the commercialwater 
%output generated by the object as defined in TotalCommercialWater.m

%
% August, 21 2011
% Afreen Siddiqi, siddiqi@mit.edu
%%
classdef CommercialWasteWater < Behavior
    properties
        greywater;      % the greywater (water containing no human waste) generated from commercial nodes
        blackwater;
        totalCommercialWaterBehavior; %handle of object created in TotalCommercialWater.m
    end
    methods
        %% CommercialWasteWater Constructor
        % Instantiates a new CommercialWasteWater object.
        % 
        % obj = CommercialWasteWater()
        %   obj:            the new CommercialWasteWater object
        %   greywater:      the greywater (with no human waste) volume
        %   blackwater:     the blackwater (from toilets) volume
        
        function obj = CommercialWasteWater()
            obj = obj@Behavior('Total Commercial WasteWater Generated per Year', ...
                ['Calculates the Commercial greywater and blackwater generated'], ...
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
        %   obj:    the CommercialWasteWater object handle
        function val = EvaluateImpl(obj) 
            
            obj.totalCommercialWaterBehavior.Evaluate(); %get the values of object associated with handle using the evaluate method

            TotalGreyWater = obj.totalCommercialWaterBehavior.kitchen*0.98+...
                obj.totalCommercialWaterBehavior.faucets;
            
            TotalBlackWater = obj.totalCommercialWaterBehavior.toilets;
                        
            %% Assign Total Extracted Recycles to val
            val = TotalGreyWater+TotalBlackWater;
            obj.greywater = TotalGreyWater;
            obj.blackwater = TotalBlackWater;
            
        end
    end
end