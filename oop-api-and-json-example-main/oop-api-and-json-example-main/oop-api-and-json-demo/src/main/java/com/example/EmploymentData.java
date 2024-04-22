package com.example;

public class EmploymentData {
    private double year;
    private double employmentRate;

    public EmploymentData(double year, double employmentRate) {
        this.year = year;
        this.employmentRate = employmentRate;
    }
    public double getYear() {
        return year;
    }
    public void setYear(double year) {
        this.year = year;
    }
    public double getEmploymentRate() {
        return employmentRate;
    }
    public void setEmploymentRate(double employmentRate) {
        this.employmentRate = employmentRate;
    }
    
}
