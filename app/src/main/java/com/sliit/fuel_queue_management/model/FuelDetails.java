package com.sliit.fuel_queue_management.model;

public class FuelDetails {
    String id;
    String type;
    String arrivalTime;
    String finishTime;
    String date;
    String status;
    FuelStation fuelStation;

    public FuelDetails() {

    }

    public FuelDetails(String id, String type, String arrivalTime, String finishTime, String date, String status, FuelStation fuelStation) {
        this.id = id;
        this.type = type;
        this.arrivalTime = arrivalTime;
        this.finishTime = finishTime;
        this.date = date;
        this.status = status;
        this.fuelStation = fuelStation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FuelStation getFuelStation() {
        return fuelStation;
    }

    public void setFuelStation(FuelStation fuelStation) {
        this.fuelStation = fuelStation;
    }
}
