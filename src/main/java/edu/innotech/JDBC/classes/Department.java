package edu.innotech.JDBC.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Department {
    Integer ID;
    String Name;

    public Department(Integer ID, String name) {
        this.ID = ID;
        Name = name;
    }
}
