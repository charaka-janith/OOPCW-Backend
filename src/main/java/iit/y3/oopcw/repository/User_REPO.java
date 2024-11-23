package iit.y3.oopcw.repository;

import iit.y3.oopcw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Transactional
@Repository
public interface User_REPO extends JpaRepository<User, Long> {

//    @Query("SELECT d FROM User d WHERE d.userName=:userName AND d.deleted = false")
    Optional<User> findByUserNameAndDeletedFalse(@Param("userName") String userName);

    User findByIdAndDeletedFalse(@NotNull Long id);

//    @Query("SELECT d FROM User d WHERE d.email=:email AND d.deleted = false")
    User findByEmailAndDeletedFalse(@Param("email") String email);

    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.userName = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String userName);

    @Query(value = "SELECT session_sequence.nextval FROM dual", nativeQuery = true)
    Long getNextValue_sessionSequence();
}
