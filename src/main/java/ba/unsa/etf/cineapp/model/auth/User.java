package ba.unsa.etf.cineapp.model.auth;

import ba.unsa.etf.cineapp.model.address.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @Column(name = "user_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  private String firstName;
  private String lastName;
  private String phone;
  private String image;
  private Boolean softDelete = false;
  private Boolean addedBySuperAdmin = false;
  private Boolean passwordChanged = true;

  @Column(name = "email", nullable = false, unique = true)
  @Email(message = "Email must be valid!")
  @NotBlank
  private String email;

  @Column(name = "password", nullable = false)
  @Size(min = 4, message = "Password must have at least 4 characters")
  private String password;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
