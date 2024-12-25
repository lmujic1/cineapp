package ba.unsa.etf.cineapp.repository.venue;

import ba.unsa.etf.cineapp.model.address.City;
import ba.unsa.etf.cineapp.model.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {
  @Query("SELECT venue FROM Venue venue WHERE venue.city = ?1")
  Set<Venue> findAllByCity(final City city);
}

