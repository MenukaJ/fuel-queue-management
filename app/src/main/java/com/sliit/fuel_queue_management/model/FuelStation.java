package com.sliit.fuel_queue_management.model;

public class FuelStation {
    String id;
    String name;
    String addressLine3;

    public FuelStation() {

    }

    public FuelStation(String id, String name, String addressLine3) {
        this.id = id;
        this.name = name;
        this.addressLine3 = addressLine3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }
}
