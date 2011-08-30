% %% HydroEnergy Class Definition
% The HydroEnergy behaviour calculates the electricity generated annually 
% in the hydropower plant
%
% 25 July 2011
% Damola Adepetu, aadepetu@masdar.ac.ae
%%
classdef HydroEnergy < Behavior
    properties
        cell;       % handle of the cell in which to evaluate the behavior
        system;     % handle of the energy system
        capacity;   % capacity of hydropower plant
    end
    methods(Access=public)
        function obj = HydroEnergy(cell,system)
            obj = obj@Behavior('HydroEnergy', ...
                ['Gets the energy generated ' ...
                'by a hydropower station annually. '], ...
                'kWh','[0,inf)');   
            obj.cell = cell;
            obj.system = system;
        end
    end
    methods(Access=protected)
        function val = EvaluateImpl(obj)
            val = 0; % initialize value
            plantCapacity = 0;
            for i=1:length(obj.system.nodes) % for each node in specified system:
                node = obj.system.nodes(i);
                if eq(node.cell,obj.cell) && strcmp(node.type.name,'Hydro Station')
                    % if node has specified cell and  node type is Hydro Station
                    % retrieve attributes from the synthesis template
                    
                    head = node.GetNodeTypeAttributeValue('Head');
                    inletSpeed = node.GetNodeTypeAttributeValue('Inlet water speed');
                    outletSpeed = node.GetNodeTypeAttributeValue('Outlet water speed');
                    flowRate = node.GetNodeTypeAttributeValue('Volumetric flow rate');
                    efficiency = node.GetNodeTypeAttributeValue('Efficiency');
                    capacityFactor = node.GetNodeTypeAttributeValue('Capacity Factor');
                    
                    plantCapacity = (9.81*head + 0.5*(inletSpeed^2 - outletSpeed^2))*flowRate*efficiency/1000;
                    val = plantCapacity*capacityFactor*8760/1000;
                    break % break out of for loop
                end
            end
            obj.capacity = plantCapacity;
        end
    end
end

