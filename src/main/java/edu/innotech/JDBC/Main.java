package edu.innotech.JDBC;

public class Main {

    public static void main(String[] args) {
        Employees employees = new Employees("./Office");
        employees.moveAnnToHR();
        employees.employeesItCount();
        employees.updateNameIfLowRegister();
    }
}
