package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.AdminService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.impl.AdminServiceImpl;

/**
 * Unit tests for search functionality in the AdminService.
 */
@ExtendWith(MockitoExtension.class)
public class SearchFunctionalityTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    private AdminService adminService;
    
    @BeforeEach
    void setUp() {
        adminService = new AdminServiceImpl(userRepository, passwordEncoder);
    }
    
    @Test
    void testSearchWithValidEmail() {
        // Prepare
        List<UserEntity> mockUsers = new ArrayList<>();
        UserEntity user = new UserEntity();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setName("Test User");
        mockUsers.add(user);

        when(userRepository.findByEmailOrNameSafely(eq("test@example.com"), isNull()))
            .thenReturn(mockUsers);

        // Act
        List<UserEntity> result = adminService.searchUser("test@example.com", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }
    
    @Test
    void testSearchWithValidName() {
        // Prepare
        List<UserEntity> mockUsers = new ArrayList<>();
        UserEntity user = new UserEntity();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        mockUsers.add(user);

        when(userRepository.findByEmailOrNameSafely(isNull(), eq("John")))
            .thenReturn(mockUsers);

        // Act
        List<UserEntity> result = adminService.searchUser(null, "John");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }
    
    @Test
    void testSearchWithBothEmailAndName() {
        // Prepare - Email should take precedence
        List<UserEntity> emailUsers = new ArrayList<>();
        UserEntity emailUser = new UserEntity();
        emailUser.setId("1");
        emailUser.setEmail("priority@example.com");
        emailUser.setName("Priority User");
        emailUsers.add(emailUser);

        when(userRepository.findByEmailOrNameSafely(eq("priority"), eq("John")))
            .thenReturn(emailUsers);

        // Act
        List<UserEntity> result = adminService.searchUser("priority", "John");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("priority@example.com", result.get(0).getEmail());
    }
    
    @Test
    void testSearchWithEmptyParameters() {
        // Prepare
        when(userRepository.findByEmailOrNameSafely(eq(""), eq("")))
            .thenReturn(new ArrayList<>());

        // Act
        List<UserEntity> result = adminService.searchUser("", "");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testSearchWithNullParameters() {
        // Prepare
        when(userRepository.findByEmailOrNameSafely(isNull(), isNull()))
            .thenReturn(new ArrayList<>());

        // Act
        List<UserEntity> result = adminService.searchUser(null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testSearchResultsContainRoles() {
        // Prepare
        List<UserEntity> mockUsers = new ArrayList<>();
        UserEntity user = new UserEntity();
        user.setId("1");
        user.setEmail("admin@example.com");
        user.setName("Admin User");
        
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        user.setRoles(roles);
        
        mockUsers.add(user);

        when(userRepository.findByEmailOrNameSafely(eq("admin"), isNull()))
            .thenReturn(mockUsers);

        // Act
        List<UserEntity> result = adminService.searchUser("admin", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getRoles().contains("ROLE_ADMIN"));
    }
} 