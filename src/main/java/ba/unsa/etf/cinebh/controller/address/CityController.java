package ba.unsa.etf.cinebh.controller.address;

import ba.unsa.etf.cinebh.model.address.City;
import ba.unsa.etf.cinebh.service.address.CityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cities")
public class CityController {

  private CityService cityService;

  @GetMapping
  public ResponseEntity<Iterable<City>> getAll() {
    return ResponseEntity.ok(cityService.getAll());
  }
}
