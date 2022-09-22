package se.hernebring.tollfeecalculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TollCalculatorTest {

    private Set<LocalDateTime> traffic;
    
    private TollCalculator calculator;
    private final LocalDate workDate = LocalDate.of(2022,9,14);

    @BeforeEach
    void init() throws IOException {
        traffic = new HashSet<>();
        calculator = new TollCalculator();
    }

    @Test
    void ordinaryVehicleBefore6amOnWeekdayPaysNothing() throws IOException {
        Vehicle car = new Vehicle(Type.CAR);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(5,59,59)));
        int forFree = (int) new TollCalculator().getTollFee(car, traffic);
        assertEquals(0, forFree);
    }

    @Test
    void ordinaryVehicleDuringLowTrafficOnWeekdayPays9Sek() throws IOException {
        Vehicle smallBus = new Vehicle(Type.BUS);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(8,45,0)));
        int lowFee = (int) new TollCalculator().getTollFee(smallBus, traffic);
        assertEquals(9, lowFee);
    }
    
    @Test
    void specialVehicleAlwaysPays0Sek() throws IOException {
        Vehicle publicTransport = new Vehicle(Type.BUS);
        publicTransport.setTones(16.0);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,45,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(7,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(8,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(12,45,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(15,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(16,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(17,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(18,15,0)));
        int forFree = (int) new TollCalculator().getTollFee(publicTransport, traffic);
        assertEquals(0, forFree);
    }

    @Test
    void ordinaryVehicleDuringMediumTrafficOnWeekdayPaysTheHigherAmountWhenPassingTwice() throws IOException {
        Vehicle car = new Vehicle(Type.TRUCK);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,29,59)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,30,0)));
        int mediumFee = (int) new TollCalculator().getTollFee(car, traffic);
        assertEquals(16, mediumFee);
    }

    @Test
    void ordinaryVehicleDuringHighAndMediumTrafficOnWeekdayWithOneHourBetweenPays38sek() throws IOException {
        Vehicle car = new Vehicle(Type.CAR);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(17,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(16,0,0)));
        int highAndMedium = (int) new TollCalculator().getTollFee(car, traffic);
        assertEquals(38, highAndMedium);
    }

    @Test
    void ordinaryVehicleOnWeekdayPaysMaximum60sekPerDay() throws IOException {
        Vehicle smallBus = new Vehicle(Type.BUS);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,45,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(7,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(8,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(12,45,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(15,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(16,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(17,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(18,15,0)));
        int maxFee = (int) new TollCalculator().getTollFee(smallBus, traffic);
        assertEquals(60, maxFee);
    }

    @Test
    void ordinaryVehicleDuringDayBeforeHolidayPaysZero() throws IOException {
        Vehicle car = new Vehicle(Type.TRUCK);
        traffic.add(LocalDateTime.of(
                LocalDate.of(2022,1,5),
                LocalTime.of(12,12,12)
        ));
        int forFree = (int) new TollCalculator().getTollFee(car, traffic);
        assertEquals(0, forFree);
    }

    @Test
    void ordinaryVehicleEnteringThriceWithinASecondDuringLowTrafficOnWeekdayPays9Sek() throws IOException {
        Vehicle smallBus = new Vehicle(Type.BUS);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(8,45,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(8,45,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(8,45,0)));
        int lowFee = (int) new TollCalculator().getTollFee(smallBus, traffic);
        assertEquals(9, lowFee);
    }

    @Test
    void ordinaryVehicleInGothenburgEnteringTwiceWithinAHourDuringHighAndMediumTrafficOnWeekdayWithOneHourBetweenThemPays38sek() throws IOException {
        Vehicle car = new Vehicle(Type.CAR);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(18,29,59)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(16,0,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(16,59,59)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(17,30,0)));
        int highAndMedium = (int) new TollCalculator().getTollFee(car, traffic);
        assertEquals(38, highAndMedium);
    }

    @Test
    void ordinaryVehicleEnteringThriceWithinAHourDuringNoLowAndMediumTrafficOnWeekdayLowestFirstPays16sek() throws IOException {
        Vehicle car = new Vehicle(Type.CAR);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(5,58,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,32,59)));
        int high = (int) new TollCalculator().getTollFee(car, traffic);
        assertEquals(16, high);
    }

    @Test
    void ordinaryVehicleThriceOverAHourDuringNoLowAndMediumTrafficOnWeekdayWhereFirstIs0SekPays16sek() throws IOException {
        Vehicle car = new Vehicle(Type.CAR);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(5,30,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,15,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(6,45,59)));
        int high = (int) new TollCalculator().getTollFee(car, traffic);
        assertEquals(16, high);
    }

}
