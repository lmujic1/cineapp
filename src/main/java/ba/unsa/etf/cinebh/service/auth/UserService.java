package ba.unsa.etf.cinebh.service.auth;

import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.auth.User;
import ba.unsa.etf.cinebh.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  @Autowired
  private final UserRepository userRepository;

  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      }
    };
  }

  public User save(final User user) {
    return userRepository.save(user);
  }

  public User getUserData(final Long id) {
    return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with provided id not found!"));
  }

  public User getUserByUsername(final String username) {
    return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User with provided email not found!"));
  }
}
