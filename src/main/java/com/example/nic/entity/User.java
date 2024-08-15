package com.example.nic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nicNumber;
    private String fname;
    private String dob;
    private String gender;
    private int age;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getNicNumber() {

        return nicNumber;
    }

    public void setNicNumber(String nicNumber) {

        this.nicNumber = nicNumber;
    }

    public String getName() {

        return fname;
    }

    public void setName(String name) {

        this.fname = name;
    }

    public String getDob() {

        return dob;
    }

    public void setDob(String dob) {

        this.dob = dob;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {

        this.age = age;
    }
}

