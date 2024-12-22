package ba.unsa.etf.cinebh.repository.auth;

import ba.unsa.etf.cinebh.model.auth.Role;
import ba.unsa.etf.cinebh.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(final String email);

  User findByRole(final Role role);
}
