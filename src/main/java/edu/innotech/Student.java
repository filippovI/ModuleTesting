package edu.innotech;

import lombok.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Student {

    @Getter
    @Setter
    private String name;
    private List<Integer> grades = new ArrayList<>();
    private final CloseableHttpClient httpClient;

    public Student(String name) {
        this(name, HttpClients.createDefault()); // Используем стандартный клиент по умолчанию
    }

    // Конструктор для инъекции (использования в тестах)
    public Student(String name, CloseableHttpClient httpClient) {
        this.name = name;
        this.httpClient = httpClient;
    }

    public List<Integer> getGrades() {
        return new ArrayList<>(grades);
    }

    @SneakyThrows
    public void addGrade(int grade) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:5352/checkGrade?grade=" + grade);
        CloseableHttpResponse httpResponse = this.httpClient.execute(request);
        HttpEntity entity = (HttpEntity) httpResponse.getEntity();
        if (!Boolean.parseBoolean(EntityUtils.toString(entity))) {
            throw new IllegalArgumentException(grade + " is wrong grade");
        }
        grades.add(grade);
    }

    @SneakyThrows
    public int raiting() {
        HttpGet request = new HttpGet("http://localhost:5352/educ?sum=" + grades.stream().mapToInt(x -> x).sum());
        try (CloseableHttpResponse httpResponse = this.httpClient.execute(request)) {
            HttpEntity entity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(entity);
            return Integer.parseInt(responseBody);
        }
    }
}
