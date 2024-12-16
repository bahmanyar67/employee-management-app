package com.shahla.ema;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Utilities {
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }

    private static boolean validateFieldOnMainThread(Handler mainHandler, TextInputEditText field, String errorMessage) {
        String text = field.getText().toString().trim();
        if (text.isEmpty()) {
            mainHandler.post(() -> field.setError(errorMessage));
            return false;
        }
        return true;
    }

    public static CompletableFuture<Boolean> validate(Context context,
                                                      TextInputEditText firstNameField,
                                                      TextInputEditText lastNameField,
                                                      TextInputEditText emailField,
                                                      TextInputEditText passwordField,
                                                      TextInputEditText passwordRepeatField,
                                                      TextInputEditText departmentField,
                                                      TextInputEditText salaryField,
                                                      TextInputEditText joiningDateField,
                                                      TextInputEditText allowedLeavesField,
                                                      Employee employee) {
        return CompletableFuture.supplyAsync(() -> {
            AtomicBoolean isValid = new AtomicBoolean(true);
            Handler mainHandler = new Handler(Looper.getMainLooper());

            isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,firstNameField, "First Name is required"));
            isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,lastNameField, "Last Name is required"));
            isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,emailField, "Email is required"));
            if (employee == null) {
                isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,passwordField, "Password is required"));
            }

            if (joiningDateField != null) {
                isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,joiningDateField, "Joining Date is required"));
                if (!Utilities.isValidDate(joiningDateField.getText().toString())) {
                    joiningDateField.setError("Invalid Date");
                    isValid.set(false);
                }
            }

            if (allowedLeavesField != null) {
                isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,allowedLeavesField, "Allowed Leaves is required"));
            }

            if (passwordRepeatField != null) {
                isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,passwordRepeatField, "Repeat Password is required"));
                if (!passwordField.getText().toString().equals(passwordRepeatField.getText().toString())) {
                    passwordRepeatField.setError("Passwords do not match");
                    isValid.set(false);
                }
            }

            isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,departmentField, "Department is required"));
            if (salaryField != null) {
                isValid.set(isValid.get() & Utilities.validateFieldOnMainThread(mainHandler,salaryField, "Salary is required"));
            }

            if (!Utilities.isValidEmail(emailField.getText().toString())) {
                emailField.setError("Invalid Email Address");
                isValid.set(false);
            }

            if (employee == null || !passwordField.getText().toString().isEmpty()) {
                if (!Utilities.isValidPassword(passwordField.getText().toString())) {
                    passwordField.setError("Password is not secure");
                    isValid.set(false);
                }
            }
            Log.d("Validation", "Field validation result: " + isValid.get());
            return isValid.get();
        }).thenCompose(isValid -> {
            if (!isValid) {
                return CompletableFuture.completedFuture(false);
            }

            // Database check
            UserDao userDao = new UserDao(context);
            if (!userDao.isEmailUnique(emailField.getText().toString(), employee)) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> emailField.setError("Email already exists in the database"));
                return CompletableFuture.completedFuture(false);
            }

            Log.d("Validation", "after database check");

            return CompletableFuture.completedFuture(true);
        });
    }

}
