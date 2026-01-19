package edu.innotech.JDBC;

import edu.innotech.JDBC.classes.Department;
import edu.innotech.JDBC.classes.Employee;
import edu.innotech.JDBC.db.DbClient;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Employees {
    private static final String DB_URL = "C:\\Users\\IvYFilippov\\Desktop\\Office\\Office";

    public static void main(String[] args) throws SQLException {
        moveAnnToHR();
        updateNameIfLowRegister();
    }

    public static void moveAnnToHR() {
        DbClient db = new DbClient(DB_URL);
        List<Employee> annsList = db.select("SELECT * FROM Employee WHERE NAME = 'Ann'",
                r -> new Employee(
                        r.getInt("ID"),
                        r.getString("Name"),
                        r.getInt("DepartmentID")));
        if (annsList.size() == 1) {
            List<Department> hrDepartment = db.select("SELECT * FROM Department WHERE NAME = 'HR'",
                    r -> new Department(r.getInt("ID"), r.getString("Name")));
            db.update("UPDATE Employee " +
                    "SET DepartmentID = ? " +
                    "WHERE ID = ?", hrDepartment.get(0).getID(), annsList.get(0).getID());
        }
    }

    public static void updateNameIfLowRegister() {
        DbClient db = new DbClient(DB_URL);
        List<Employee> employees = db.select("SELECT * FROM Employee",
                r -> new Employee(
                        r.getInt("ID"),
                        r.getString("Name"),
                        r.getInt("DepartmentID")))
                .stream()
                .filter(e -> Character.isLowerCase(e.getName().charAt(0)))
                .peek(e -> e.setName(e.getName().substring(0, 1).toUpperCase() + e.getName().substring(1)))
                .collect(Collectors.toList());
        for (Employee e : employees) {
            db.update("UPDATE Employee " +
                    "SET NAME = ? " +
                    "WHERE ID = ?", e.getName(), e.getID());
        }
    }

}
