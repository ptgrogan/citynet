% %% CSPEnergy Class Definition
% The PVStationEnergy behaviour calculates the electricity generated annually 
% in the PVstation
%
% 25 July 2011
% Damola Adepetu, aadepetu@masdar.ac.ae
%%
classdef CSPEnergy < Behavior
    properties
        cell;       % handle of the cell in which to evaluate the behavior
        system;     % handle of the energy system
        capacity;   % capacity of CSP power plant
    end
    methods(Access=public)
        function obj = CSPEnergy(cell,system)
            obj = obj@Behavior('CSP Energy', ...
                ['Gets the energy generated ' ...
                'by a CSP station annually. '], ...
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
                if eq(node.cell,obj.cell) && strcmp(node.type.name,'PV Station')
                    % if node has specified cell and  node type is PV Station
                    % retrieve attributes from the synthesis template
             
                    mirrorLength = node.GetNodeTypeAttributeValue('Mirror Length');
                    mirrorWidth = node.GetNodeTypeAttributeValue('Mirror Width');
                    numberMirrors = node.GetNodeTypeAttributeValue('Number of mirrors');
                    efficiency = node.GetNodeTypeAttributeValue('Solar-to-Electric Efficiency');
                    mirrorCapacity = node.GetNodeTypeAttributeValue('Capacity per mirror');
                    annualDNI = node.GetNodeTypeAttributeValue('Annual DNI');
                    val = annualDNI*efficiency*mirrorWidth*mirrorLength*numberMirrors;
                    plantCapacity = mirrorCapacity*numberMirrors;
                    
                    break % break out of for loop
                end
            end  
            obj.capacity = plantCapacity;
        end
    end
end
