package com.shahla.ema;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {

    private Integer id;
    private String email;
    private String password;
    private String userType;
    private String name;
    private String position;
    private Double Salary;
    private LocalDate employmentDate;
    private Integer TakenDays;
    private Integer RemainingDays;

    public User(Integer id, String email, String password, String userType, String name, String position, Double salary,
                LocalDate employmentDate, Integer TakenDays, Integer RemainingDays) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.name = name;
        this.position = position;
        this.Salary = salary;
        this.employmentDate = employmentDate;
        this.TakenDays = TakenDays;
        this.RemainingDays = RemainingDays;
    }

    public User(Integer id, String email, String password, String userType, String name, String position,
                Double salary, LocalDate employmentDate) {
        this(id, email, password, userType, name, position, salary, employmentDate, 0, 20);
    }

    // Constructor with default values for takenDays and remainingDays
    public User(Integer id, String email, String password, String userType, String name, String position) {
        this(id, email, password, userType, name, position, 0.00, LocalDate.now(), 0, 20);
    }


    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public Double getSalary() {
        return Salary;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public Integer getTakenDays() {
        return TakenDays;
    }

    public Integer getRemainingDays() {
        return RemainingDays;
    }

    public void setSalary(Double salary) {
        this.Salary = salary;
    }

    public void setTakenDays(Integer takenDays) {
        this.TakenDays = takenDays;
    }

    public void setRemainingDays(Integer remainingDays) {
        this.RemainingDays = remainingDays;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}