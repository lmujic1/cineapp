package ba.unsa.etf.cinebh.repository.auth;

import ba.unsa.etf.cinebh.model.auth.PasswordReset;
import ba.unsa.etf.cinebh.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
  @Query("SELECT pr FROM PasswordReset pr WHERE pr.user=?1 AND pr.valid=TRUE")
  List<PasswordReset> findAllPasswordResetsForUser(final User user);

  Optional<PasswordReset> findByEmailAndToken(final String email, final String token);

  Optional<PasswordReset> findByEmail(final String email);
}
