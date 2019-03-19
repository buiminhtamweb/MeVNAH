package com.example.toant.googlemap.models;

import java.util.List;

public class TimeKm {

    public String status;
    public List<Rows> rows;
    public List<String> origin_addresses;
    public List<String> destination_addresses;

    public TimeKm(String status, List<Rows> rows, List<String> origin_addresses, List<String> destination_addresses) {
        this.status = status;
        this.rows = rows;
        this.origin_addresses = origin_addresses;
        this.destination_addresses = destination_addresses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }
}
