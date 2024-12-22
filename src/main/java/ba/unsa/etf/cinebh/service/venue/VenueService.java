package ba.unsa.etf.cinebh.service.venue;

import ba.unsa.etf.cinebh.dto.request.other.PaginationParams;
import ba.unsa.etf.cinebh.dto.request.venue.VenueFilterParams;
import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.address.City;
import ba.unsa.etf.cinebh.model.venue.Venue;
import ba.unsa.etf.cinebh.repository.address.CityRepository;
import ba.unsa.etf.cinebh.repository.venue.VenueRepository;
import ba.unsa.etf.cinebh.specification.VenueSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class VenueService {
  @Autowired
  private VenueRepository venueRepository;

  @Autowired
  private CityRepository cityRepository;

  public Iterable<Venue> getAll() {
    return venueRepository.findAll();
  }

  public Iterable<Venue> getVenuesByCity(final City city) {
    return venueRepository.findAllByCity(city);
  }

  public Venue findById(final Long id) {
    return venueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue with provided id not found!"));
  }

  public Venue save(final Venue venue) {
    return venueRepository.save(venue);
  }

  public void remove(final Long id) throws JsonProcessingException {
    if (!venueRepository.existsById(id)) {
      throw new ResourceNotFoundException("Venue with id= " + id + " does not exist");
    }
    venueRepository.deleteById(id);
  }

  public Page<Venue> getVenues(final int pageNumber, final int size) {
    return venueRepository.findAll(PageRequest.of(pageNumber - 1, size));
  }

  public Page<Venue> getVenuesByFilter(final VenueFilterParams filterParams, final PaginationParams paginationParams) {
    Optional<City> city = cityRepository.findById(filterParams.getCity());
    Specification<Venue> filters = Specification.where(StringUtils.isBlank(filterParams.getContains()) ? null : VenueSpecification.nameLike(filterParams.getContains()))
        .and(city.map(VenueSpecification::hasProjectionInCity).orElse(null));
    return venueRepository.findAll(filters, PageRequest.of(paginationParams.getPage() - 1, paginationParams.getSize()));
  }
}
