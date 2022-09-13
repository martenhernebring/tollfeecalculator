package se.hernebring.tollfeecalculator;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TollCalculator {
    public int getTollFee(Vehicle vehicle, List<LocalDateTime> dates) {
        LocalDateTime intervalStart = dates.get(0);
        int totalFee = 0;
        for (LocalDateTime date : dates) {
            int nextFee = getTollFee(date, vehicle);
            int tempFee = getTollFee(intervalStart, vehicle);
            long minutes = ChronoUnit.MINUTES.between(intervalStart, date);

            if (minutes <= 60) {
                if (totalFee > 0) 
                    totalFee -= tempFee;
                if (nextFee >= tempFee) 
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

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) 
            return false;
        String vehicleType = vehicle.getVehicle();
        return vehicleType.equals(TollFreeVehicles.Motorbike.toString()) ||
               vehicleType.equals(TollFreeVehicles.Tractor.toString()) ||
               vehicleType.equals(TollFreeVehicles.Emergency.toString()) ||
               vehicleType.equals(TollFreeVehicles.Diplomat.toString()) ||
               vehicleType.equals(TollFreeVehicles.Bus.toString()) ||
               vehicleType.equals(TollFreeVehicles.Military.toString());
    }

    public int getTollFee(LocalDateTime date, Vehicle vehicle) {
        if (TollDate.isFree(date) || isTollFreeVehicle(vehicle))
            return 0;

        int hour = date.getHour();
        int minute = date.getMinute();

        if (hour == 6 && minute >= 0 && minute <= 29) 
            return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) 
            return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) 
            return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) 
            return 13;
        else if (hour >= 8 && hour <= 14 && minute >= 30 && minute <= 59) 
            return 8;
        else if (hour == 15 && minute >= 0 && minute <= 29) 
            return 13;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) 
            return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) 
            return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) 
            return 8;
        else return 0;
    }

}

enum TollFreeVehicles {
    Motorbike,
    Tractor,
    Emergency,
    Diplomat,
    Bus,
    Military
}

