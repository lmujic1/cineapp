package ba.unsa.etf.cinebh.controller.movie;

import ba.unsa.etf.cinebh.dto.request.image.ImageRequest;
import ba.unsa.etf.cinebh.dto.request.movie.*;
import ba.unsa.etf.cinebh.dto.request.other.PaginationParams;
import ba.unsa.etf.cinebh.dto.request.projection.ProjectionRequest;
import ba.unsa.etf.cinebh.dto.response.DetailsResponse;
import ba.unsa.etf.cinebh.dto.response.MovieResponse;
import ba.unsa.etf.cinebh.dto.response.NumberOfElementsResponse;
import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.image.Image;
import ba.unsa.etf.cinebh.model.movie.Genre;
import ba.unsa.etf.cinebh.model.movie.Movie;
import ba.unsa.etf.cinebh.model.movie.Profession;
import ba.unsa.etf.cinebh.model.movie.Professional;
import ba.unsa.etf.cinebh.model.projection.Projection;
import ba.unsa.etf.cinebh.model.venue.Venue;
import ba.unsa.etf.cinebh.repository.movie.GenreRepository;
//import ba.unsa.etf.cinebh.service.auth.AmazonService;
import ba.unsa.etf.cinebh.service.image.ImageService;
import ba.unsa.etf.cinebh.service.movie.MovieService;
import ba.unsa.etf.cinebh.service.movie.ProfessionalService;
import ba.unsa.etf.cinebh.service.other.CSVService;
import ba.unsa.etf.cinebh.service.projection.ProjectionService;
import ba.unsa.etf.cinebh.service.venue.VenueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

  ObjectMapper objectMapper;
  @Autowired
  private MovieService movieService;
  @Autowired
  private VenueService venueService;
  private GenreRepository genreRepository;
  private ImageService imageService;
  private ProfessionalService professionalService;
  private ProjectionService projectionService;
//  private AmazonService amazonClient;

  @Autowired
  private CSVService csvService;

  @GetMapping
  public ResponseEntity<Iterable<Movie>> getAll() {
    return ResponseEntity.ok(movieService.getAll());
  }

  @GetMapping("/count-elements")
  public ResponseEntity<NumberOfElementsResponse> getNumberOfElements() {
    return ResponseEntity.ok(movieService.getNumberOfElements());
  }

  @GetMapping("/search-by-status")
  public ResponseEntity<Iterable<Movie>> getByStatus(StatusParams statusParams, PaginationParams paginationParams) {
    return ResponseEntity.ok(movieService.getByStatus(statusParams, paginationParams.getPage(), paginationParams.getSize()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id) {
    Movie newMovie = movieService.findById(id);
    return ResponseEntity.ok().body(newMovie);
  }

  @GetMapping("/currently")
  public ResponseEntity<Iterable<Movie>> getCurrently(PaginationParams paginationParams) {
    return ResponseEntity.ok(movieService.getCurrentlyShowing(paginationParams.getPage(), paginationParams.getSize()));
  }

  @GetMapping("/upcoming")
  public ResponseEntity<Iterable<Movie>> getUpcoming(PaginationParams paginationParams) {
    return ResponseEntity.ok(movieService.getUpcoming(paginationParams.getPage(), paginationParams.getSize()));
  }

  @GetMapping("/all-upcoming")
  public ResponseEntity<Iterable<Movie>> getAllUpcoming(PaginationParams paginationParams) {
    return ResponseEntity.ok(movieService.getAllUpcoming(paginationParams.getPage(), paginationParams.getSize()));
  }

  @GetMapping("/search-currently")
  public ResponseEntity<Page<Movie>> getCurrentlyMovies(CurrentlyMoviesFilterParams filterParams, PaginationParams paginationParams) {
    return ResponseEntity.ok(movieService.getCurrentlyShowingForFilter(filterParams, paginationParams));
  }

  @GetMapping("/search-upcoming")
  public ResponseEntity<Page<Movie>> getUpcomingMovies(UpcomingMoviesFilterParams filterParams, PaginationParams paginationParams) {
    return ResponseEntity.ok(movieService.getUpcomingForFilter(filterParams, paginationParams));
  }

  @GetMapping("/similar")
  public ResponseEntity<Page<Movie>> findSimilarMovies(@RequestParam Long movie, PaginationParams paginationParams) {
    Movie movieObj = movieService.findById(movie);
    return ResponseEntity.ok(movieService.findAllSimilarMoviesByGenre(movieObj, paginationParams));
  }

  @PostMapping
  public ResponseEntity<MovieResponse> createMovie(@Validated @RequestBody MovieRequest movieRequest) {
    Movie movie = new Movie(movieRequest.getName(),
        movieRequest.getStep(),
        movieRequest.getLanguage(),
        movieRequest.getProjectionStart(),
        movieRequest.getProjectionEnd(),
        movieRequest.getDirector(),
        movieRequest.getSynopsis(),
        movieRequest.getRating(),
        movieRequest.getDuration(),
        movieRequest.getTrailer(),
        movieRequest.getStatus()
    );
    Set<Long> genresSet = movieRequest.getGenres();
    if (genresSet != null) {
      Set<Genre> genres = genresSet.stream()
          .map(genreId -> genreRepository.findById(genreId)
              .orElseThrow(() -> new RuntimeException("Error: Genre not found for ID " + genreId)))
          .collect(Collectors.toSet());
      movie.setGenres(genres);
    }

    Movie createdMovie = movieService.save(movie);
    return ResponseEntity.ok(new MovieResponse(createdMovie.getMovieId()));
  }

  @PostMapping("/{id}")
  public ResponseEntity<String> updateMovie(@PathVariable long id, @Validated @RequestBody MovieRequest movieDetails) {
    Movie updateMovie = movieService.findById(id);
    updateMovie.setName(movieDetails.getName());
    updateMovie.setLanguage(movieDetails.getLanguage());
    updateMovie.setProjectionStart(movieDetails.getProjectionStart());
    updateMovie.setProjectionEnd(movieDetails.getProjectionEnd());
    updateMovie.setDirector(movieDetails.getDirector());
    updateMovie.setSynopsis(movieDetails.getSynopsis());
    updateMovie.setRating(movieDetails.getRating());
    updateMovie.setDuration(movieDetails.getDuration());
    updateMovie.setTrailer(movieDetails.getTrailer());
    updateMovie.setStatus(movieDetails.getStatus());
    updateMovie.setStep(movieDetails.getStep());
    Set<Long> genresSet = movieDetails.getGenres();
    Set<Genre> genres = genresSet.stream()
        .map(genreId -> genreRepository.findById(genreId)
            .orElseThrow(() -> new RuntimeException("Error: Genre not found for ID " + genreId)))
        .collect(Collectors.toSet());
    updateMovie.setGenres(genres);
    movieService.save(updateMovie);
    return new ResponseEntity<>("Movie with id = " + id + " successfully updated!", HttpStatus.OK);
  }

  @PostMapping(path = "/{id}/add-files")
  public ResponseEntity<String> addImages(@PathVariable long id, @RequestPart(value = "files", required = false) MultipartFile[] files, @RequestPart(value = "images", required = false) ImageRequest[] imageRequests) {
    try {
      Movie movie = movieService.findById(id);
      if (movie == null) {
        return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
      }

      Set<Image> newImages = new HashSet<>();

      int index = 0;
      if (files != null) {
        for (MultipartFile file : files) {
//          String fileUrl = amazonClient.uploadFile(file);
//          newImages.add(imageService.createImage(new Image(fileUrl, imageRequests[ index ].getCover(), movie)));
          index++;
        }
      }

      if (imageRequests != null) {
        for (int i = index; i < imageRequests.length; i++) {
          Image p = imageService.findById(imageRequests[ i ].getId());
          p.setCover(imageRequests[ i ].getCover());
          imageService.save(p);
          newImages.add(p);
        }
      }
      movie.setImages(newImages);
      movieService.save(movie);

      return new ResponseEntity<>("Successfully added images for movie with id=" + id + "!", HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>("An error occurred while adding images", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(path = "/{id}/images")
  public ResponseEntity<String> deleteImages(@PathVariable long id, @RequestBody List<Long> deleteImages) {
    try {
      Movie movie = movieService.findById(id);
      if (movie == null) {
        return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
      }

      Set<Image> images = movie.getImages();
      Set<Image> imagesToDelete = deleteImages.stream()
          .map(imageService::findById)
          .collect(Collectors.toSet());

      for (Image image : imagesToDelete) {
        if (image == null) {
          return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
//        amazonClient.deleteFileFromS3Bucket(image.getLink());
        images.remove(image);
        imageService.deleteImage(image);
      }

      movie.setImages(images);
      movieService.save(movie);

      return new ResponseEntity<>("Successfully deleted images for movie with id=" + id + "!", HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>("An error occurred while deleting images", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(path = "/{id}/projection")
  public ResponseEntity<String> addOrUpdateProjections(@PathVariable long id, @Validated @RequestBody ProjectionRequest[] projectionRequests) {
    Movie movie = movieService.findById(id);

    Set<Projection> updatedProjections = Arrays.stream(projectionRequests)
        .map(projectionRequest -> {
          if (projectionRequest.getId() != null) {
            Projection projection = projectionService.findById(projectionRequest.getId());
            Venue venue = venueService.findById(projectionRequest.getVenue());
            projection.setTime(projectionRequest.getTime());
            projection.setVenue(venue);
            return projectionService.save(projection);
          } else {
            Venue venue = venueService.findById(projectionRequest.getVenue());
            return projectionService.createProjectionForMovie(movie, projectionRequest.getTime(), venue);
          }
        })
        .collect(Collectors.toSet());

    movie.setProjections(updatedProjections);
    movieService.save(movie);
    return new ResponseEntity<>("Successfully added or updated projections for movie with id=" + id + "!", HttpStatus.OK);
  }


  private void deleteProjectionsForMovie(Movie movie) {
    movie.setProjections(new HashSet<>());
    movieService.save(movie);
    projectionService.deleteProjectionsByMovie(movie);
  }

  @DeleteMapping(path = "/{id}/projection")
  public ResponseEntity<String> deleteProjections(@PathVariable long id) {
    Movie movie = movieService.findById(id);
    deleteProjectionsForMovie(movie);
    return new ResponseEntity<>("Successfully deleted projections for movie with id=" + id + "!", HttpStatus.OK);
  }

  @PostMapping(path = "/{id}/writers")
  public ResponseEntity<String> addWriters(@PathVariable long id, @Validated @RequestBody ProfessionalRequest[] writerRequests) {
    Movie movie = movieService.findById(id);
    Set<Professional> writers = Arrays.stream(writerRequests)
        .map(writerRequest -> {
          Professional writer = professionalService.findWriterByFullName(writerRequest.getFirstName(), writerRequest.getLastName());
          if (writer == null) {
            writer = professionalService.save(new Professional(writerRequest.getFirstName(), writerRequest.getLastName(), Profession.WRITER));
          }
          return writer;
        })
        .collect(Collectors.toSet());
    movie.setProfessionals(writers);
    movieService.save(movie);
    return new ResponseEntity<>("Successfully added writers for movie with id=" + id + "!", HttpStatus.OK);
  }

  @PostMapping(path = "/{id}/actors")
  public ResponseEntity<String> addActors(@PathVariable long id, @Validated @RequestBody ProfessionalRequest[] actorRequests) {
    Movie movie = movieService.findById(id);
    Set<Professional> actors = Arrays.stream(actorRequests)
        .map(writerRequest -> {
          Professional actor = professionalService.findWriterByFullName(writerRequest.getFirstName(), writerRequest.getLastName());
          if (actor == null) {
            actor = professionalService.save(new Professional(writerRequest.getFirstName(), writerRequest.getLastName(), Profession.ACTOR));
          }
          return actor;
        })
        .collect(Collectors.toSet());
    movie.setProfessionals(actors);
    movieService.save(movie);
    return new ResponseEntity<>("Successfully added actors for movie with id=" + id + "!", HttpStatus.OK);
  }

  @PostMapping(path = "/{id}/add-details")
  public ResponseEntity<DetailsResponse> addDetails(@PathVariable long id,
                                                    @RequestParam(value = "actorsFile", required = false) MultipartFile actorsFile,
                                                    @RequestParam(value = "writersFile", required = false) MultipartFile writersFile) {
    try {
      Movie movie = movieService.findById(id);
      if (movie == null) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }

      Set<Professional> actors = new HashSet<>();
      Set<Professional> writers = new HashSet<>();

      if (actorsFile != null && !actorsFile.isEmpty() && actorsFile.getOriginalFilename().endsWith(".csv")) {
        actors = readActorsFromCSV(actorsFile);
      }
      if (writersFile != null && !writersFile.isEmpty() && writersFile.getOriginalFilename().endsWith(".csv")) {
        writers = readWritersFromCSV(writersFile);
      }

      Set<Professional> professionals = new HashSet<>();
      professionals.addAll(actors);
      professionals.addAll(writers);


      if (!actors.isEmpty()) {
        professionals.addAll(actors);
      }
      if (!writers.isEmpty()) {
        professionals.addAll(writers);
      }

      movie.setProfessionals(professionals);

      movieService.save(movie);

      DetailsResponse response = new DetailsResponse(actors, writers);

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Set<Professional> readActorsFromCSV(MultipartFile file) throws Exception {
    Set<Professional> actors = new HashSet<>();
    List<String[]> rows = csvService.readCsv(file);

    for (String[] fields : rows) {
      if (fields.length < 3) continue;
      String firstName = fields[ 0 ].trim();
      String lastName = fields[ 1 ].trim();

      Professional newActor = professionalService.findActorByFullName(firstName, lastName);
      if (newActor == null) {
        newActor = professionalService.save(new Professional(firstName, lastName, Profession.ACTOR));
      }
      actors.add(newActor);
    }
    return actors;
  }

  private Set<Professional> readWritersFromCSV(MultipartFile file) throws Exception {
    Set<Professional> writers = new HashSet<>();
    List<String[]> rows = csvService.readCsv(file);

    for (String[] fields : rows) {
      if (fields.length < 2) continue;
      String firstName = fields[ 0 ].trim();
      String lastName = fields[ 1 ].trim();

      Professional writer = professionalService.findWriterByFullName(firstName, lastName);
      if (writer == null) {
        writer = professionalService.save(new Professional(firstName, lastName, Profession.WRITER));
      }
      writers.add(writer);
    }
    return writers;
  }

  @DeleteMapping(path = "/{id}/delete-actors")
  public ResponseEntity<String> deleteActors(@PathVariable long id) {
    try {
      Movie movie = movieService.findById(id);
      if (movie == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      movie.getProfessionals().stream().map(professional -> professional.getProfession() == Profession.ACTOR).toList().clear();
      movieService.save(movie);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(path = "/{id}/delete-writers")
  public ResponseEntity<String> deleteWriters(@PathVariable long id) {
    try {
      Movie movie = movieService.findById(id);
      if (movie == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      movie.getProfessionals().stream().map(professional -> professional.getProfession() == Profession.WRITER).toList().clear();
      movieService.save(movie);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Movie applyPatchToMovie(JsonPatch patch, Movie targetMovie) throws JsonPatchException, JsonProcessingException {
    JsonNode patched = patch.apply(objectMapper.convertValue(targetMovie, JsonNode.class));
    return objectMapper.treeToValue(patched, Movie.class);
  }

  @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
  public ResponseEntity<String> updateMovie(@PathVariable Long id, @RequestBody JsonPatch patch) {
    try {
      Movie movie = movieService.findById(id);
      Movie moviePatched = applyPatchToMovie(patch, movie);
      movieService.save(moviePatched);
      return new ResponseEntity<>("Movie with id = " + id + " successfully updated!", HttpStatus.OK);
    } catch (JsonPatchException | JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/update-status")
  public ResponseEntity<String> updateStatus(@Validated @RequestBody UpdateStatusRequest updateStatusRequest) {
    Set<Long> movieIds = updateStatusRequest.getMovieIds();
    String newStatus = updateStatusRequest.getStatus();
    movieService.batchUpdateStatus(movieIds, newStatus);
    return new ResponseEntity<>("Movies successfully updated!", HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteMovie(@PathVariable long id) throws JsonProcessingException {
    movieService.remove(id);
    return new ResponseEntity<>("Movie successfully deleted!", HttpStatus.OK);
  }
}
