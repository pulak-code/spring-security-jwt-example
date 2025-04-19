package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserAlreadyExistsException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserNotFoundException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity testUser;
    private UserRegisterRequest registerRequest;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new UserEntity();
        testUser.setId("1");
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("hashedPassword123");
        testUser.setRoles(List.of("ROLE_USER"));
        testUser.setAddresses(new ArrayList<>());
        testUser.setOrderIds(new ArrayList<>());

        // Setup register request
        registerRequest = new UserRegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setName("Test User");
        registerRequest.setPassword("password123");

        // Setup update request
        updateRequest = new UserUpdateRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setAddresses(new ArrayList<>());
    }

    @Test
    void saveUser_RegisterRequest_Success() {
        // Arrange
        when(userRepository.existsByEmail(eq("test@example.com"))).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        // Act
        UserEntity result = userService.saveUser(registerRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getName(), result.getName());
        assertTrue(result.getRoles().contains("ROLE_USER"));
        
        // Verify interactions
        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void saveUser_RegisterRequest_UserExists() {
        // Arrange
        when(userRepository.existsByEmail(eq("test@example.com"))).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> 
            userService.saveUser(registerRequest)
        );
        
        // Verify
        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void getUserByEmail_Success() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        // Act
        UserEntity result = userService.getUserByEmail(testUser.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        
        // Verify
        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void getUserByEmail_NotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> 
            userService.getUserByEmail(email)
        );
        
        // Verify
        verify(userRepository).findByEmail(email);
    }

    @Test
    void userExists_EmailExists() {
        // Arrange
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);
        
        // Act
        boolean result = userService.userExists(testUser.getEmail());
        
        // Assert
        assertTrue(result);
        
        // Verify
        verify(userRepository).existsByEmail(testUser.getEmail());
    }

    @Test
    void searchUsers_WithValidParameters() {
        // Arrange
        String email = "test";
        String name = "user";
        List<UserEntity> expectedUsers = List.of(testUser);
        when(userRepository.findByEmailOrNameSafely(email, name)).thenReturn(expectedUsers);
        
        // Act
        List<UserEntity> result = userService.searchUsers(email, name);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getEmail(), result.get(0).getEmail());
        
        // Verify
        verify(userRepository).findByEmailOrNameSafely(email, name);
    }
} 