package ba.unsa.etf.cinebh.model.ticket;

import ba.unsa.etf.cinebh.model.auth.User;
import ba.unsa.etf.cinebh.model.projection.Projection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
  @Id
  @Column(name = "ticket_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reservationId;

  @ElementCollection
  @CollectionTable(name = "ticket_seats", joinColumns = @JoinColumn(name = "ticket_id"))
  @Column(name = "seat")
  private List<String> seats = new ArrayList<>();

  private Date date;
  private Integer price;

  @Enumerated(EnumType.STRING)
  private Type type;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "projection_id")
  private Projection projection;

  public Ticket(final List<String> seats, final Date date, final Integer price, final Type type, final User user, final Projection projection) {
    this.seats = seats;
    this.date = date;
    this.price = price;
    this.type = type;
    this.user = user;
    this.projection = projection;
  }

  public Ticket(final Date date, final Integer price, final Type type, final User user, final Projection projection) {
    this.date = date;
    this.price = price;
    this.type = type;
    this.user = user;
    this.projection = projection;
  }
}
