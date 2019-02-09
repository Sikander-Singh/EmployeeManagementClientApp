package com.example.mangurkaur.employeeattendancepro.Activties;

public class TimeCardClass {


    private String TimeCardId;
    private String StartTime;
    private String EndTime;
    private String Date;
    private String EmpId;

    public String getTimeCardId() {
        return TimeCardId;
    }

    public void setTimeCardId(String timeCardId) {
        TimeCardId = timeCardId;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }
}
