package ba.unsa.etf.cinebh.controller.auth;

import ba.unsa.etf.cinebh.dto.request.auth.ChangePasswordRequest;
import ba.unsa.etf.cinebh.dto.request.auth.UserRequest;
import ba.unsa.etf.cinebh.dto.response.UserResponse;
import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.address.City;
import ba.unsa.etf.cinebh.model.auth.User;
import ba.unsa.etf.cinebh.repository.address.CityRepository;
//import ba.unsa.etf.cinebh.service.auth.AmazonService;
import ba.unsa.etf.cinebh.service.auth.JWTService;
import ba.unsa.etf.cinebh.service.auth.UserService;
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

//  @Autowired
//  private AmazonService amazonClient;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/details")
  public ResponseEntity<UserResponse> getLoggedInUser(@RequestHeader("Authorization") String token) {
    final String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    final User user = userService.getUserByUsername(username);
    final UserResponse response = new UserResponse(user.getUserId(), user.getFirstName(), user.getLastName(), user.getPhone(), user.getPhoto(), user.getEmail(), user.getCity());
    return ResponseEntity.ok(response);
  }

  @PutMapping("/change-info")
  public ResponseEntity<String> changeUserInfo(@RequestHeader("Authorization") String token, @RequestPart(value = "photo", required = false) MultipartFile photo, @Validated @RequestPart(value = "user", required = false) UserRequest userRequest) {
    final String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setEmail(userRequest.getEmail());
    user.setPhone(userRequest.getPhone());
    City city = cityRepository.findById(userRequest.getCity()).orElseThrow(() -> new ResourceNotFoundException("City id doesn't exist!"));
    user.setCity(city);
//    if(photo != null) {
//      if(user.getPhoto() != null) amazonClient.deleteFileFromS3Bucket(user.getPhoto());
//      user.setPhoto(amazonClient.uploadFile(photo));
//    }
    userService.save(user);
    return ResponseEntity.ok("User details successfully saved!");
  }

  @PutMapping("/change-password")
  public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordRequest changePasswordRequest) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);

    if(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())){
      user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
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
