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

import static edu.innotech.JDCB.EmployeeUtils.*;


public class EmployeeTest {

    private static final String DB_URL = "mem:test_office;DB_CLOSE_DELAY=-1";
    private static final Employees EMPLOYEES = new Employees(DB_URL);
    static final DbClient DB_CLIENT = new DbClient(DB_URL);

    @Test
    @DisplayName("Перемещаем сотрудника Ann в HR отдел")
    public void moveAnnToHRTest() {
        DB_CLIENT.createDB();
        List<Employee> annsList = getAnnsEmployee();
        Assertions.assertTrue(annsList.size() == 1 && annsList.get(0).getDepartmentID() != 3,
                "Сотрудников Ann больше, чем 1 или сотрудник в HR департаменте");
        EMPLOYEES.moveAnnToHR();
        annsList = getAnnsEmployee();
        Assertions.assertTrue(annsList.size() == 1 && annsList.get(0).getDepartmentID() == 3,
                "Сотрудников Ann больше, чем 1 или сотрудник не в HR департаменте");
    }

    @Test
    @DisplayName("Делаем большой первую буквы в имени, если она маленькая")
    public void updateNameIfLowRegisterTest() {
        DB_CLIENT.createDB();
        List<Employee> lowRegisterEmployeesList = getLowRegisterEmployees();
        if (lowRegisterEmployeesList.size() > 1) {
            EMPLOYEES.updateNameIfLowRegister();
            lowRegisterEmployeesList = getLowRegisterEmployees();
            Assertions.assertTrue(lowRegisterEmployeesList.isEmpty(), "Есть имена с маленькой буквой");
        }
    }

    @Test
    @DisplayName("Проверяем количество сотрудников в IT отделе")
    public void employeesItCountTest() {
        String count = String.valueOf(getEmployeesFromIt().size());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        EMPLOYEES.employeesItCount();
        Assertions.assertEquals(count, outputStream.toString().trim());
    }
}