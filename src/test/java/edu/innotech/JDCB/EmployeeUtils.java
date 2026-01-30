package edu.innotech.JDCB;

import edu.innotech.JDBC.classes.Employee;

import java.util.List;

import static edu.innotech.JDBC.Employees.MAP_TO_EMPLOYEE;
import static edu.innotech.JDCB.EmployeeTest.DB_CLIENT;

public class EmployeeUtils {

    public static List<Employee> getLowRegisterEmployees() {
        return DB_CLIENT.select(DbQueries.FIND_LOW_REGISTER_NAME.getQuery(), MAP_TO_EMPLOYEE);
    }

    public static List<Employee> getAnnsEmployee() {
        return DB_CLIENT.select(DbQueries.FIND_ANN.getQuery(), MAP_TO_EMPLOYEE);
    }

    public static List<Employee> getEmployeesFromIt() {
        DB_CLIENT.createDB();
        return DB_CLIENT.select(DbQueries.FIND_EMPLOYEES_FROM_IT.getQuery(), MAP_TO_EMPLOYEE);
    }
}
