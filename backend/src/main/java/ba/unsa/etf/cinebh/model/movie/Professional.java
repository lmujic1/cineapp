package ba.unsa.etf.cinebh.model.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professionals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Professional {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long actorId;

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  private Profession profession;

  public Professional(final String firstName, final String lastName, final Profession profession) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.profession = profession;
  }
}
