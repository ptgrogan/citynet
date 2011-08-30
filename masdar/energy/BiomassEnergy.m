% %% BiomassEnergy Class Definition
% The BiomassEnergy behaviour calculates the electricity generated annually 
% in the biomass power plant.
%
% 27 Aug 2011
% Damola Adepetu, aadepetu@masdar.ac.ae
%%
classdef BiomassEnergy < Behavior
    properties
        capacity;       % capacity of the power plant
        fuelConsumed;   % the amount of fuel required to run the plant
        cell;           % handle of the cell in which to evaluate the behavior
        system;         % handle of the energy system
    end
    methods(Access=public)
        function obj = BiomassEnergy(cell,system)
            obj = obj@Behavior('Biomass Energy', ...
                ['Gets the energy generated ' ...
                'by a biomass power plant annually. '], ...
                'kWh','[0,inf)');   
            obj.cell = cell;
            obj.system = system;
        end
    end
    methods(Access=protected)
        function val = EvaluateImpl(obj)
            val = 0; % initialize value
            fuelUsed = 0;
            plantCapacity = 0;
            for i=1:length(obj.system.nodes) % for each node in specified system:
                node = obj.system.nodes(i);
                if eq(node.cell,obj.cell) && strcmp(node.type.name,'Biomass Station')
                    % if node has specified cell and  node type is Biomass
                    % Station retrieve attributes from the synthesis template
                    plantType = node.GetNodeTypeAttributeValue('Plant Type');
                    steamTurbineCapacity = node.GetNodeTypeAttributeValue('Steam Turbine Capacity');
                    gasTurbineCapacity = node.GetNodeTypeAttributeValue('Gas Turbine Capacity');
                    fuelLHV = node.GetNodeTypeAttributeValue('Fuel LHV');
                    plantConversionEfficiency = node.GetNodeTypeAttributeValue('Plant Conversion Efficiency');
                    annualFullLoadHours = node.GetNodeTypeAttributeValue('Full Load Hours of Operation');
                    
                    plantCapacity = steamTurbineCapacity + gasTurbineCapacity;
                    val = annualFullLoadHours*plantCapacity;
                    fuelUsed = plantCapacity*3.6*annualFullLoadHours/(fuelLHV*plantConversionEfficiency);
                    
                    break % break out of for loop
                end
            end
            obj.fuelConsumed = fuelUsed;
            obj.capacity = plantCapacity;
        end
    end
end