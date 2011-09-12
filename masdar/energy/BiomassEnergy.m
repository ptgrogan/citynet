% %% BiomassEnergy Class Definition
% The BiomassEnergy behaviour calculates the electricity generated annually 
% in the biomass power plant.
%
% 27 Aug 2011
% Damola Adepetu, aadepetu@masdar.ac.ae
%%
classdef BiomassEnergy < Behavior
    properties
        biomassEnergyGenerated = containers.Map('KeyType','int32','ValueType','double');
        capacity;       % capacity of the power plant
        fuelConsumed;   % the amount of fuel required to run the plant
%         cell;           % handle of the cell in which to evaluate the behavior
%         system;         % handle of the energy system
    end
    methods
        function obj = BiomassEnergy()
            obj = obj@Behavior('Biomass Energy', ...
                ['Gets the energy generated ' ...
                'by a biomass power plant annually. '], ...
                'kWh','[0,inf)');   
%             obj.cell = cell;
%             obj.system = system;
        end
        %% PlotBiomassEnergy Function
        
        function PlotBiomassEnergy(obj)
            figure
            title(['Biomass Energy Generation (' obj.units ')'])
            obj.PlotCellValueMap(obj.BiomassEnergy)
        end
    end
    methods(Access=protected)
        function val = EvaluateImpl(obj)
            val = 0; % initialize value
            fuelUsed = 0;
            plantCapacity = 0;
            clear obj.biomassEnergyGenerated
            obj.biomassEnergyGenerated = containers.Map('KeyType','int32','ValueType','double');
            city = CityNet.instance().city;
            for s=1:length(city.systems)
                if strcmpi(city.systems.name,'Energy') 
                    for i=1:length(city.systems(s).nodes)
                        if strcmpi(city.system(s).nodes(i), 'Biomass Station')
                        node = city.systems(s).nodes(i);
                        % if node has specified cell and  node type is Biomass
                        % Station retrieve attributes from the synthesis template
                        % plantType = node.GetNodeTypeAttributeValue('Plant Type');
                        steamTurbineCapacity = node.GetNodeTypeAttributeValue('Steam Turbine Capacity');
                        gasTurbineCapacity = node.GetNodeTypeAttributeValue('Gas Turbine Capacity');
                        fuelLHV = node.GetNodeTypeAttributeValue('Fuel LHV');
                        plantConversionEfficiency = node.GetNodeTypeAttributeValue('Plant Conversion Efficiency');
                        annualFullLoadHours = node.GetNodeTypeAttributeValue('Full Load Hours of Operation');

                        plantCapacity = steamTurbineCapacity + gasTurbineCapacity;
                        val = annualFullLoadHours*plantCapacity;
                        obj.fuelConsumed = plantCapacity*3.6*annualFullLoadHours/(fuelLHV*plantConversionEfficiency);
                        obj.capacity = plantCapacity;
                        end
                    break
                    end
                end
           end
        end
    end
end