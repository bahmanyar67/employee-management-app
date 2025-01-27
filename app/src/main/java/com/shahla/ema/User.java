package com.shahla.ema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;


public class User implements Serializable {

    @Expose
    private int id;

    @SerializedName("firstname")
    @Expose
    private String firstName;

    @SerializedName("lastname")
    @Expose
    private String lastName;
    @Expose
    private String email;
    @Expose (serialize = false, deserialize = false)
    private String password;
    @Expose
    private String department;
    @Expose
    private Double salary;

    private String userType;

    @SerializedName("joiningdate")
    @JsonAdapter(LocalDateAdapter.class)
    private LocalDate joiningDate;

    @Expose
    private Integer leaves;


    private boolean notificationsEnabled;

    // No-arg constructor required by Room
    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String department,
                Double salary, String userType, LocalDate joiningDate, Integer leaves) {
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
        if (joiningDate != null) {
            this.joiningDate = joiningDate;
        }
    }

    public Integer getLeaves() {
        return leaves;
    }

    public void setLeaves(Integer leaves) {
        this.leaves = leaves;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}


