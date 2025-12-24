package edu.innotech;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tests {

    @Test
    public void testGetGrades() {
        List<Integer> expectedGrades = new ArrayList<>(List.of(2, 3));
        Student student = new Student("student");
        student.addGrade(2);
        student.addGrade(3);
        Assertions.assertEquals(expectedGrades, student.getGrades(), "Полученные оценки != исходным");
    }

    @Test
    public void testEncapsulationGrades() {
        Student student = new Student("student");
        student.addGrade(2);
        student.getGrades().add(5);
        Assertions.assertEquals(1, student.getGrades().size(), "Размер листа оценок != 1");
        Assertions.assertEquals(2, student.getGrades().get(0), "Оценка != 2");
    }

    @ParameterizedTest(name = "addSuccessGrade")
    @ValueSource(ints = {2, 3, 4, 5})
    public void testSuccessAddGrade(Integer arg) {
        List<Integer> expectedGrades = new ArrayList<>(List.of(arg));
        Student student = new Student("student");
        student.addGrade(arg);
        Assertions.assertEquals(expectedGrades, student.getGrades(), "Листа оценок не равен исходному");
    }

    @ParameterizedTest(name = "addUnsuccessGrade")
    @ValueSource(ints = {1, -1, 6, 100})
    public void testUnsuccessAddGrade(Integer arg) {
        Student student = new Student("student");
        Assertions.assertThrows(IllegalArgumentException.class, () -> student.addGrade(arg),
                "Была добавлена некорректная оценка");
    }

    @Test
    public void testGetName() {
        Student student = new Student("student");
        Assertions.assertEquals("student", student.getName(),
                "Полученное имя не совпадает с исходным");
    }

    @Test
    public void testSetName() {
        Student student = new Student("student");
        student.setName("Ivan");
        Assertions.assertEquals("Ivan", student.getName(),
                "Полученное имя не совпадает с установленным");
    }

    @Test
    public void testEquals() {
        Student student = new Student("student");
        Student student1 = new Student("student");
        Student student3 = null;
        String student4 = "Oleg";
        Student student5 = new Student("Gleb");
        Student student6 = student5;
        Assertions.assertEquals(false, student.equals(student4), "Объекты не должны быть равны");
        Assertions.assertEquals(student5, student6);
        Assertions.assertEquals(false, student.equals(student3), "Объекты не должны быть равны");
        student5.addGrade(2);
        student5.addGrade(5);
        Assertions.assertNotEquals(student, student5, "Объекты не должны быть равны");
        student.addGrade(2);
        student.addGrade(5);
        student1.addGrade(2);
        student1.addGrade(5);
        Assertions.assertEquals(student, student1, "Объекты должны быть равны");
        student1.addGrade(3);
        Assertions.assertNotEquals(student, student1, "Объекты не должны быть равны");
    }

    @ParameterizedTest(name = "getToString")
    @ValueSource(strings = {"Student{name=student, marks=[2]}"})
    public void testToSting(String arg) {
        Student student = new Student("student");
        student.addGrade(2);
        Assertions.assertEquals(arg, student.toString(), "Полученная строка не совпадает с ожидаемой");
    }

    @Test
    public void testHashCode() {
        Student student = new Student("student");
        student.addGrade(2);
        student.addGrade(5);
        String name = "student";
        List<Integer> grades = new ArrayList<>(List.of(2, 5));
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(name);
        hash = 13 * hash + Objects.hashCode(grades);
        Assertions.assertEquals(hash, student.hashCode(), "HashCode объектов должен совпадать");
        student.addGrade(3);
        Assertions.assertNotEquals(hash, student.hashCode(), "HashCode объектов не должен совпадать");
    }
}
