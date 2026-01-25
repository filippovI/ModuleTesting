package edu.innotech.JDBC;

import edu.innotech.JDBC.classes.Department;
import edu.innotech.JDBC.classes.Employee;
import edu.innotech.JDBC.db.DbClient;
import edu.innotech.JDBC.interfaces.RowMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Employees {

    private static final RowMapper<Employee> MAP_TO_EMPLOYEE = r -> new Employee(
            r.getInt("ID"),
            r.getString("Name"),
            r.getInt("DepartmentID"));
    private static final RowMapper<Department> MAP_TO_DEPARTMENT = r -> new Department(
            r.getInt("ID"),
            r.getString("Name"));
    private static final String DB_URL = "C:\\Users\\IvYFilippov\\Desktop\\Office\\Office";

    public static void main(String[] args) {
        moveAnnToHR();
        updateNameIfLowRegister();
        employeesItCount();
    }

    public static void moveAnnToHR() {
        DbClient db = new DbClient(DB_URL);
        List<Employee> annsList = db.select("SELECT * FROM Employee WHERE NAME = 'Ann'", MAP_TO_EMPLOYEE);
        if (annsList.size() == 1) {
            List<Department> hrDepartment = db.select("SELECT * FROM Department WHERE NAME = 'HR'", MAP_TO_DEPARTMENT);
            db.update("UPDATE Employee " +
                    "SET DepartmentID = ? " +
                    "WHERE ID = ?", hrDepartment.get(0).getID(), annsList.get(0).getID());
            System.out.println("Сотрудник " + annsList.get(0).getName() + " перемещен в HR отдел");
        }
    }

    public static void updateNameIfLowRegister() {
        DbClient db = new DbClient(DB_URL);
        List<Employee> employees = db.select("SELECT * FROM Employee", MAP_TO_EMPLOYEE)
                .stream()
                .filter(e -> Character.isLowerCase(e.getName().charAt(0)))
                .collect(Collectors.toList());
        employees.forEach(e -> e.setName(e.getName().substring(0, 1).toUpperCase() + e.getName().substring(1)));

        if (!employees.isEmpty()) {
            for (Employee e : employees) {
                db.update("UPDATE Employee " +
                        "SET NAME = ? " +
                        "WHERE ID = ?", e.getName(), e.getID());
            }
        }
        System.out.println(employees.size());
    }


    public static void employeesItCount() {
        DbClient db = new DbClient(DB_URL);
        List<Employee> employees = db.select("SELECT * " +
                "FROM Employee e " +
                "JOIN Department d " +
                "WHERE e.DepartmentID = d.ID " +
                "AND d.Name = 'IT' ", MAP_TO_EMPLOYEE);

        System.out.println(employees.size());
    }
}