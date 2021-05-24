package cab.snapp.warehouse.service.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BusinessException {

  public ValidationException() {
  }

  public ValidationException(String message, HttpStatus status, Integer code) {
    super(message, status, code);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
