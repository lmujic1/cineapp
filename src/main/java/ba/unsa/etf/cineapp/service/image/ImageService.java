package ba.unsa.etf.cineapp.service.image;

import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.image.Image;
import ba.unsa.etf.cineapp.model.movie.Movie;
import ba.unsa.etf.cineapp.repository.image.ImageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class ImageService {
  @Autowired
  private ImageRepository imageRepository;

  public Iterable<Image> getAll() {
    return imageRepository.findAll();
  }

  public Image findById(final Long id) {
    return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image with provided id not found!"));
  }

  public Image createImage(final Image image) {
    return imageRepository.save(image);
  }

  public Set<Image> getImagesByMovie(final Movie movie) {
    return imageRepository.getImagesByMovie(movie);
  }

  public Image getMovieCover(final Movie movie) {
    return imageRepository.getMovieCover(movie);
  }

  public Image save(final Image image) {
    return imageRepository.save(image);
  }

  public void remove(final Long id) throws JsonProcessingException {
    if (!imageRepository.existsById(id)) {
      throw new ResourceNotFoundException("Movie with id= " + id + " does not exist");
    }
    imageRepository.deleteById(id);
  }

  public void deleteImage(final Image image) {
    imageRepository.delete(image);
  }

  public Set<Image> getCovers() {
    return imageRepository.findByCoverTrue();
  }
}

