package ba.unsa.etf.cinebh.model.auth;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String token;
  private Boolean loggedOut;

  @Column(nullable = false)
  private String refreshToken;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
