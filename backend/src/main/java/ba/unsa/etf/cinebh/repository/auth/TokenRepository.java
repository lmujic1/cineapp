package ba.unsa.etf.cinebh.repository.auth;

import ba.unsa.etf.cinebh.model.auth.Token;
import ba.unsa.etf.cinebh.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
  @Query("SELECT t FROM Token t WHERE t.user=?1 AND t.loggedOut=FALSE")
  List<Token> findAllTokensByUser(final User user);

  Optional<Token> findByToken(final String token);
}
