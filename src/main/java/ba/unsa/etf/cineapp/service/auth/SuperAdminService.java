package ba.unsa.etf.cineapp.service.auth;

import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.auth.Role;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.repository.auth.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminService {
  @Autowired
  private final UserRepository userRepository;

  public Page<User> getUsers(final int pageNumber, final int size) {
  List<Role> roles = List.of(Role.ADMIN, Role.USER);
    return userRepository.findAllByRoleIn(roles, PageRequest.of(pageNumber - 1, size));
  }

  public User save(final User user) {
    return userRepository.save(user);
  }

  public void remove(final Long id) throws JsonProcessingException {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User with id= " + id + " does not exist");
    }
    userRepository.deleteById(id);
  }

  public User getUserData(final Long id) {
    return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with provided id not found!"));
  }
}
