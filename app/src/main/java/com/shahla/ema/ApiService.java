package com.shahla.ema;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApiService {
    private static final String BASE_URL = "http://10.0.2.2:5000";
    private RequestQueue requestQueue;
    private Gson gson;


    public ApiService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public void getAllEmployees(Response.Listener<List<Employee>> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/employees";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Type listType = new TypeToken<List<Employee>>() {
                    }.getType();
                    List<Employee> employees = gson.fromJson(response.toString(), listType);
                    listener.onResponse(employees);
                }, errorListener);
        requestQueue.add(request);
    }

    // getMyEmployees method should get an array of employees id as a parameter and only return those matching the id
    public void getMyEmployees(int[] ids, Response.Listener<List<Employee>> listener, Response.ErrorListener errorListener) {
        this.getAllEmployees(employees -> {
            List<Employee> myEmployees = new ArrayList<>();
            for (Employee employee : employees) {
                for (int id : ids) {
                    if (employee.getId() == id) {
                        myEmployees.add(employee);
                        break;
                    }
                }
            }
            listener.onResponse(myEmployees);
        }, errorListener);
    }

    public void getEmployeeById(int id, Response.Listener<Employee> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/employees/get/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Employee employee = gson.fromJson(response.toString(), Employee.class);
                    listener.onResponse(employee);
                }, errorListener);
        requestQueue.add(request);
    }

    public void addEmployee(Employee employee, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/employees/add";
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(employee));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, listener, errorListener);
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(int id, Employee employee, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/employees/edit/" + id;
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(employee));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, listener, errorListener);
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int id, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "/employees/delete/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, listener, errorListener);
        requestQueue.add(request);
    }

    public void isEmailUnique(String email, Employee e, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        getAllEmployees(employees -> {
            // if the email is the same as the employee's current email, it is okay
            if (e != null && e.getEmail().equals(email)) {
                listener.onResponse(true);
                return;
            }

            // if the email is assigned to another employee, it is not unique
            boolean isUnique = employees.stream().noneMatch(item -> item.getEmail().equals(email));
            listener.onResponse(isUnique);
        }, errorListener);
    }

    public void getEmployeeIdByEmail(String email, Response.Listener<Integer> listener, Response.ErrorListener errorListener) {
        getAllEmployees(employees -> {
            int id = employees.stream()
                    .filter(item -> item.getEmail().equals(email))
                    .map(Employee::getId)
                    .findFirst()
                    .orElse(-1);
            listener.onResponse(id);
        }, errorListener);
    }


}