package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "failed_login_attempts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedLoginAttempt {
    
    @Id
    private String id;

    @NotNull
    @Email
    @Indexed(unique = true)
    private String email;

    @NotNull
    @Field("attempt_count")
    @Builder.Default
    private Integer attempts = 0;

    @Field("last_attempt")
    private LocalDateTime lastFailedAttempt;

    @Field("lockout_end")
    private LocalDateTime lockoutEndTime;

    @NotNull
    @Field("is_locked")
    @Builder.Default
    private Boolean isLocked = false;

    @NotNull
    @Field("created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Checks if the account is locked due to failed login attempts.
     *
     * @return true if the account is locked, false otherwise
     */
    public boolean isLocked() {
        return isLocked;
    }
    
    /**
     * Sets the locked status of the account.
     *
     * @param locked true to lock the account, false to unlock it
     */
    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }
}
