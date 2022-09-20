package se.hernebring.tollfeecalculator;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

class TollDate {

    static boolean isFree(LocalDateTime date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY | date.getDayOfWeek() == DayOfWeek.SUNDAY)
            return true;

        if (date.getMonth() == Month.JULY)
            return true;

        HolidayManager m = HolidayManager.getInstance(HolidayCalendar.SWEDEN);
        if(m.isHoliday(LocalDate.from(date)))
            return true;
        else return m.isHoliday(LocalDate.from(date.plusDays(1L)));
    }
}
