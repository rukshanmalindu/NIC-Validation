package com.example.nic.dto;

public class UserDTO {
    private String nicNumber;

    private String dob;
    private String gender;
    private int age;

    private long totalRecords;
    private long maleUsers;
    private long femaleUsers;

    // Getters and Setters

    public long getTotalRecords() {return totalRecords;}

    public void setTotalRecords(long totalRecords) {this.totalRecords = totalRecords;}

    public long getMaleUsers() {return maleUsers;}

    public void setMaleUsers(long maleUsers) {this.maleUsers = maleUsers;}

    public long getFemaleUsers() {return femaleUsers;}

    public void setFemaleUsers(long femaleUsers) {this.femaleUsers = femaleUsers;}

    public String getNicNumber() {return nicNumber;}

    public void setNicNumber(String nicNumber) {this.nicNumber = nicNumber;}
    public String getDob() {return dob;}

    public void setDob(String dob) {this.dob = dob;}

    public String getGender() {return gender;}

    public void setGender(String gender) {this.gender = gender;}

    public int getAge() {return age;}

    public void setAge(int age) {this.age = age;}
}




