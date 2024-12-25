package ba.unsa.etf.cineapp.repository.movie;

import ba.unsa.etf.cineapp.model.movie.Profession;
import ba.unsa.etf.cineapp.model.movie.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
  @Query("SELECT r FROM Professional r WHERE r.firstName = :firstName AND r.lastName = :lastName AND r.profession = 'ACTOR'")
  Professional findActorByFirstNameAndLastName(final String firstName, final String lastName);

  @Query("SELECT r FROM Professional r WHERE r.firstName = :firstName AND r.lastName = :lastName AND r.profession = 'WRITER'")
  Professional findWriterByFirstNameAndLastName(final String firstName, final String lastName);

  Iterable<Professional> findAllByProfession(final Profession profession);
}

