package edu.innotech;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class CheckGradeServiceStub {
    private HttpServer server;
    private final int port;

    public CheckGradeServiceStub(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/checkGrade", new CheckGradeHandler());
        server.setExecutor(null); // Используем стандартный исполнитель потоков
        server.start();
        System.out.println("edu.innotech.CheckGradeServiceStub started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0); // Остановка немедленно
            System.out.println("edu.innotech.CheckGradeServiceStub stopped.");
        }
    }

    static class CheckGradeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (!"GET".equals(requestMethod)) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            // Парсим параметры запроса
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            String gradeParam = queryParams.get("grade");

            String response = "false"; // По умолчанию считаем оценку неверной
            if (gradeParam != null) {
                try {
                    int grade = Integer.parseInt(gradeParam);
                    // Логика валидации: оценка от 1 до 5
                    if (grade >= 1 && grade <= 5) {
                        response = "true";
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid grade parameter: " + gradeParam);
                    // Возвращаем false для некорректного формата
                }
            }

            sendResponse(exchange, 200, response);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
            exchange.sendResponseHeaders(statusCode, responseBody.length());
            OutputStream os = exchange.getResponseBody();
            os.write(responseBody.getBytes());
            os.close();
        }

        private Map<String, String> parseQueryParams(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null || query.isEmpty()) {
                return result;
            }
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
            return result;
        }
    }
}
