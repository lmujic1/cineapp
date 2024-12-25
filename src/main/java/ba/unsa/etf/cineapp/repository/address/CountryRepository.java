package ba.unsa.etf.cineapp.repository.address;

import ba.unsa.etf.cineapp.model.address.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
