package iit.y3.oopcw.repository;

import iit.y3.oopcw.model.PasswordHistory;
import iit.y3.oopcw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface PasswordHistory_REPO extends JpaRepository<PasswordHistory, Long> {
    PasswordHistory findByPasswordAndUserAndDeletedFalse(String password, User user);
    List<PasswordHistory> findAllByUser(User user);
}