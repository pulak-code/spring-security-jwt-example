package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.utils;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.dto.AuthRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.dto.AuthResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.dto.UserRegistrationDto;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

/**
 * Helper class for integration tests providing utility methods for authentication,
 * creating HTTP headers, and making HTTP requests.
 */
public class TestHelper {

    /**
     * Creates a user registration DTO with the specified parameters.
     */
    public static UserRegistrationDto createUserRegistration(String email, String password, String name, String role) {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setName(name);
        userDto.setRoles(Collections.singletonList(role));
        return userDto;
    }

    /**
     * Creates authentication request DTO with email and password.
     */
    public static AuthRequest createAuthRequest(String email, String password) {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(email);
        authRequest.setPassword(password);
        return authRequest;
    }

    /**
     * Creates HTTP headers with content type application/json.
     */
    public static HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Creates HTTP headers with authorization bearer token.
     */
    public static HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = createJsonHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    /**
     * Registers a user and returns the response entity.
     */
    public static ResponseEntity<String> registerUser(TestRestTemplate restTemplate, String baseUrl, 
                                                    UserRegistrationDto registrationDto) {
        HttpEntity<UserRegistrationDto> request = new HttpEntity<>(registrationDto, createJsonHeaders());
        return restTemplate.exchange(
                baseUrl + "/api/auth/user/register",
                HttpMethod.POST,
                request,
                String.class
        );
    }

    /**
     * Logs in a user and returns the auth response containing tokens.
     */
    public static ResponseEntity<AuthResponse> loginUser(TestRestTemplate restTemplate, String baseUrl, 
                                                      String email, String password) {
        AuthRequest authRequest = createAuthRequest(email, password);
        HttpEntity<AuthRequest> request = new HttpEntity<>(authRequest, createJsonHeaders());
        return restTemplate.exchange(
                baseUrl + "/api/auth/user/login",
                HttpMethod.POST,
                request,
                AuthResponse.class
        );
    }

    /**
     * Makes a GET request with authorization header.
     */
    public static <T> ResponseEntity<T> getWithAuth(TestRestTemplate restTemplate, String url, 
                                                 String token, Class<T> responseType) {
        HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders(token));
        return restTemplate.exchange(url, HttpMethod.GET, request, responseType);
    }

    /**
     * Makes a POST request with authorization header.
     */
    public static <T, R> ResponseEntity<R> postWithAuth(TestRestTemplate restTemplate, String url, 
                                                     T body, String token, Class<R> responseType) {
        HttpEntity<T> request = new HttpEntity<>(body, createAuthHeaders(token));
        return restTemplate.exchange(url, HttpMethod.POST, request, responseType);
    }

    /**
     * Makes a DELETE request with authorization header.
     */
    public static <R> ResponseEntity<R> deleteWithAuth(TestRestTemplate restTemplate, String url, 
                                                    String token, Class<R> responseType) {
        HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders(token));
        return restTemplate.exchange(url, HttpMethod.DELETE, request, responseType);
    }
} 