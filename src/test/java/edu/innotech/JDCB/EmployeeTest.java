package edu.innotech.JDCB;

import edu.innotech.JDBC.Employees;
import edu.innotech.JDBC.classes.Employee;
import edu.innotech.JDBC.db.DbClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static edu.innotech.JDBC.Employees.MAP_TO_EMPLOYEE;

public class EmployeeTest {

    private static final String DB_URL = "mem:test_office;DB_CLOSE_DELAY=-1";
    private static final Employees employees = new Employees(DB_URL);
    private static final DbClient dbClient = new DbClient(DB_URL);


    @Test
    @DisplayName("Перемещаем сотрудника Ann в HR отдел")
    public void moveAnnToHRTest() {
        dbClient.createDB();
        List<Employee> annsList = getAnnsEmployee();
        Assertions.assertTrue(annsList.size() == 1 && annsList.get(0).getDepartmentID() != 3);
        employees.moveAnnToHR();
        annsList = getAnnsEmployee();
        Assertions.assertTrue(annsList.size() == 1 && annsList.get(0).getDepartmentID() == 3);
    }

    @Test
    @DisplayName("Делаем большой первую буквы в имени, если она маленькая")
    public void updateNameIfLowRegisterTest() {
        dbClient.createDB();
        List<Employee> lowRegisterEmployeesList = getLowRegisterEmployees();
        if (lowRegisterEmployeesList.size() > 1) {
            employees.updateNameIfLowRegister();
            lowRegisterEmployeesList = getLowRegisterEmployees();
            Assertions.assertTrue(lowRegisterEmployeesList.isEmpty());
        }

    }

    @Test
    @DisplayName("Проверяем количество сотрудников в IT отделе")
    public void employeesItCountTest() {
        String count = String.valueOf(getEmployeesFromIt().size());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        employees.employeesItCount();
        Assertions.assertEquals(count, outputStream.toString().trim());
    }

    private List<Employee> getLowRegisterEmployees() {
        return dbClient.select("SELECT * FROM Employee WHERE NAME ~ '^[a-zа-яё]' ", MAP_TO_EMPLOYEE);
    }

    private List<Employee> getAnnsEmployee() {
        return dbClient.select("SELECT * FROM Employee WHERE NAME = 'Ann'", MAP_TO_EMPLOYEE);
    }

    private List<Employee> getEmployeesFromIt() {
        dbClient.createDB();
        return dbClient.select("SELECT * " +
                "FROM Employee e " +
                "JOIN Department d " +
                "WHERE e.DepartmentID = d.ID " +
                "AND d.Name = 'IT' ", MAP_TO_EMPLOYEE);
    }
}