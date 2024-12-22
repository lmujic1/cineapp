package ba.unsa.etf.cinebh.service.address;

import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.address.City;
import ba.unsa.etf.cinebh.repository.address.CityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityService {
  @Autowired
  private CityRepository cityRepository;

  public Iterable<City> getAll() {
    return cityRepository.findAll();
  }

  public City findById(Long id) {
    return cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City with provided id not found!"));
  }

  public City findByName(String name) {
    return cityRepository.findByName(name);
  }

  public City save(City city) {
    return cityRepository.save(city);
  }

  public void remove(Long id) throws JsonProcessingException {
    if (!cityRepository.existsById(id)) {
      throw new ResourceNotFoundException("City with id= " + id+ " does not exist");
    }
    cityRepository.deleteById(id);
  }
}
