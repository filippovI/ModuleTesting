package edu.innotech;

import edu.innotech.classes.Student;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;


public class StudentApiTest {

    private final static String BASE_STUDENT_URL = "http://localhost:8080/student/";
    private final static String BASE_TOP_STUDENT_URL = "http://localhost:8080/topStudent/";
    private final static ArrayList<Integer> MARKS = new ArrayList<>(List.of(2, 3, 4));
    private boolean deleteFlag = true; // По умолчанию удаляем
    private static int ID = 1;

    @AfterEach
    public void deleteStudent() {
        if (deleteFlag) {
            while (ID > 1) {
                deleteStudent(ID--);
            }
        } else
            deleteFlag = true;
    }

    @Test
    @DisplayName("Создание студента с id и именем")
    public void createStudentWithIdAndName() {
        createStudent(++ID, "Ivan")
                .then()
                .statusCode(201)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Создание студента только с именем")
    public void createStudentWithNameAndWithoutId() {
        deleteFlag = false;
        String id = createStudent("Igor")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body(matchesPattern("^\\d+$"))
                .extract()
                .body()
                .asString();
        getStudent(Integer.parseInt(id))
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("Igor"))
                .body("id", Matchers.equalTo(Integer.parseInt(id)));
        deleteStudent(Integer.parseInt(id));
    }

    @Test
    @DisplayName("Создание студента только с id")
    public void createStudentWithIdAndWithoutName() {
        createStudent(++ID, null, null)
                .then()
                .statusCode(400)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Создание студента с id, именем и оценками")
    public void createStudentWithIdAndNameAndMarks() {
        createStudent(++ID, "Igor", MARKS)
                .then()
                .statusCode(201)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Создание студента только с именем и оценками")
    public void createStudentWithNameAndMarksAndWithoutID() {
        deleteFlag = false;
        String id = createStudent("Igor", MARKS)
                .then()
                .statusCode(201)
                .body(matchesPattern("^\\d+$"))
                .extract()
                .asString();
        deleteStudent(Integer.parseInt(id));
    }

    @Test
    @DisplayName("Удаление существующего студента")
    public void deleteExistStudent() {
        deleteFlag = false;
        createStudent(ID, "Jack", MARKS);
        deleteStudent(ID)
                .then()
                .statusCode(200)
                .body(is(emptyString()));
        getStudent(ID)
                .then()
                .statusCode(404)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Удаление несуществующего студента")
    public void deleteNotExistStudent() {
        deleteFlag = false;
        deleteStudent(ID)
                .then()
                .statusCode(404)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Обновление студента")
    public void updateStudent() {
        ArrayList<Integer> marks = new ArrayList<>(List.of(3, 1, 2));
        createStudent(++ID, "Petr", MARKS);
        createStudent(ID, "Valera", marks);
        getStudent(ID)
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo("Valera"))
                .body("marks", Matchers.equalTo(marks))
                .body("id", Matchers.equalTo(ID));
    }

    @Test
    @DisplayName("Получение существующего студента с оценками")
    public void getExistStudentWithMarks() {
        createStudent(++ID, "Sergey", MARKS);
        getStudent(ID)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.equalTo(ID))
                .body("name", Matchers.equalTo("Sergey"))
                .body("marks", Matchers.equalTo(MARKS));
    }

    @Test
    @DisplayName("Получение существующего студента без оценок")
    public void getExistStudentWithoutMarks() {
        createStudent(++ID, "Mark");
        getStudent(ID)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.equalTo(ID))
                .body("name", Matchers.equalTo("Mark"))
                .body("marks", hasSize(0));
    }

    @Test
    @DisplayName("Получение несуществующего студента")
    public void getNotExistStudent() {
        deleteFlag = false;
        getStudent(ID)
                .then()
                .statusCode(404)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Получение топ студентов из пустой БД")
    public void getTopStudentWithEmptyDb() {
        deleteFlag = false;
        getTopStudent()
                .then()
                .statusCode(200)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Получение топ оценок из БД без оценок")
    public void getTopStudentWithEmptyMarks() {
        createStudent(++ID, "Kirill");
        createStudent(ID, "Mike");
        getTopStudent()
                .then()
                .statusCode(200)
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("Получение топ студентов, если есть студенты с одинаковым средним баллом и количеством оценок")
    public void getTopStudentsWithMaxAverageMarksAndCount() {
        createStudent(++ID, "Holly", MARKS);
        createStudent(++ID, "Molly", MARKS);
        createStudent(++ID, "Polly", MARKS);
        createStudent(++ID, "Dolly", new ArrayList<>(List.of(2, 3, 3, 1)));
        List<Student> students = getTopStudent()
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<Student>>() {
                });

        HashSet<ArrayList<Integer>> sumAndCountMarksSet = new HashSet<>();
        for (Student student : students) {
            sumAndCountMarksSet.add(new ArrayList<>(List.of(
                    student.getMarks().size(),
                    student.getMarks().stream().mapToInt(Integer::intValue).sum())));
        }
        Assertions.assertTrue(students.size() >= 3);
        Assertions.assertTrue(sumAndCountMarksSet.contains(List.of(3, 9)));
        Assertions.assertEquals(1, sumAndCountMarksSet.size());
    }

    @Test
    @DisplayName("Получение топ студента, если у него максимальный средний балл или больше всего оценок")
    public void getTopStudentWithMaxAverageMarksAndCount() {
        createStudent(++ID, "Holly", new ArrayList<>(List.of(5)));
        createStudent(++ID, "Molly", new ArrayList<>(List.of(5, 5)));
        createStudent(++ID, "Polly", MARKS);
        createStudent(++ID, "Dolly", new ArrayList<>(List.of(2, 3, 3, 1)));
        List<Student> students = getTopStudent()
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<Student>>() {
                });
        System.out.println(students);

        Assertions.assertEquals(1, students.size());
        Assertions.assertEquals(students.get(0).getMarks(), List.of(5, 5));
    }

    private Response createStudent(Integer id, String name, ArrayList<Integer> marks) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new Student(id, name, marks))
                .when()
                .post(BASE_STUDENT_URL);
    }

    private Response createStudent(String name, ArrayList<Integer> marks) {
        return createStudent(null, name, marks);
    }

    private Response createStudent(int id, String name) {
        return createStudent(id, name, null);
    }

    private Response createStudent(String name) {
        return createStudent(null, name, null);
    }

    private Response deleteStudent(int id) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete(BASE_STUDENT_URL + id);
    }

    private Response getStudent(int id) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_STUDENT_URL + id);
    }

    private Response getTopStudent() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_TOP_STUDENT_URL);
    }
}