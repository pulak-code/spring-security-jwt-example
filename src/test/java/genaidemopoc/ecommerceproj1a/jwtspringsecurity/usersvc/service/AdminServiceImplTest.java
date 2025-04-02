package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AdminServiceImpl adminService;
    
    @Mock
    private Principal principal;
    
    private UserEntity testUser;
    private List<UserEntity> userList;
    
    @BeforeEach
    public void setup() {
        // Create test user
        testUser = new UserEntity();
        testUser.setId("1");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRoles(List.of("ROLE_USER"));
        
        // Create test user list
        userList = new ArrayList<>();
        userList.add(testUser);
    }
    
    @Test
    @DisplayName("Test searchUser with null email and null username")
    public void testSearchUserWithBothNull() {
        when(userRepository.findByEmailOrNameSafely(null, null)).thenReturn(userList);
        
        List<UserEntity> result = adminService.searchUser(null, null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByEmailOrNameSafely(null, null);
    }
    
    @Test
    @DisplayName("Test searchUser with empty email and empty username")
    public void testSearchUserWithBothEmpty() {
        when(userRepository.findByEmailOrNameSafely("", "")).thenReturn(userList);
        
        List<UserEntity> result = adminService.searchUser("", "");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByEmailOrNameSafely("", "");
    }
    
    @Test
    @DisplayName("Test searchUser with valid email and null username")
    public void testSearchUserWithValidEmailNullUsername() {
        when(userRepository.findByEmailOrNameSafely("test", null)).thenReturn(userList);
        
        List<UserEntity> result = adminService.searchUser("test", null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByEmailOrNameSafely("test", null);
    }
    
    @Test
    @DisplayName("Test searchUser with null email and valid username")
    public void testSearchUserWithNullEmailValidUsername() {
        when(userRepository.findByEmailOrNameSafely(null, "user")).thenReturn(userList);
        
        List<UserEntity> result = adminService.searchUser(null, "user");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByEmailOrNameSafely(null, "user");
    }
    
    @Test
    @DisplayName("Test searchUser with exception handling")
    public void testSearchUserWithException() {
        doThrow(new IllegalArgumentException("Argument for creating $regex pattern for property 'name' must not be null"))
            .when(userRepository).findByEmailOrNameSafely(anyString(), anyString());
        
        when(userRepository.findAll()).thenReturn(userList);
        
        List<UserEntity> result = adminService.searchUser("test", "user");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByEmailOrNameSafely("test", "user");
    }
    
    @Test
    @DisplayName("Test searchUser with empty results")
    public void testSearchUserWithEmptyResults() {
        when(userRepository.findByEmailOrNameSafely("nonexistent", null))
            .thenReturn(Collections.emptyList());
        
        List<UserEntity> result = adminService.searchUser("nonexistent", null);
        
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository, times(1)).findByEmailOrNameSafely("nonexistent", null);
    }
} 