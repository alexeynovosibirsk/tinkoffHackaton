package com.nazarov.tinkoffcontest.repository;

package com.alexeynovosibirsk.demo.repository;

import com.alexeynovosibirsk.demo.entity.Role;
import com.alexeynovosibirsk.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

    @Query(value = "SELECT COUNT(*) FROM Usr u " +
            "JOIN User_role ur" +
            " ON u.id = ur.user_id" +
            " WHERE ur.roles LIKE 'ADMIN'",
            nativeQuery = true
    )
    int findAdmins();

    @Query(value = "SELECT COUNT(*) FROM Usr u " +
            "JOIN User_role ur" +
            " ON u.id = ur.user_id" +
            " WHERE ur.roles LIKE 'USER'",
            nativeQuery = true
    )
    int findUsers();

    @Query(value = "SELECT COUNT(*) FROM Usr" +
            " WHERE active = true",
            nativeQuery = true
    )
    int activeAccounts();

    @Query(value = "SELECT COUNT(*) FROM Usr" +
            " WHERE active = false",
            nativeQuery = true
    )
    int inactiveAccounts();

    @Query(value = "SELECT * FROM Usr" +
            " WHERE username LIKE %?1%" +
            " AND  email LIKE %?2%" +
            " AND  active = true",
            nativeQuery = true
    )
    List<User> mainSelectActive(String username, String email);

    @Query(value = "SELECT * FROM Usr" +
            " WHERE username LIKE %?1%" +
            " AND  email LIKE %?2%" +
            " AND  active = false",
            nativeQuery = true
    )
    List<User> mainSelectInactive(String username, String email);

    @Modifying
    @Query(value = "DELETE FROM USER_ROLE " +
            " WHERE roles = 'ADMIN' " +
            " AND user_id = ?1",

            nativeQuery = true
    )
    void deleteAdmin(long id);
}