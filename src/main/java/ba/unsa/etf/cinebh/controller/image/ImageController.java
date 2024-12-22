package ba.unsa.etf.cinebh.controller.image;

import ba.unsa.etf.cinebh.model.image.Image;
import ba.unsa.etf.cinebh.model.movie.Movie;
import ba.unsa.etf.cinebh.service.image.ImageService;
import ba.unsa.etf.cinebh.service.movie.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
  @Autowired
  private MovieService movieService;

  @Autowired
  private ImageService imageService;

  @GetMapping
  public ResponseEntity<Iterable<Image>> getAll() {
    return ResponseEntity.ok(imageService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Image> getImageById(@PathVariable("id") Long id) {
    Image newImage = imageService.findById(id);
    return ResponseEntity.ok().body(newImage);
  }

  @GetMapping("/movie/{id}")
  public ResponseEntity<Iterable<Image>> getImageByMovieId(@PathVariable("id") Long id) {
    Movie movie = movieService.findById(id);
    Set<Image> newImages = imageService.getImagesByMovie(movie);
    return ResponseEntity.ok(newImages);
  }

  @GetMapping("/cover/movie/{id}")
  public ResponseEntity<Image> getCoverByMovieId(@PathVariable("id") Long id) {
    Movie movie = movieService.findById(id);
    Image cover = imageService.getMovieCover(movie);
    return ResponseEntity.ok(cover);
  }

  @GetMapping("/covers")
  public ResponseEntity<Iterable<Image>> getAllCovers() {
    Set<Image> covers = imageService.getCovers();
    return ResponseEntity.ok(covers);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteImage(@PathVariable long id) throws JsonProcessingException {
    imageService.remove(id);
    return new ResponseEntity<>("Image successfully deleted!", HttpStatus.OK);
  }
}

