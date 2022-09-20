package se.hernebring.tollfeecalculator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TollCalculator {
    public static int getTollFee(Vehicle vehicle, Set<LocalDateTime> dates) {
        if(vehicle.isTollFree() || dates.isEmpty())
            return 0;

        int totalFee = 0, previousFee, currentFee;
        LocalDateTime previous, current;
        Iterator<LocalDateTime> sortedIterator = new TreeSet<>(dates).iterator();
        do {
            previous = sortedIterator.next();
            previousFee = getTollFee(previous);
        } while (previousFee <= 0 && sortedIterator.hasNext());
        totalFee += previousFee;
        while(sortedIterator.hasNext()) {
            do {
                current = sortedIterator.next();
                currentFee = getTollFee(current);
            } while (currentFee <= 0 && sortedIterator.hasNext());
            var minutes = ChronoUnit.MINUTES.between(previous, current);
            if(minutes <= 60) {
                if(currentFee > previousFee) {
                    totalFee = totalFee + currentFee - previousFee;
                    previousFee = currentFee;
                }
            } else {
                totalFee += currentFee;
                previous = current;
                previousFee = currentFee;
            }
        }
        if (totalFee > 60)
            totalFee = 60;

        return totalFee;
    }

    private static int getTollFee(LocalDateTime date) {
        if (TollDate.isFree(date))
            return 0;

        int hour = date.getHour();
        int minute = date.getMinute();

        if (hour == 7 | hour == 16 | (hour == 15 && minute >= 30))
            return 22;
        else if ((hour == 6 && minute >= 30) |
                (hour == 8 && minute <= 29) |
                hour == 15 | hour == 17)
            return 16;
        else if ((hour >= 6 && hour <= 14) |
                (hour == 18 && minute <= 29))
            return 9;
        else return 0;
    }

}
