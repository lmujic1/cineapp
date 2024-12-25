package ba.unsa.etf.cineapp.controller.movie;

import ba.unsa.etf.cineapp.dto.request.movie.ProfessionalRequest;
import ba.unsa.etf.cineapp.model.movie.Profession;
import ba.unsa.etf.cineapp.model.movie.Professional;
import ba.unsa.etf.cineapp.service.movie.ProfessionalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/professionals")
public class ProfessionalController {
  @Autowired
  private ProfessionalService professionalService;

  @GetMapping("/actors")
  public ResponseEntity<Iterable<Professional>> getAllActors() {
    return ResponseEntity.ok(professionalService.getAllActors());
  }

  @GetMapping("/writers")
  public ResponseEntity<Iterable<Professional>> getAllWriters() {
    return ResponseEntity.ok(professionalService.getAllWriters());
  }

  @GetMapping("/actor/{id}")
  public ResponseEntity<Professional> getActorById(@PathVariable("id") Long id) {
    final Professional newActor = professionalService.findById(id);
    return ResponseEntity.ok().body(newActor);
  }

  @GetMapping("/writer/{id}")
  public ResponseEntity<Professional> getWriterById(@PathVariable("id") Long id) {
    final Professional newWriter = professionalService.findById(id);
    return ResponseEntity.ok().body(newWriter);
  }

  @PostMapping("/actor")
  public ResponseEntity<String> createActor(@Validated @RequestBody ProfessionalRequest professionalRequest) {
    professionalService.save(new Professional(professionalRequest.getFirstName(), professionalRequest.getLastName(), Profession.ACTOR));
    return new ResponseEntity<>("Actor successfully added!", HttpStatus.CREATED);
  }

  @PostMapping("/writer")
  public ResponseEntity<String> createWriter(@Validated @RequestBody ProfessionalRequest professionalRequest) {
    professionalService.save(new Professional(professionalRequest.getFirstName(), professionalRequest.getLastName(), Profession.WRITER));
    return new ResponseEntity<>("Actor successfully added!", HttpStatus.CREATED);
  }
}
