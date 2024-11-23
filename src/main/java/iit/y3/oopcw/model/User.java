package iit.y3.oopcw.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
public class User extends AbstractBaseEntity {
    @NotEmpty(message = "First Name cannot be empty")
    @NotNull(message = "First Name cannot be null")
    private String firstName;
    @NotEmpty(message = "Last Name cannot be empty")
    @NotNull(message = "Last Name cannot be null")
    private String lastName;
    @NotEmpty(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    private String userName;
    //@ValidPassword
    private String password;
    private String role;
    private String contactNo;
    //@Email(message = "Email should be valid")
    private String email;
    /* limit user login */
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
    @Column(name = "failed_attempt")
    private int failedAttempt = 0;
    @Column(name = "lock_time")
    private Date lockTime;
    private String status;
    private LocalDate expiryDate;
}