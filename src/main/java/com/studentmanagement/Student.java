package com.studentmanagement;

public class Student {

    private int id;
    private String name;
    private String department;
    private String email;
    private String phone;

    public Student(int id, String name, String department, String email, String phone) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }

    public Student(String name, String department, String email, String phone) {
        this(0, name, department, email, phone);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
