package ba.unsa.etf.cineapp;

import ba.unsa.etf.cineapp.model.auth.Role;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class CineappApplication implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(CineappApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    User adminAccount = userRepository.findByRole(Role.SUPERADMIN);
    if(adminAccount == null) {
      User user = new User();
      user.setEmail("lejlamujic998@gmail.com");
      user.setFirstName("Lejla");
      user.setLastName("Mujic");
      user.setRole(Role.SUPERADMIN);
      user.setPassword(new BCryptPasswordEncoder().encode("admin"));
      userRepository.save(user);
    }
  }
}
