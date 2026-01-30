package edu.innotech;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentTest {

    private CloseableHttpClient mockHttpClient;
    private Student student;

    @BeforeEach
    void setUp() {
        mockHttpClient = Mockito.mock(CloseableHttpClient.class);
        student = new Student("Alice", mockHttpClient);
    }

    private CloseableHttpResponse createFullyConfiguredMockHttpResponse(String responseBody) throws IOException {
        CloseableHttpResponse mockHttpResponse = Mockito.mock(CloseableHttpResponse.class);
        HttpEntity mockHttpEntity = Mockito.mock(HttpEntity.class);
        InputStream stream = new ByteArrayInputStream(responseBody.getBytes());
        when(mockHttpEntity.getContent()).thenReturn(stream);
        when(mockHttpResponse.getEntity()).thenReturn(mockHttpEntity);
        return mockHttpResponse;
    }

    @ParameterizedTest(name = "Добавляем валидные оценки {0}")
    @ValueSource(ints = {2, 3, 4, 5})
    void addGradeAndValidGradeAndShouldAddToGradesList(Integer validGrade) throws IOException {
        CloseableHttpResponse configuredResponse = createFullyConfiguredMockHttpResponse("true");
        when(mockHttpClient.execute(any(HttpGet.class)))
                .thenReturn(configuredResponse);
        student.addGrade(validGrade);
        assertTrue(student.getGrades().contains(validGrade),
                "Список оценок должен содержать правильную оценку");
        assertEquals(1, student.getGrades().size(),
                "Список оценок должен содержать только одну оценку");
        verify(mockHttpClient, times(1)).execute(any(HttpGet.class));
    }

    @ParameterizedTest(name = "Добавляем невалидные оценки {0}")
    @ValueSource(ints = {1, 6, -1, 99})
    void addGradeAndInvalidGradeAndShouldThrowExceptionAndNotAddToGradesList(Integer invalidGrade) throws IOException {
        CloseableHttpResponse configuredResponse = createFullyConfiguredMockHttpResponse("false");
        when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(configuredResponse);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                        student.addGrade(invalidGrade),
                "Должно быть выброшено IllegalArgumentException для неправильной оценки");
        assertTrue(exception.getMessage().contains(String.valueOf(invalidGrade)),
                "Сообщение исключения должно содержать неправильную оценку");
        assertTrue(student.getGrades().isEmpty(),
                "Список оценок должен быть пустым после попытки добавить неправильную оценку");
        verify(mockHttpClient, times(1)).execute(any(HttpGet.class));
    }
}