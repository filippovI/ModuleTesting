package edu.innotech.JDCB;

import edu.innotech.JDBC.Employees;
import edu.innotech.JDBC.db.DbClient;
import org.junit.jupiter.api.Test;

public class EmployeeTest {

    private static final String DB_URL = "mem:test_office;DB_CLOSE_DELAY=-1";


    public static void main(String[] args) {
        DbClient dbClient = new DbClient(DB_URL);
        dbClient.createDB();
    }

    public static void moveAnnToHRTest() {

        //dbClient.createDB();
        //employees.moveAnnToHR();


    }
}
