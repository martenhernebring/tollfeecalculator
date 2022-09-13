package se.hernebring.tollfeecalculator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TollCalculatorTest {

    TollCalculator calculator = new TollCalculator();
    LocalTime lunch = LocalTime.of(12,54);

    @Test
    void workDayHasTollAtLunch() {
        LocalDate workDay = LocalDate.of(2022,9,12);
        LocalDateTime writtenIsTollDate = LocalDateTime.of(workDay, lunch);
        assertFalse(calculator.isTollFreeDate(writtenIsTollDate));
    }

    @Test
    void sundayHasNoToll() {
        LocalDate sunday = LocalDate.of(2022,9,18);
        LocalDateTime noTollDate = LocalDateTime.of(sunday, lunch);
        assertTrue(calculator.isTollFreeDate(noTollDate));
    }

    @Test
    void saturdayHasNoToll() {
        LocalDate saturday = LocalDate.of(2022,9,18);
        LocalDateTime noTollDate = LocalDateTime.of(saturday, lunch);
        assertTrue(calculator.isTollFreeDate(noTollDate));
    }

    @Test
    void workDayInJulyHasNoTollAtLunch() {
        LocalDate workDayInJuly = LocalDate.of(2022,7,14);
        LocalDateTime noTollDate = LocalDateTime.of(workDayInJuly, lunch);
        assertTrue(calculator.isTollFreeDate(noTollDate));
    }

    @Test
    void holidayWeekdayOutsideJulyHasNoToll() {
        LocalDate holidayWeekdayOutsideJuly = LocalDate.of(2022,1,6);
        LocalDateTime noTollDate = LocalDateTime.of(holidayWeekdayOutsideJuly, lunch);
        assertTrue(calculator.isTollFreeDate(noTollDate));
    }
}
