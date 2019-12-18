package com.example.metrobustracker.Model;

import java.sql.Date;

public class VehicleLocations {

    private double VehicleLongitude;
    private float Speed;
    //private Date LocationTime;
    private double VehicleLatitude;
    private long Odometer;
    private int BlockNumber;
    private int Direction;
    private String Terminal;
    private String Route;
    private float Bearing;

    public double getVehicleLongitude() {
        return VehicleLongitude;
    }

    public void setVehicleLongitude(double vehicleLongitude) {
        VehicleLongitude = vehicleLongitude;
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(float speed) {
        Speed = speed;
    }

    public double getVehicleLatitude() {
        return VehicleLatitude;
    }

    public void setVehicleLatitude(double vehicleLatitude) {
        VehicleLatitude = vehicleLatitude;
    }

    public long getOdometer() {
        return Odometer;
    }

    public void setOdometer(long odometer) {
        Odometer = odometer;
    }

    public int getBlockNumber() {
        return BlockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        BlockNumber = blockNumber;
    }

    public int getDirection() {
        return Direction;
    }

    public void setDirection(int direction) {
        Direction = direction;
    }

    public String getTerminal() {
        return Terminal;
    }

    public void setTerminal(String terminal) {
        Terminal = terminal;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public float getBearing() {
        return Bearing;
    }

    public void setBearing(float bearing) {
        Bearing = bearing;
    }

    @Override
    public String toString() {
        return "VehicleLocations{" +
                "VehicleLongitude='" + VehicleLongitude + '\'' +
                ", Speed='" + Speed + '\'' +
                ", VehicleLatitude='" + VehicleLatitude + '\'' +
                ", Odometer='" + Odometer + '\'' +
                ", BlockNumber='" + BlockNumber + '\'' +
                ", Direction='" + Direction + '\'' +
                ", Terminal='" + Terminal + '\'' +
                ", Route='" + Route + '\'' +
                ", Bearing='" + Bearing + '\'' +
                '}';
    }
}
