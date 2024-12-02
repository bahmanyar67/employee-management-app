package com.shahla.ema;

import java.io.Serializable;
import java.time.LocalDate;



public class User implements Serializable {

    private int id;

    private String firstName;

    private String lastName;
    private String email;
    private String password;
    private String department;
    private Double salary;

    private String userType;

    private LocalDate joiningDate;

    private Integer leaves;

    // No-arg constructor required by Room
    public User() {}

    public User(String firstName, String lastName, String email, String password, String department, Double salary, String userType, LocalDate joiningDate, Integer leaves) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.salary = salary;
        this.userType = userType;
        this.joiningDate = joiningDate;
        this.leaves = leaves;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Integer getLeaves() {
        return leaves;
    }

    public void setLeaves(Integer leaves) {
        this.leaves = leaves;
    }
}