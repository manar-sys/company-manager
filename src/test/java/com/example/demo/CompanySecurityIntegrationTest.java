package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.jayway.jsonpath.JsonPath;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CompanySecurityIntegrationTest {

    @SuppressWarnings({"resource", "unused"}) // Used indirectly by Spring/Testcontainers
    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.0")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void adminToken_canCreateUpdateDeleteCompany() throws Exception {
        String adminToken = loginAndGetToken("admin", "1234");

        Long companyId = createCompany(adminToken);
        assertNotNull(companyId);

        String updateJson = """
                {
                  "name": "%s",
                  "budget": %s
                }
                """.formatted(
                "Updated-" + UUID.randomUUID(),
                BigDecimal.valueOf(250000)
        );
        HttpResponse<String> updateResponse = sendJson("PUT", "/companies/" + companyId, updateJson, adminToken);
        assertTrue(updateResponse.statusCode() >= 200 && updateResponse.statusCode() < 300);

        HttpResponse<String> deleteResponse = sendJson("DELETE", "/companies/" + companyId, null, adminToken);
        assertTrue(deleteResponse.statusCode() == HttpStatus.NO_CONTENT.value());
    }

    @Test
    void userToken_cannotDeleteCompany_returns403() throws Exception {
        String adminToken = loginAndGetToken("admin", "1234");
        Long companyId = createCompany(adminToken);

        String userToken = loginAndGetToken("user", "1234");

        HttpResponse<String> deleteResponse = sendJson("DELETE", "/companies/" + companyId, null, userToken);
        assertTrue(deleteResponse.statusCode() == HttpStatus.FORBIDDEN.value());
    }

    @Test
    void noToken_returns401() throws Exception {
        HttpResponse<String> deleteResponse = sendJson("DELETE", "/companies/1", null, null);
        assertTrue(deleteResponse.statusCode() == HttpStatus.UNAUTHORIZED.value());
    }

    private Long createCompany(String token) throws Exception {
        String createJson = """
                {
                  "name": "%s",
                  "budget": %s
                }
                """.formatted(
                "Company-" + UUID.randomUUID(),
                BigDecimal.valueOf(100000)
        );
        HttpResponse<String> response = sendJson("POST", "/companies", createJson, token);
        assertTrue(response.statusCode() >= 200 && response.statusCode() < 300);
        Number idValue = JsonPath.read(response.body(), "$.id");
        assertNotNull(idValue);
        return idValue.longValue();
    }

    private String loginAndGetToken(String username, String password) throws Exception {
        String loginJson = """
                {
                  "username": "%s",
                  "password": "%s"
                }
                """.formatted(username, password);
        HttpResponse<String> response = sendJson("POST", "/auth/login", loginJson, null);
        assertTrue(response.statusCode() >= 200 && response.statusCode() < 300);
        String token = JsonPath.read(response.body(), "$.token");
        assertNotNull(token);
        return token;
    }

    private HttpResponse<String> sendJson(String method, String path, String body, String token) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url(path)))
                .header("Content-Type", "application/json");

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = switch (method) {
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : "")).build();
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body : "")).build();
            case "DELETE" -> builder.DELETE().build();
            default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
