package ba.unsa.etf.cinebh.model.movie;

import ba.unsa.etf.cinebh.model.image.Image;
import ba.unsa.etf.cinebh.model.projection.Projection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
  @Id
  @Column(name = "movie_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long movieId;
  private String name;
  private String language;
  private Date projectionStart;
  private Date projectionEnd;
  private String director;

  @Column(length = 500)
  private String synopsis;
  private String rating;
  private Integer duration;
  private String trailer;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private Step step;

  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Projection  > projections = new HashSet<>();

  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Image> images = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "genre_movies",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  private Set<Genre> genres = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "movie_professionals",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "professional_id")
  )
  private Set<Professional> professionals = new HashSet<>();


  public Movie(final String name, final Step step, final String language, final Date projectionStart, final Date projectionEnd, final String director, final String synopsis, final String rating, final Integer duration, final String trailer, final Status status) {
    this.name = name;
    this.language = language;
    this.projectionStart = projectionStart;
    this.projectionEnd = projectionEnd;
    this.director = director;
    this.synopsis = synopsis;
    this.rating = rating;
    this.duration = duration;
    this.trailer = trailer;
    this.status = status;
    this.step = step;
  }
}
