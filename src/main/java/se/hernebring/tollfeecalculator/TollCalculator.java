package se.hernebring.tollfeecalculator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TollCalculator {
    public static int getTollFee(Vehicle vehicle, List<LocalDateTime> dates) {
        if(vehicle.isTollFree())
            return 0;
        LocalDateTime intervalStart = dates.get(0);
        int totalFee = 0;
        for (LocalDateTime date : dates) {
            int nextFee = getTollFee(date);
            int tempFee = getTollFee(intervalStart);
            long minutes = ChronoUnit.MINUTES.between(intervalStart, date);

            if (minutes <= 60) {
                if (totalFee > 0)
                    totalFee -= tempFee;
                if (nextFee > tempFee)
                    tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
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
