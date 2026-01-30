package edu.innotech.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Student {
    Integer id;
    String name;
    ArrayList<Integer> marks;

    public Student() {
    }

    public Student(Integer id, String name, ArrayList<Integer> marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }
}
