package com.motorph;

/**
 * Employee.java - Data model for an employee record.
 * Stores all fields from the MotorPH employee CSV (FR-01).
 */
public class Employee {

    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String birthday;
    private String sssNumber;
    private String philHealthNumber;
    private String tin;
    private String pagIbigNumber;
    private String status;       // Regular / Probationary
    private String position;
    private double basicMonthlySalary;
    private double hourlyRate;

    // Constructor
    public Employee(String employeeNumber, String lastName, String firstName,
                    String birthday, String sssNumber, String philHealthNumber,
                    String tin, String pagIbigNumber, String status,
                    String position, double basicMonthlySalary, double hourlyRate) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.tin = tin;
        this.pagIbigNumber = pagIbigNumber;
        this.status = status;
        this.position = position;
        this.basicMonthlySalary = basicMonthlySalary;
        this.hourlyRate = hourlyRate;
    }

    // Getters
    public String getEmployeeNumber() { return employeeNumber; }
    public String getLastName()       { return lastName; }
    public String getFirstName()      { return firstName; }
    public String getBirthday()       { return birthday; }
    public String getSssNumber()      { return sssNumber; }
    public String getPhilHealthNumber() { return philHealthNumber; }
    public String getTin()            { return tin; }
    public String getPagIbigNumber()  { return pagIbigNumber; }
    public String getStatus()         { return status; }
    public String getPosition()       { return position; }
    public double getBasicMonthlySalary() { return basicMonthlySalary; }
    public double getHourlyRate()     { return hourlyRate; }

    public String getFullName() {
        return lastName + ", " + firstName;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | PHP %.2f/mo",
                employeeNumber, getFullName(), position, basicMonthlySalary);
    }
}
