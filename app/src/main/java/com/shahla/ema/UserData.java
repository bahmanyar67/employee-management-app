package com.shahla.ema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    private static UserData instance;
    private List<User> userList;

    private UserData() {
        userList = new ArrayList<>();
        userList.add(new User(1, "shahla@employee.shah", "Shahla123!",
                "employee", "Shahla Bahmanyar", "Software Engineer", 18000.00, LocalDate.of(2020, 1, 1), 5, 15));
        userList.add(new User(2, "bob_jones@email.ltd", "RanDom2Pass!",
                "employee", "Bob Jones", "Product Manager", 25000.00, LocalDate.of(2019, 5, 1), 10, 10));
        userList.add(new User(3, "carol_brown@email.ltd", "RanDom3Pass!",
                "employee", "Carol Brown", "Data Analyst", 14000.00, LocalDate.of(2018, 3, 1), 15, 5));
        userList.add(new User(4, "dave_wilson@email.ltd", "RanDom4Pass!",
                "employee", "Dave Wilson", "HR Specialist", 17000.00, LocalDate.of(2024, 7, 1), 20, 0));
        userList.add(new User(5, "eve_martin@email.ltd", "RanDom5Pass!",
                "employee", "Eve Martin", "UI/UX Designer", 23000.00, LocalDate.of(2023, 9, 1), 0, 20));
        userList.add(new User(6, "frank_clark@email.ltd", "RanDom6Pass!",
                "employee", "Frank Clark", "Network Administrator", 19000.00, LocalDate.of(2022, 11, 1), 0, 20));
        userList.add(new User(7, "grace_lee@email.ltd", "RanDom7Pass!",
                "employee", "Grace Lee", "Marketing Specialist", 33000.00, LocalDate.of(2021, 1, 1), 0, 20));
        userList.add(new User(8, "harry_white@email.ltd", "RanDom8Pass!",
                "employee", "Harry White", "Database Administrator", 21000.00, LocalDate.of(2020, 3, 1), 0, 20));
        userList.add(new User(9, "ivy_green@email.ltd", "RanDom9Pass!",
                "employee", "Ivy Green", "Financial Analyst", 16500.00, LocalDate.of(2019, 5, 1), 0, 20));
        userList.add(new User(10, "jack_kelly@email.ltd", "RanDom10Pass!",
                "employee", "Jack Kelly", "Business Analyst", 15400.00, LocalDate.of(2018, 7, 1), 0, 20));


        // Add admins
        userList.add(new User(11, "shahla1@admin.shah", "Shahla123!", "admin", "Shahla Bahmanyar", "CEO at EMA"));
        userList.add(new User(12, "vivek@admin.shah", "Vivek123!", "admin", "Vivek Singh", "CTO at EMA"));

    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<User> getEmployees() {
        List<User> employees = new ArrayList<>();
        for (User user : userList) {
            if (user.getUserType().equals("employee")) {
                employees.add(user);
            }
        }
        return employees;
    }
}