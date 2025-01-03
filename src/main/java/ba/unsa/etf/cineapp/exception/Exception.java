package ba.unsa.etf.cineapp.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class Exception {
  private final String message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime timeStamp;

  public Exception(final String message, final HttpStatus httpStatus, final ZonedDateTime timeStamp) {
    this.message = message;
    this.httpStatus = httpStatus;
    this.timeStamp = timeStamp;
  }

  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public ZonedDateTime getTimeStamp() {
    return timeStamp;
  }
}

