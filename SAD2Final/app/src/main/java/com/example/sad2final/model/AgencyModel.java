package com.example.sad2final.model;

public class AgencyModel {

    private String department;
    private String address;
    private String contactNo;

    public AgencyModel() {}

    public AgencyModel(String department, String address, String contactNo) {
        this.department = department;
        this.address = address;
        this.contactNo = contactNo;
    }

    public String getDepartment() {
        return department;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNo() {
        return contactNo;
    }

}
