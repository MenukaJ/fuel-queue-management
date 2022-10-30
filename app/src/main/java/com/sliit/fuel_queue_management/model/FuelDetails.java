package com.sliit.fuel_queue_management.model;

public class FuelDetails {
    String id;
    String type;

    public FuelDetails() {

    }

    public FuelDetails(String id, String type) {
        this.id = id;
        this.type = type;
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
}
