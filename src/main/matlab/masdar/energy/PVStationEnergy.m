% %% PVStationEnergy Class Definition
% The PVStationEnergy behaviour calculates the electricity generated annually 
% in the PVstation
%
% 25 July 2011
% Damola Adepetu, aadepetu@masdar.ac.ae
%%
classdef PVStationEnergy < Behavior
    properties
        cell;       % handle of the cell in which to evaluate the behavior
        system;     % handle of the energy system
    end
    methods(Access=public)
        function obj = PVStationEnergy(cell,system)
            obj = obj@Behavior('PV Station Energy', ...
                ['Gets the energy generated ' ...
                'by a PV station annually. '], ...
                'kWh','[0,inf)');   
            obj.cell = cell;
            obj.system = system;
        end
    end
    methods(Access=protected)
        function val = EvaluateImpl(obj)
            val = 0; % initialize value
            for i=1:length(obj.system.nodes) % for each node in specified system:
                node = obj.system.nodes(i);
                if eq(node.cell,obj.cell) && strcmp(node.type.name,'PV Station')
                    % if node has specified cell and  node type is PV Station
                    % retrieve attributes from the synthesis template
                    panelWidth = node.GetNodeTypeAttributeValue('Panel Width');
                    panelLength = node.GetNodeTypeAttributeValue('Panel Length');
                    panelEfficiency = node.GetNodeTypeAttributeValue('PV Panel Efficiency');
                    annualDNI = node.GetNodeTypeAttributeValue('Annual DNI');
                    numberPanels = node.GetNodeTypeAttributeValue('Number of panels');
                    val = annualDNI*panelEfficiency*panelWidth*panelLength*numberPanels;
                    break % break out of for loop
                end
            end  
        end
    end
end