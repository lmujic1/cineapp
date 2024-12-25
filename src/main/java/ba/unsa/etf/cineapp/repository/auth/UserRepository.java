package ba.unsa.etf.cineapp.repository.auth;

import ba.unsa.etf.cineapp.model.auth.Role;
import ba.unsa.etf.cineapp.model.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(final String email);

  Optional<User> findByFirstName(final String email);

  User findByRole(final Role role);

  Page<User> findAllByRoleIn(List<Role> roles, Pageable pageable);
}
