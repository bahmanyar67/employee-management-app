package com.shahla.ema;

import java.time.LocalDate;

public class HolidayRequest {
    public enum Status {
        APPROVED,
        DECLINED,
        WAITING
    }

    private int id;
    private Employee employee;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String note;
    private Status status;
    private boolean shouldNotify;


    public HolidayRequest() {
    }

    public HolidayRequest(Employee employee, LocalDate fromDate, LocalDate toDate, String note, Status status) {
        this.employee = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.note = note;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isShouldNotify() {
        return shouldNotify;
    }

    public void setShouldNotify(boolean shouldNotify) {
        this.shouldNotify = shouldNotify;
    }
}