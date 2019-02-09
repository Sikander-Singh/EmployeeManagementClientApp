package com.example.mangurkaur.employeeattendancepro.Activties;

public class EmpClass {

    private String FirstName;
    private String LastName;
    private String Phone;
    private String Email;
    private String EmpId;
    private String Gender;
    private String dataId;
    private String TimeCardId;
    private String status;
    private String statusDate;

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getTimeCardId() {
        return TimeCardId;
    }

    public void setTimeCardId(String timeCardId) {
        TimeCardId = timeCardId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public EmpClass() {
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }



}
