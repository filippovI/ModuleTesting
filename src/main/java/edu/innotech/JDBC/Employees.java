package edu.innotech.JDBC;

import edu.innotech.JDBC.classes.Department;
import edu.innotech.JDBC.classes.Employee;
import edu.innotech.JDBC.db.DbClient;

import java.sql.SQLException;
import java.util.List;

public class Employees {
    private static final String DB_URL = "C:\\Users\\IvYFilippov\\Desktop\\Office\\Office";

    public static void main(String[] args) throws SQLException {
        moveAnnToHR();
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

}
