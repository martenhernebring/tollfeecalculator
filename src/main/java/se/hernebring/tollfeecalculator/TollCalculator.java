package se.hernebring.tollfeecalculator;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
               vehicleType.equals(TollFreeVehicles.Foreign.toString()) ||
               vehicleType.equals(TollFreeVehicles.Military.toString());
    }

    public int getTollFee(LocalDateTime date, Vehicle vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) 
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

    boolean isTollFreeDate(LocalDateTime date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY | date.getDayOfWeek() == DayOfWeek.SUNDAY)
            return true;

        int month = date.getMonthValue();
        final int july = 7;
        if (month == july)
            return true;

        HolidayManager m = HolidayManager.getInstance(HolidayCalendar.SWEDEN);
        if(m.isHoliday(LocalDate.from(date)))
            return true;
        int year = date.getYear();
        int day = date.getDayOfMonth();

        if (year == 2013) {
            if (month == 1 && day == 1 ||
                month == 3 && (day == 28 || day == 29) ||
                month == 4 && (day == 1 || day == 30) ||
                month == 5 && (day == 1 || day == 8 || day == 9) ||
                month == 6 && (day == 5 || day == 6 || day == 21) ||
                month == 7 ||
                month == 11 && day == 1 ||
                month == 12 && (day == 24 || day == 25 || day == 26 || day == 31)
            )
                return true;
        }
        return false;
    }
}

enum TollFreeVehicles {
    Motorbike,
    Tractor,
    Emergency,
    Diplomat,
    Foreign,
    Military
}

