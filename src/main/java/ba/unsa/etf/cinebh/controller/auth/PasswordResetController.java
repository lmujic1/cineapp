package ba.unsa.etf.cinebh.controller.auth;

import ba.unsa.etf.cinebh.dto.request.auth.NewPasswordRequest;
import ba.unsa.etf.cinebh.dto.request.auth.ResetPasswordRequest;
import ba.unsa.etf.cinebh.service.auth.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @PostMapping
  public ResponseEntity<String> forgotPassword(@RequestBody ResetPasswordRequest request){
    try {
      String response = passwordResetService.forgotPassword(request.getEmail());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/check-code")
  public ResponseEntity<String> checkCode(@RequestBody ResetPasswordRequest request){
    try {
      String response = passwordResetService.checkCode(request.getEmail(), request.getCode());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/new-password")
  public ResponseEntity<String> setNewPassword(@RequestBody NewPasswordRequest request){
    try {
      String response = passwordResetService.setNewPassword(request.getEmail(), request.getNewPassword(), request.getToken());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
  }
}
