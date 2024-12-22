package ba.unsa.etf.cinebh.model.venue;

import ba.unsa.etf.cinebh.model.address.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Venue {
  @Id
  @Column(name = "venue_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long venueId;
  private String name;
  private String photo;
  private String street;
  private Integer streetNumber;
  private String telephone;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  public Venue(final String name, final String photo, final String street, final Integer streetNumber, final String telephone, final City city) {
    this.name = name;
    this.photo = photo;
    this.street = street;
    this.streetNumber = streetNumber;
    this.telephone = telephone;
    this.city = city;
  }
}
