package ba.unsa.etf.cineapp.controller.projection;

import ba.unsa.etf.cineapp.model.movie.Movie;
import ba.unsa.etf.cineapp.model.projection.Projection;
import ba.unsa.etf.cineapp.model.venue.Venue;
import ba.unsa.etf.cineapp.service.movie.MovieService;
import ba.unsa.etf.cineapp.service.projection.ProjectionService;
import ba.unsa.etf.cineapp.service.venue.VenueService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/projections")
public class ProjectionController {
  @Autowired
  private ProjectionService projectionService;

  @Autowired
  private MovieService movieService;

  @Autowired
  private VenueService venueService;

  @GetMapping("/times")
  ResponseEntity<List<String>> getProjectionTimes(){
    return ResponseEntity.ok(projectionService.getTimes());
  }

  @GetMapping
  ResponseEntity<List<Projection>> getMovieProjectionsForVenue(@RequestParam Long movie, @RequestParam Long venue, @RequestParam String date){
    Movie movieObj = movieService.findById(movie);
    Venue venueObj = venueService.findById(venue);
    Date dateObj = Date.valueOf(date);
    return ResponseEntity.ok(projectionService.getProjectionsForMovie(movieObj, venueObj));
  }

  @GetMapping("/movie/{id}")
  ResponseEntity<List<Projection>> getMovieProjections(@PathVariable long id){
    Movie movie = movieService.findById(id);
    return ResponseEntity.ok(projectionService.getProjectionsForMovie(movie, null));
  }

  @DeleteMapping("/{id}")
  ResponseEntity<String> deleteProjection(@PathVariable long id) {
    return ResponseEntity.ok(projectionService.deleteProjection(id));
  }
}
