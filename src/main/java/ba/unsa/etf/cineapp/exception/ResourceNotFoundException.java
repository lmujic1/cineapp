package ba.unsa.etf.cineapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException{
  public ResourceNotFoundException(final String message) { super(message); }

  public ResourceNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
