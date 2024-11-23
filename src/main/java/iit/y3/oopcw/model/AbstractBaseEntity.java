package iit.y3.oopcw.model;

import java.util.Date;

import iit.y3.oopcw.springsecurity.SpringSecurityUserDetails;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.constraints.NotNull;

@MappedSuperclass
@Data
public abstract class AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date creationTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Column(updatable = false)
    private Long createdBy;

    private Long updatedBy;

    @NotNull
    private boolean deleted;

    @NotNull
    private Long deletionToken = 0L;

    @PrePersist
    protected final void onCreate() {
        this.creationTimestamp = new Date();
        this.createdBy = AbstractBaseEntity.currentUserId();
    }

    @PreUpdate
    protected final void onUpdate() {
        this.updateTimestamp = new Date();
        this.updatedBy = AbstractBaseEntity.currentUserId();
    }

    private static Long currentUserId() {
//        Long userId = 0L;
//        final Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//        if (authentication !=
//                null && authentication.isAuthenticated()) {
//            try {
//                userId = ((User)
//                        authentication.getPrincipal()).getId();
//            } catch (ClassCastException e) {
//                userId = ((SpringSecurityUserDetails)
//                        authentication.getPrincipal()).getUser().getId();
//            }
//        }
//        return userId;
        return 1L;
    }
}