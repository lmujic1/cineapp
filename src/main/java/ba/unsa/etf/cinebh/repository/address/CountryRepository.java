package ba.unsa.etf.cinebh.repository.address;

import ba.unsa.etf.cinebh.model.address.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
