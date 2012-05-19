function A = dateFromHour(hour)
%A Code to calculate the day and month of the year based on the hours
%'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
time = mod(hour,24);
if time == 0
    time =24;
end
cumDays = ceil(hour/24);
Months =[31    59    90   120   151   181   212   243   273   304   334   365];
location = find(cumDays<=Months);
month=13-length(location);

if month ==1
    day = ceil(hour/24);
else
    day = ceil(hour/24) - Months(month-1);
end

A = [month, day, time];
end