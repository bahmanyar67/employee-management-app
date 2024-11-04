package com.shahla.ema;

import java.time.LocalDate;

public class HolidayRequest {
    public enum Status {
        APPROVED,
        DECLINED,
        WAITING
    }

    private User employee;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String note;
    private Status status;

    public HolidayRequest(User employee, LocalDate fromDate, LocalDate toDate, String note, Status status) {
        this.employee = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.note = note;
        this.status = status;
    }

    public User getEmployee() {
        return employee;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public String getNote() {
        return note;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}