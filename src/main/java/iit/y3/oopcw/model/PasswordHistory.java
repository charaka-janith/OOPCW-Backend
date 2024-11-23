package iit.y3.oopcw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "password_history")
public class PasswordHistory extends AbstractBaseEntity {

    @ManyToOne
    private User user;

    @NotNull
    private String password;

    private Date expiryDate;
}