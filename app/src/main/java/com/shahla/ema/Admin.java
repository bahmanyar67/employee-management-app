package com.shahla.ema;

import java.time.LocalDate;

public class Admin extends User {

    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, "Administrator", 0.0, "admin", LocalDate.now(), 0);
    }
}
