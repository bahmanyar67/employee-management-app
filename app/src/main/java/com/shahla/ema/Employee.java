package com.shahla.ema;

import java.time.LocalDate;

public class Employee extends User {

    public Employee(){
        super();
    }
    public Employee(String firstName, String lastName, String email, String password, String department, Double salary, LocalDate joiningDate, Integer leaves) {
        super(firstName, lastName, email, password, department, salary, "employee", joiningDate, leaves);
    }
}
