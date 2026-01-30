package edu.innotech.JDBC.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Employee {
    Integer ID;
    String Name;
    Integer DepartmentID;

    public Employee(Integer ID, String name, Integer departmentID) {
        this.ID = ID;
        Name = name;
        DepartmentID = departmentID;
    }
}
