package ba.unsa.etf.cineapp.controller.venue;

import ba.unsa.etf.cineapp.dto.request.other.PaginationParams;
import ba.unsa.etf.cineapp.dto.request.venue.VenueFilterParams;
import ba.unsa.etf.cineapp.dto.request.venue.VenueRequest;
import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.address.City;
import ba.unsa.etf.cineapp.model.venue.Venue;
import ba.unsa.etf.cineapp.service.address.CityService;
import ba.unsa.etf.cineapp.service.auth.AmazonService;
import ba.unsa.etf.cineapp.service.venue.VenueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/venues")
public class VenueController {

  ObjectMapper objectMapper;
  @Autowired
  private VenueService venueService;
  @Autowired
  private CityService cityService;

  private AmazonService amazonClient;

  @GetMapping("/all")
  public ResponseEntity<Iterable<Venue>> getAll() {
    return ResponseEntity.ok(venueService.getAll());
  }

  @GetMapping
  public ResponseEntity<Page<Venue>> getVenues(PaginationParams paginationParams) {
    return ResponseEntity.ok(venueService.getVenues(paginationParams.getPage(), paginationParams.getSize()));
  }

  @GetMapping("/search")
  public ResponseEntity<Page<Venue>> getVenuesForFilter(VenueFilterParams filterParams, PaginationParams paginationParams) {
    return ResponseEntity.ok(venueService.getVenuesByFilter(filterParams, paginationParams));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Venue> getVenueById(@PathVariable("id") Long id) {
    Venue venue = venueService.findById(id);
    return ResponseEntity.ok().body(venue);
  }

  @GetMapping("/city/{id}")
  public ResponseEntity<Iterable<Venue>> getVenuesByCity(@PathVariable("id") Long id) {
    City city = cityService.findById(id);
    return ResponseEntity.ok(venueService.getVenuesByCity(city));
  }

  @PostMapping
  public ResponseEntity<String> createVenue(@RequestPart(value = "image", required = false) MultipartFile image, @Validated @RequestPart(value = "venue", required = false) VenueRequest venueRequest) {
    City city = cityService.findById(venueRequest.getCity());
    String imageUrl = amazonClient.uploadFile(image);
    Venue venue = new Venue(venueRequest.getName(), imageUrl, venueRequest.getStreet(), venueRequest.getNumber(), venueRequest.getTelephone(), city);
    venueService.save(venue);
    return new ResponseEntity<>("Venue successfully added!", HttpStatus.CREATED);
  }

  @PostMapping("/{id}")
  public ResponseEntity<String> updateVenue(@PathVariable Long id, @RequestPart(value = "image", required = false) MultipartFile image, @Validated @RequestPart(value = "venue", required = false) VenueRequest venueRequest) {
    Venue updateVenue = venueService.findById(id);
    updateVenue.setName(venueRequest.getName());
    updateVenue.setStreet(venueRequest.getStreet());
    updateVenue.setStreetNumber(venueRequest.getNumber());
    updateVenue.setTelephone(venueRequest.getTelephone());
    City newCity = cityService.findById(venueRequest.getCity());
    updateVenue.setCity(newCity);
    if (image != null) {
      if(updateVenue.getImage() != null) amazonClient.deleteFileFromS3Bucket(updateVenue.getImage());
      updateVenue.setImage(amazonClient.uploadFile(image));
    }
    venueService.save(updateVenue);
    return new ResponseEntity<>("Venue with id = " + id + " successfully updated!", HttpStatus.OK);
  }

  private Venue applyPatchToVenue(JsonPatch patch, Venue targetVenue) throws JsonPatchException, JsonProcessingException {
    JsonNode patched = patch.apply(objectMapper.convertValue(targetVenue, JsonNode.class));
    return objectMapper.treeToValue(patched, Venue.class);
  }

  @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
  public ResponseEntity<String> updateVenue(@PathVariable Long id, @RequestBody JsonPatch patch) {
    try {
      Venue venue = venueService.findById(id);
      Venue venuePatched = applyPatchToVenue(patch, venue);
      venueService.save(venuePatched);
      return new ResponseEntity<>("Venue with id = " + id + " successfully updated!", HttpStatus.OK);
    } catch (JsonPatchException | JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteVenue(@PathVariable long id) throws JsonProcessingException {
    Venue venue = venueService.findById(id);
    String photoUrl = venue.getImage();
    amazonClient.deleteFileFromS3Bucket(photoUrl);
    venueService.remove(id);
    return new ResponseEntity<>("Venue successfully deleted!", HttpStatus.OK);
  }
}
