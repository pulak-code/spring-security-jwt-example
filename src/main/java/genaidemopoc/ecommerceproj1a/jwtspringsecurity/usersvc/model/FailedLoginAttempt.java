package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "failed_login_attempts", indexes = {
    @jakarta.persistence.Index(name = "idx_failed_login_email", columnList = "email", unique = true),
    @jakarta.persistence.Index(name = "idx_failed_login_locked", columnList = "is_locked")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedLoginAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(name = "attempt_count")
    @Builder.Default
    private Integer attempts = 0;

    @Column(name = "last_attempt")
    private LocalDateTime lastFailedAttempt;

    @Column(name = "lockout_end")
    private LocalDateTime lockoutEndTime;

    @NotNull
    @Column(name = "is_locked")
    @Builder.Default
    private Boolean isLocked = false;

    @NotNull
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
