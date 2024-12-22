package ba.unsa.etf.cinebh.controller.address;

import ba.unsa.etf.cinebh.model.address.Country;
import ba.unsa.etf.cinebh.service.address.CountryService;
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

