package ba.unsa.etf.cineapp.controller.address;

import ba.unsa.etf.cineapp.model.address.Country;
import ba.unsa.etf.cineapp.service.address.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {

  private CountryService countryService;

  @GetMapping
  public ResponseEntity<Iterable<Country>> getAll() {
    return ResponseEntity.ok(countryService.getAll());
  }
}

