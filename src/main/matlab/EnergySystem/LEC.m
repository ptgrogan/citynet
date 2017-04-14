function k=LEC(Capex, OM, lifetime, energy, discount)
    top=0;
    bottom =0;
    
    for i=1:lifetime
       bottom =  bottom + energy/(1+discount)^i;
    end


    for i=0:lifetime
        if i==0
            top =  top + (Capex)/(1+discount)^i;     
        else
            top =  top + OM/(1+discount)^i;
        end
    end
    
    k=top/bottom*100;
end