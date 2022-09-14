package se.hernebring.tollfeecalculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void ordinaryVehicleDoesPayToll() {
        Vehicle car = new Vehicle(Type.CAR);
        Vehicle truck = new Vehicle(Type.TRUCK);
        truck.setTones(18.0);
        assertTrue(car.isToll());
        assertTrue(truck.isToll());
        assertNotEquals(car, truck);
    }

    @Test
    void specialVehiclesDoesNotPayToll() {
        Vehicle emergencyVan = new Vehicle(Type.EMERGENCY);
        Vehicle diplomatCar = new Vehicle(Type.DIPLOMAT);
        Vehicle handicappedCar = new Vehicle(Type.HANDICAP);
        Vehicle militaryTruck = new Vehicle(Type.MILITARY);
        Vehicle trailer = new Vehicle(Type.TRAILER);
        Vehicle tractor = new Vehicle(Type.TRACTOR);
        Vehicle heavyEquipment = new Vehicle(Type.HEAVY_EQUIPMENT);
        Vehicle motorbike = new Vehicle(Type.MOTORBIKE);
        assertFalse(emergencyVan.isToll());
        assertFalse(diplomatCar.isToll());
        assertFalse(handicappedCar.isToll());
        assertFalse(militaryTruck.isToll());
        assertFalse(trailer.isToll());
        assertFalse(tractor.isToll());
        assertFalse(heavyEquipment.isToll());
        assertFalse(motorbike.isToll());
    }

    @Test
    void busUnder14TonesDoesPayToll() {
        Vehicle smallBus = new Vehicle(Type.BUS);
        assertTrue(smallBus.isToll());
    }

    @Test
    void busWith14TonesOrMoreDoesNotPayToll() {
        Vehicle publicTransportBus = new Vehicle(Type.BUS);
        publicTransportBus.setTones(16.0);
        assertFalse(publicTransportBus.isToll());
    }
}
