function loadFlowText(BusData, GenData, BranchData)

fileID = fopen('case9_ies.m','w+');
fprintf(fileID, 'function mpc = case9_ies \n');
fprintf(fileID, 'mpc.version = ''2'' ; \n');
fprintf(fileID, 'mpc.baseMVA = 100 ; \n');
%input the bus details

fprintf(fileID, 'mpc.bus = [ \n');
[sizeBus,~]=size(BusData);

for i=1:sizeBus
%fprintf(fileID,'%2.0f %2.0f %8.2f %8.2f %1.0f %1.0f %6.4f %6.4f %1.0f %8.4f %6.4f %6.4f %6.4f;\n', BusData);
fprintf(fileID,[num2str(BusData(i,1)) ' ' num2str(BusData(i,2)) ' ' num2str(BusData(i,3)) ' ' num2str(BusData(i,4)) ' ' num2str(BusData(i,5)) ' ' num2str(BusData(i,6)) ' ']);
fprintf(fileID,[num2str(BusData(i,7)) ' ' num2str(BusData(i,8)) ' ' num2str(BusData(i,9)) ' ' num2str(BusData(i,10)) ' ' num2str(BusData(i,11)) ' ' num2str(BusData(i,12)) ' ' num2str(BusData(i,13)) ';\n']);
end
fprintf(fileID, '];\n');

%%input the generator details
fprintf(fileID, 'mpc.gen = [\n');
%fprintf(fileID,'%3.0f %8.2f %8.2f %8.2f %8.2f %2.0f %3.0f %2.0f %8.2f %8.2f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f %2.0f;\n', GenData);
[sizeGen,~]=size(GenData);

for i=1:sizeGen
fprintf(fileID,[num2str(GenData(i,1)) ' ' num2str(GenData(i,2)) ' ' num2str(GenData(i,3)) ' ' num2str(GenData(i,4)) ' ' num2str(GenData(i,5)) ' ' num2str(GenData(i,6)) ' ']);
fprintf(fileID,[num2str(GenData(i,7)) ' ' num2str(GenData(i,8)) ' ' num2str(GenData(i,9)) ' ' num2str(GenData(i,10)) ' ' num2str(GenData(i,11)) ' ' num2str(GenData(i,12)) ' ' num2str(GenData(i,13)) ' ']);
fprintf(fileID,[num2str(GenData(i,14)) ' ' num2str(GenData(i,15)) ' ' num2str(GenData(i,16)) ' ' num2str(GenData(i,17)) ' ' num2str(GenData(i,18)) ' ' num2str(GenData(i,19)) ' ']);
fprintf(fileID,[num2str(GenData(i,20)) ' ' num2str(GenData(i,21)) ';\n']);
end
fprintf(fileID, '];\n');

%%input the edge, i.e., powerline details
fprintf(fileID, 'mpc.branch = [ \n');
%fprintf(fileID,'%3.0f %3.0f %7.4f %7.4f %7.4f %3.0f %3.0f %3.0f %1.0f %1.0f %1.0f %4.0f %4.0f;\n', BranchData);
[sizeBranch,~] = size(BranchData);
for i=1:sizeBranch
fprintf(fileID,[num2str(BranchData(i,1)) ' ' num2str(BranchData(i,2)) ' ' num2str(BranchData(i,3)) ' ' num2str(BranchData(i,4)) ' ' num2str(BranchData(i,5)) ' ' num2str(BranchData(i,6)) ' ']);
fprintf(fileID,[num2str(BranchData(i,7)) ' ' num2str(BranchData(i,8)) ' ' num2str(BranchData(i,9)) ' ' num2str(BranchData(i,10)) ' ' num2str(BranchData(i,11)) ' ' num2str(BranchData(i,12)) ' ' num2str(BranchData(i,13)) ';\n']);
end

fprintf(fileID, '];\n');
fprintf(fileID, 'mpc.gencost = [ \n');
for i=1:sizeGen
fprintf(fileID, '2	1500	0	3	 0.11	5	150; \n');
end
fprintf(fileID, '];\n');
%Close m file
fclose(fileID);

end