package com.shahla.ema;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    // get employees
    @Query("SELECT * FROM users WHERE user_type = 'employee'")
    List<User> getEmployees();

    @Query("SELECT * FROM users WHERE id = :id")
    User getEmployeeById(int id);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User findUserByEmail(String email);

}
