package ba.unsa.etf.cinebh.service.movie;

import ba.unsa.etf.cinebh.dto.request.movie.CurrentlyMoviesFilterParams;
import ba.unsa.etf.cinebh.dto.request.movie.StatusParams;
import ba.unsa.etf.cinebh.dto.request.movie.UpcomingMoviesFilterParams;
import ba.unsa.etf.cinebh.dto.request.other.PaginationParams;
import ba.unsa.etf.cinebh.dto.response.NumberOfElementsResponse;
import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.address.City;
import ba.unsa.etf.cinebh.model.image.Image;
import ba.unsa.etf.cinebh.model.movie.Genre;
import ba.unsa.etf.cinebh.model.movie.Movie;
import ba.unsa.etf.cinebh.model.movie.Status;
import ba.unsa.etf.cinebh.model.venue.Venue;
import ba.unsa.etf.cinebh.repository.address.CityRepository;
import ba.unsa.etf.cinebh.repository.movie.GenreRepository;
import ba.unsa.etf.cinebh.repository.movie.MovieRepository;
import ba.unsa.etf.cinebh.repository.venue.VenueRepository;
//import ba.unsa.etf.cinebh.service.auth.AmazonService;
import ba.unsa.etf.cinebh.service.image.ImageService;
import ba.unsa.etf.cinebh.specification.MovieSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class MovieService {
  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private VenueRepository venueRepository;

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private ImageService imageService;

//  @Autowired
//  private AmazonService amazonService;

  public Iterable<Movie> getAll() {
    return movieRepository.findAll();
  }

  public Movie findById(final Long id) {
    return movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie with provided id not found!"));
  }

  public Movie save(final Movie movie) {
    return movieRepository.save(movie);
  }

  public void remove(final Long id) throws JsonProcessingException {
    if (!movieRepository.existsById(id)) {
      throw new ResourceNotFoundException("Movie with id= " + id + " does not exist");
    }
    movieRepository.deleteById(id);
  }

  public Page<Movie> getCurrentlyShowing(final Integer pageNumber, final Integer size) {
    return movieRepository.findCurrentlyShowing(PageRequest.of(pageNumber - 1, size));
  }

  public Page<Movie> getUpcoming(final Integer pageNumber, final Integer size) {
    return movieRepository.findUpcoming(PageRequest.of(pageNumber-1, size));
  }

  public Page<Movie> findAllSimilarMoviesByGenre(final Movie movie, final PaginationParams paginationParams){
    Set<Genre> genreList = movie.getGenres();
    Specification<Movie> filters = Specification.where(MovieSpecification.hasSimilarGenres(movie.getMovieId(), genreList))
        .and(MovieSpecification.projectionEndGreaterThenDate(null))
        .and(MovieSpecification.hasStatus(Status.PUBLISHED));
    return movieRepository.findAll(filters, PageRequest.of(paginationParams.getPage()-1, paginationParams.getSize()));
  }

  public Page<Movie> getCurrentlyShowingForFilter(final CurrentlyMoviesFilterParams currentlyMoviesFilterParams, final PaginationParams paginationParams) {
    Optional<City> city = cityRepository.findById(currentlyMoviesFilterParams.getCity());
    Optional<Venue> venue = venueRepository.findById(currentlyMoviesFilterParams.getVenue());
    Optional<Genre> genre = genreRepository.findById(currentlyMoviesFilterParams.getGenre());
    Specification<Movie> filters = Specification.where(StringUtils.isBlank(currentlyMoviesFilterParams.getContains()) ? null : MovieSpecification.nameLike(currentlyMoviesFilterParams.getContains()))
        .and(StringUtils.isBlank(currentlyMoviesFilterParams.getTime()) ? null : MovieSpecification.hasProjectionTime(currentlyMoviesFilterParams.getTime()))
        .and(genre.map(MovieSpecification::hasGenre).orElse(null))
        .and(city.map(MovieSpecification::hasProjectionInCity).orElse(null))
        .and(venue.map(MovieSpecification::hasProjectionInVenue).orElse(null))
        .and(MovieSpecification.projectionStartLessThenDate(currentlyMoviesFilterParams.getStartDate()))
        .and(MovieSpecification.projectionEndGreaterThenDate(currentlyMoviesFilterParams.getStartDate()))
        .and(MovieSpecification.hasStatus(Status.PUBLISHED));
    return movieRepository.findAll(filters, PageRequest.of(paginationParams.getPage() - 1, paginationParams.getSize()));
  }


  public Page<Movie> getUpcomingForFilter(final UpcomingMoviesFilterParams upcomingMoviesFilterParams, final PaginationParams paginationParams) {
    Optional<City> city = cityRepository.findById(upcomingMoviesFilterParams.getCity());
    Optional<Venue> venue = venueRepository.findById(upcomingMoviesFilterParams.getVenue());
    Optional<Genre> genre = genreRepository.findById(upcomingMoviesFilterParams.getGenre());
    Specification<Movie> filters = Specification.where(StringUtils.isBlank(upcomingMoviesFilterParams.getContains()) ? null : MovieSpecification.nameLike(upcomingMoviesFilterParams.getContains()))
        .and(genre.map(MovieSpecification::hasGenre).orElse(null))
        .and(city.map(MovieSpecification::hasProjectionInCity).orElse(null))
        .and(venue.map(MovieSpecification::hasProjectionInVenue).orElse(null))
        .and(MovieSpecification.projectionBetweenDates(upcomingMoviesFilterParams.getStartDate(), upcomingMoviesFilterParams.getEndDate()))
        .and(MovieSpecification.hasStatus(Status.PUBLISHED));
    return movieRepository.findAll(filters, PageRequest.of(paginationParams.getPage() - 1, paginationParams.getSize()));
  }

  public Page<Movie> getByStatus(final StatusParams statusParams, final Integer pageNumber, final Integer size) {
    Status status = Status.valueOf(statusParams.getStatus());
    return movieRepository.findByStatus(status, PageRequest.of(pageNumber-1, size));
  }

  public NumberOfElementsResponse getNumberOfElements() {
    int drafts = (int) movieRepository.countDrafts();
    int currently = (int) movieRepository.countCurrentlyShowing();
    int upcoming = (int) movieRepository.countUpcoming();
    int archived = (int) movieRepository.countArchived();
    return new NumberOfElementsResponse(drafts, currently, upcoming, archived);
  }

  public Page<Movie> getAllUpcoming(final Integer page, final Integer size) {
    return movieRepository.findAllUpcoming(PageRequest.of(page-1, size));
  }

  @Transactional
  public void batchUpdateStatus(final Set<Long> movieIds, final String newStatus) {
    for (final Long id : movieIds) {
      Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found for ID " + id));
      movie.setStatus(Status.valueOf(newStatus));
      movieRepository.save(movie);
    }
  }

  public void deletePhotos(final long movieId, final List<Long> imageIds) {
    Movie movie = findById(movieId);
    Set<Image> images = movie.getImages();

    for (final Long id : imageIds) {
      Image image = imageService.findById(id);
//      if (photo != null && images.contains(photo)) {
//        amazonService.deleteFileFromS3Bucket(photo.getLink());
//        images.remove(photo);
//        imageService.deleteImage(photo);
//      }
    }

    movie.setImages(images);
    save(movie);
  }
}
