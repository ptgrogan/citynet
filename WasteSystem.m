%% WasteSystem Class Definition
% An instance of the System class specifically for the waste system.
%
% 9-May 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef WasteSystem < System
    methods
        %% WasteSystem Constructor
        % Instantiates a new WasteSystem object with specified name and 
        % description.
        %
        % obj = WasteSystem(id, name, description)
        %   id:             unique identifier
        %   name:           system name (string)
        %   description:    system description (string)
        function obj = WasteSystem(varargin)
            obj = obj@System(varargin{:});
        end
    end
end