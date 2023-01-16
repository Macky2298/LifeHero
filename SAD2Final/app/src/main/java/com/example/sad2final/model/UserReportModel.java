package com.example.sad2final.model;

public class UserReportModel {

    private String date;
    private String address;
    private String location;
    private String department;

    public UserReportModel() {}

    public UserReportModel(String date, String address, String location, String department) {
        this.date = date;
        this.address = address;
        this.location = location;
        this.department = department;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public String getDepartment() {
        return department;
    }

}
