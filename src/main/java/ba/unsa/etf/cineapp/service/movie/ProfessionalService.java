package ba.unsa.etf.cineapp.service.movie;

import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.movie.Profession;
import ba.unsa.etf.cineapp.model.movie.Professional;
import ba.unsa.etf.cineapp.repository.movie.ProfessionalRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfessionalService {
  @Autowired
  private ProfessionalRepository professionalRepository;

  public Iterable<Professional> getAllWriters() {
    return professionalRepository.findAllByProfession(Profession.WRITER);
  }

  public Iterable<Professional> getAllActors() {
    return professionalRepository.findAllByProfession(Profession.ACTOR);
  }

  public Professional findById(final Long id) {
    return professionalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional with provided id not found!"));
  }

  public Professional save(final Professional professional) {
    return professionalRepository.save(professional);
  }

  public void remove(final Long id) throws JsonProcessingException {
    if (!professionalRepository.existsById(id)) {
      throw new ResourceNotFoundException("Professional with id= " + id + " does not exist");
    }
    professionalRepository.deleteById(id);
  }

  public Professional findActorByFullName(final String firstName, final String lastName) {
    return professionalRepository.findActorByFirstNameAndLastName(firstName, lastName);
  }

  public Professional findWriterByFullName(final String firstName, final String lastName) {
    return professionalRepository.findWriterByFirstNameAndLastName(firstName, lastName);
  }
}
