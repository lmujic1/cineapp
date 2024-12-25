package ba.unsa.etf.cineapp.controller.auth;

import ba.unsa.etf.cineapp.dto.request.auth.ChangePasswordRequest;
import ba.unsa.etf.cineapp.dto.request.auth.UserRequest;
import ba.unsa.etf.cineapp.dto.response.UserResponse;
import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.address.City;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.repository.address.CityRepository;
import ba.unsa.etf.cineapp.service.auth.AmazonService;
import ba.unsa.etf.cineapp.service.auth.JWTService;
import ba.unsa.etf.cineapp.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final JWTService jwtService;

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private AmazonService amazonClient;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/details")
  public ResponseEntity<UserResponse> getLoggedInUser(@RequestHeader("Authorization") String token) {
    final String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    final User user = userService.getUserByUsername(username);
    final UserResponse response = new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(), user.getPhone(), user.getImage(), user.getEmail(), user.getCity());
    return ResponseEntity.ok(response);
  }

  @PutMapping("/change-info")
  public ResponseEntity<String> changeUserInfo(@RequestHeader("Authorization") String token, @RequestPart(value = "image", required = false) MultipartFile image, @Validated @RequestPart(value = "user", required = false) UserRequest userRequest) {
    final String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setEmail(userRequest.getEmail());
    user.setPhone(userRequest.getPhone());
    City city = cityRepository.findById(userRequest.getCity()).orElseThrow(() -> new ResourceNotFoundException("City id doesn't exist!"));
    user.setCity(city);
    if (image != null) {
      if (user.getImage() != null) {
        amazonClient.deleteFileFromS3Bucket(user.getImage());
      }
      user.setImage(amazonClient.uploadFile(image));
    }
    userService.save(user);
    return ResponseEntity.ok("User details successfully saved!");
  }

  @PutMapping("/change-password")
  public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordRequest changePasswordRequest) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);

    if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
      user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
      user.setPasswordChanged(changePasswordRequest.getPasswordChanged());
      userService.save(user);
      return ResponseEntity.ok("Password successfully changed!");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong old password");
    }
  }

  @PutMapping("/deactivate-account")
  public ResponseEntity<String> deactivate(@RequestHeader("Authorization") String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);

    user.setSoftDelete(true);
    userService.save(user);
    return ResponseEntity.ok("User deactivated!");
  }
}
