package edu.innotech.JDCB;

import lombok.Getter;

@Getter
public enum DbQueries {
    FIND_ANN("SELECT * FROM Employee WHERE NAME = 'Ann'"),
    FIND_LOW_REGISTER_NAME("SELECT * FROM Employee WHERE NAME ~ '^[a-zа-яё]'"),
    FIND_EMPLOYEES_FROM_IT("SELECT * " +
            "FROM Employee e " +
            "JOIN Department d " +
            "WHERE e.DepartmentID = d.ID " +
            "AND d.Name = 'IT' ");

    private final String query;

    DbQueries(String query) {
        this.query = query;
    }
}
