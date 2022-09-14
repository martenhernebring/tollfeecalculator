package se.hernebring.tollfeecalculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TollCalculatorTest {

    private List<LocalDateTime> traffic;
    private LocalDate workDate = LocalDate.of(2022,9,14);

    @BeforeEach
    void init() {
        traffic = new ArrayList<>();
    }

    @Test
    void ordinaryVehicleBefore6amOnWeekdayPaysNothing() {
        Vehicle car = new Vehicle(Type.CAR);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(5,59,59)));
        int forFree = TollCalculator.getTollFee(car, traffic);
        assertEquals(0, forFree);
    }

    @Test
    void ordinaryVehicleDuringLowTrafficOnWeekdayPays9Sek() {
        Vehicle smallBus = new Vehicle(Type.BUS);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(8,45,0)));
        int lowFee = TollCalculator.getTollFee(smallBus, traffic);
        assertEquals(9, lowFee);
    }
    
    @Test
    void specialVehicleAlwaysPays0Sek() {
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
        int forFree = TollCalculator.getTollFee(publicTransport, traffic);
        assertEquals(0, forFree);
    }

    @Test
    void ordinaryVehicleDuringMediumTrafficOnWeekdayPaysTheHigherAmountWhenPassingTwice() {
        Vehicle car = new Vehicle(Type.TRUCK);
        traffic.add(LocalDateTime.of(
                LocalDate.of(2022,9,14),
                LocalTime.of(6,29,59)
        ));
        traffic.add(LocalDateTime.of(
                LocalDate.of(2022,9,14),
                LocalTime.of(6,30,0)
        ));
        int mediumFee = TollCalculator.getTollFee(car, traffic);
        assertEquals(16, mediumFee);
    }

    @Test
    void ordinaryVehicleDuringHighAndMediumTrafficOnWeekdayWithOneHourBetweenPays38sek() {
        Vehicle car = new Vehicle(Type.CAR);
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(16,0,0)));
        traffic.add(LocalDateTime.of(workDate, LocalTime.of(17,30,0)));
        int highAndMedium = TollCalculator.getTollFee(car, traffic);
        assertEquals(38, highAndMedium);
    }

    @Test
    void ordinaryVehicleOnWeekdayPaysMaximum60sekPerDay() {
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
        int maxFee = TollCalculator.getTollFee(smallBus, traffic);
        assertEquals(60, maxFee);
    }

    @Test
    void ordinaryVehicleDuringDayBeforeHolidayPaysZero() {
        Vehicle car = new Vehicle(Type.TRUCK);
        traffic.add(LocalDateTime.of(
                LocalDate.of(2022,1,5),
                LocalTime.of(12,12,12)
        ));
        int forFree = TollCalculator.getTollFee(car, traffic);
        assertEquals(0, forFree);
    }

}
