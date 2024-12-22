package ba.unsa.etf.cinebh.model.image;

import ba.unsa.etf.cinebh.model.movie.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String link;
  private Boolean cover;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id", nullable = false)
  @JsonIgnore
  private Movie movie;

  public Image(final String link, final Boolean cover, final Movie movie) {
    this.link = link;
    this.cover = cover;
    this.movie = movie;
  }
}
