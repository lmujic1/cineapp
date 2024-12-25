package ba.unsa.etf.cineapp.repository.address;

import ba.unsa.etf.cineapp.model.address.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
  City findByName(String name);
}
