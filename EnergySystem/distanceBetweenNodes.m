function distance = distanceBetweenNodes(node1, node2)
    length = ceil(node1/10) - ceil(node2/10);
    width1 = mod(node1,10);
    width2 = mod(node2,10);

    if width1 == 0
        width1= 10;
    end
    
    if width2 == 0
        width2 = 10;
    end
    
    distance = 2.45/100*sqrt(length^2 + (width1-width2)^2);
end