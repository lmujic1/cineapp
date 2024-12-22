package ba.unsa.etf.cinebh.service.address;

import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.address.Country;
import ba.unsa.etf.cinebh.repository.address.CountryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryService {
  @Autowired
  private CountryRepository countryRepository;

  public Iterable<Country> getAll() {
    return countryRepository.findAll();
  }

  public Country findById(Long id) {
    return countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with provided id not found!"));
  }

  public Country save(Country country) {
    return countryRepository.save(country);
  }

  public void remove(Long id) throws JsonProcessingException {
    if (!countryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Country with id= " + id+ " does not exist");
    }
    countryRepository.deleteById(id);
  }
}
