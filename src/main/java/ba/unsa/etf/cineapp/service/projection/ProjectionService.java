package ba.unsa.etf.cineapp.service.projection;

import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.movie.Movie;
import ba.unsa.etf.cineapp.model.projection.Projection;
import ba.unsa.etf.cineapp.model.venue.Venue;
import ba.unsa.etf.cineapp.repository.projection.ProjectionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectionService {
  @Autowired
  private ProjectionRepository projectionRepository;

  public Iterable<Projection> getAll() {
    return projectionRepository.findAll();
  }

  public Projection findById(final Long id) {
    return projectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Projection with provided id not found!"));
  }

  public void deleteProjectionsByMovie(final Movie movie) {
    projectionRepository.deleteByMovie(movie);
  }

  public Projection createProjectionForMovie(final Movie movie, final Time time, final Venue venue) {
    return projectionRepository.save(new Projection(time, movie, venue));
  }

  public Projection save(final Projection projection) {
    return projectionRepository.save(projection);
  }

  public List<String> getTimes() {
    List<String> strings = new ArrayList<>();
    List<Time> times = projectionRepository.getTimes();
    for (Time t : times) {
      LocalTime localTime = t.toLocalTime();
      String newString = localTime.getHour() + ":" + localTime.getMinute();
      if (localTime.getMinute() == 0) newString = newString.concat("0");
      strings.add(newString);
    }
    return strings;
  }

  public List<Projection> getProjectionsForMovie(final Movie movie, final Venue venue) {
    if(venue==null) return projectionRepository.getProjectionsForMovie(movie);
    return projectionRepository.getProjectionsForMovieAndVenue(movie.getMovieId(), venue.getVenueId());
  }

  public void remove(final Long id) throws JsonProcessingException {
    if (!projectionRepository.existsById(id)) {
      throw new ResourceNotFoundException("Projection with id= " + id + " does not exist");
    }
    projectionRepository.deleteById(id);
  }

  public String deleteProjection(final long id) {
    projectionRepository.deleteById(id);
    return "Projection deleted!";
  }
}
