package ba.unsa.etf.cineapp.controller.auth;

import ba.unsa.etf.cineapp.dto.request.auth.UserRequest;
import ba.unsa.etf.cineapp.dto.request.other.EmailRequest;
import ba.unsa.etf.cineapp.dto.request.other.PaginationParams;
import ba.unsa.etf.cineapp.model.address.City;
import ba.unsa.etf.cineapp.model.auth.Role;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.service.address.CityService;
import ba.unsa.etf.cineapp.service.auth.AmazonService;
import ba.unsa.etf.cineapp.service.auth.JWTService;
import ba.unsa.etf.cineapp.service.auth.PasswordService;
import ba.unsa.etf.cineapp.service.auth.SuperAdminService;
import ba.unsa.etf.cineapp.service.other.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/super-admin")
public class SuperAdminController {
  private SuperAdminService superAdminService;
  private CityService cityService;

  @Autowired
  private AmazonService amazonClient;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

  @Autowired
  private JWTService jwtService;

  @GetMapping("/users")
  public ResponseEntity<Page<User>> getUsers(PaginationParams paginationParams) {
    return ResponseEntity.ok(superAdminService.getUsers(paginationParams.getPage(), paginationParams.getSize()));
  }

  @PostMapping("/user/new")
  public ResponseEntity<String> createUser(@RequestPart(value = "image", required = false) MultipartFile image, @Validated @RequestPart(value = "user", required = false) UserRequest userRequest) {
    User user = new User();
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setPhone(userRequest.getPhone());
    user.setEmail(userRequest.getEmail());
    user.setRole(Role.ADMIN);
    user.setAddedBySuperAdmin(true);
    user.setPasswordChanged(false);
    final String password = PasswordService.generateRandomPassword(12);
    user.setPassword(passwordEncoder.encode(password));

    final City city = cityService.findById(userRequest.getCity());
    user.setCity(city);

    if (image != null) {
      user.setImage(amazonClient.uploadFile(image));
    }

    superAdminService.save(user);

    var jwt = jwtService.generateToken(user);

    String resetLink = "http://localhost:5173/user-profile/password?token=" + jwt;

    emailService.sendEmail(
        new EmailRequest(
            user.getEmail(),
            "Welcome to Cineapp - Your New Password",
            "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
            "Congratulations! You have been added as a new administrator for Cineapp by our superadmin. Below is your initial password which you will use to access your admin account for the first time:\n\n" +
            "**Password: " + password + "**\n\n" + // Include the generated password
            "For security purposes, please ensure to change this password immediately after your first login. You can do this by visiting your account settings or clicking the following link to set a new password of your choice:\n" +
            resetLink + "\n\n" + // Include a link to reset the password
            "Please note, for security reasons, it is recommended to change this password immediately upon login. The above link will remain active for 24 hours. If you do not change your password within this timeframe, you will need to request a new link from your superadmin.\n\n" +
            "If you have any questions or require further assistance, please do not hesitate to contact our support team at lejladevmujic@gmail.com.\n\n" +
            "Thank you for your attention, and we look forward to a successful collaboration.\n\n" +
            "Warm regards,\n\n" +
            "Cineapp Support Team\n" +
            "lejladevmujic@gmail.com"
        )
    );


    return ResponseEntity.ok("New admin user successfully created!");
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable long id) throws JsonProcessingException {
    final User user = superAdminService.getUserData(id);
    final String photoUrl = user.getImage();
    amazonClient.deleteFileFromS3Bucket(photoUrl);

    superAdminService.remove(id);
    return ResponseEntity.ok("User successfully deleted!");
  }

  @PutMapping("/user/{id}/deactivate-account")
  public ResponseEntity<String> deactivateUserAccount(@PathVariable long id) {
    User user = superAdminService.getUserData(id);

    user.setSoftDelete(true);
    superAdminService.save(user);
    return ResponseEntity.ok("User deactivated!");
  }
}
