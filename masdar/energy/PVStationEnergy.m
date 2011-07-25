% %% PVStationEnergy Class Definition
% The PVStationEnergy behaviour calculates the electricity generated annually 
% in the PVstation
%
% 25 July 2011
% Damola Adepetu, aadepetu@masdar.ac.ae
%%
classdef PVStationEnergy < Behavior
    properties
        panelLength; % Length of PV panel in meters
        panelWidth; % Width of PV panel in meters
        panelEfficiency;  
        annualDNI; % Annual Direct Normal Solar irradiation kWh/m2-a
        panelCapacity; %Rated capacity of panel
        numberOfPanels; % Number of panels in station
        energyGenerated; % Energy generated annually
    end
    
    methods
        function obj = PVStationEnergy(panelLength, panelWidth,...
                panelEfficiency, annualDNI, panelCapacity,...
                numberOfPanels,energyGenerated)
            obj = obj@Behavior('PV Station Energy', ...
                ['Gets the energy generated ' ...
                'by a PV station annually. '], ...
                'kWh','[0,inf)');
            obj.panelLength = panelLength;
            obj.panelWidth = panelWidth;
            obj.panelEfficiency = panelEfficiency;
            obj.annualDNI = annualDNI;
            obj.panelCapacity = panelCapacity;
            obj.numberOfPanels = numberOfPanels;        
        end
    end