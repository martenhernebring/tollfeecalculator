package se.hernebring.tollfeecalculator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TollDateTest {
    private final LocalTime lunch = LocalTime.of(12,54);

    @Test
    void workDayHasTollAtLunch() {
        LocalDate workDay = LocalDate.of(2022,9,12);
        LocalDateTime writtenIsTollDate = LocalDateTime.of(workDay, lunch);
        assertFalse(TollDate.isFree(writtenIsTollDate));
    }

    @Test
    void sundayHasNoToll() {
        LocalDate sunday = LocalDate.of(2022,9,18);
        LocalDateTime noTollDate = LocalDateTime.of(sunday, lunch);
        assertTrue(TollDate.isFree(noTollDate));
    }

    @Test
    void saturdayHasNoToll() {
        LocalDate saturday = LocalDate.of(2022,9,18);
        LocalDateTime noTollDate = LocalDateTime.of(saturday, lunch);
        assertTrue(TollDate.isFree(noTollDate));
    }

    @Test
    void workDayInJulyHasNoToll() {
        LocalDate workDayInJuly = LocalDate.of(2022,7,14);
        LocalDateTime noTollDate = LocalDateTime.of(workDayInJuly, lunch);
        assertTrue(TollDate.isFree(noTollDate));
    }

    @Test
    void holidayWeekdayOutsideJulyHasNoToll() {
        LocalDate holidayWeekdayOutsideJuly = LocalDate.of(2022,1,6);
        LocalDateTime noTollDate = LocalDateTime.of(holidayWeekdayOutsideJuly, lunch);
        assertTrue(TollDate.isFree(noTollDate));
    }

    @Test
    void weekdayBeforeHolidayWeekdayOutsideJulyHasNoToll() {
        LocalDate weekdayBeforeHolidayWeekdayOutsideJuly = LocalDate.of(2022,1,5);
        LocalDateTime noTollDate = LocalDateTime.of(weekdayBeforeHolidayWeekdayOutsideJuly, lunch);
        assertTrue(TollDate.isFree(noTollDate));
    }
}
