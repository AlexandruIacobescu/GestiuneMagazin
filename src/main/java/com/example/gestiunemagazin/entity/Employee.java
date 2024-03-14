package com.example.gestiunemagazin.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleStringProperty firstname, lastname, department,email, eid, phoneNumber;
    private SimpleDoubleProperty salary;
    public Employee(String eid, String fnm, String lnm, String dept, Double sal, String email, String phone){
        this.eid = new SimpleStringProperty(eid);
        this.firstname = new SimpleStringProperty(fnm);
        this.lastname = new SimpleStringProperty(lnm);
        this.department = new SimpleStringProperty(dept);
        this.salary = new SimpleDoubleProperty(sal);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phone);
    }
    public String getEid(){return this.eid.get();}
    public String getFirstname(){return this.firstname.get();}
    public String getLastname(){return this.lastname.get();}
    public String getDepartment(){return this.department.get();}
    public double getSal(){return this.salary.get();}
    public String getEmail(){return this.email.get();}
    public String getPhoneNumber(){return this.phoneNumber.get();}
}