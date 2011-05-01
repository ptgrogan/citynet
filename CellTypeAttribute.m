%% CellTypeAttribute Class Definition
% An instance of the CellTypeAttribute class is used to describe an aspect 
% of a particular CellType. Each CellTypeAttribute is given a name and 
% numeric value.
%
% 31-March 2011
% Paul Grogan, ptgrogan@mit.edu
%%
classdef CellTypeAttribute
    properties
        id;             % unique identifier of cell type attribute, integer
        name;           % name of cell type attribute, string
        description;    % description of cell type attribute, string
        units;          % units of attribute value, string
        bounds;         % allowable bounds on attribute value, string
        value;          % numerical value of cell attribute, double
    end
    methods
        %% CellTypeAttribute Constructor
        % Create a new instance of a CellTypeAttribute object and assign 
        % its name and value attributes
        %        
        % obj = CellTypeAttribute(id, name, description, rgbColor)
        %   id:             identifier of the cell type attribute (integer)
        %   name:           name of the cell type attribute (string)
        %   description:    description of the cell type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        %
        % obj = CellTypeAttribute(name, description, rgbColor)
        %   name:           name of the cell type attribute (string)
        %   description:    description of the cell type attribute (string)
        %   units:          units of attribute value (string)
        %   bounds:         allowable bounds on attribute value (string)
        %   value:          attribute value (-)
        % 
        % obj = CellTypeAttribute()
        
        function obj=CellTypeAttribute(varargin)
            if nargin==6
                obj.id = varargin{1};
                obj.name = varargin{2};
                obj.description = varargin{3};
                obj.units = varargin{4};
                obj.bounds = varargin{5};
                obj.value = varargin{6};
            elseif nargin==5
                obj.id = SynthesisTemplate.instance().GetNextCellTypeId();
                obj.name = varargin{1};
                obj.description = varargin{2};
                obj.units = varargin{3};
                obj.bounds = varargin{4};
                obj.value = varargin{5};
            else
                obj.id = SynthesisTemplate.instance().GetNextCellTypeId();
                obj.name = 'New Cell Type Attribute';
                obj.description = '';
                obj.units = '-';
                obj.bounds = '[0,0]';
                obj.value = 0;
            end
        end
    end
end