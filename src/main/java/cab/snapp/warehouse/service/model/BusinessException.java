package cab.snapp.warehouse.service.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {

  private HttpStatus status;
  private Integer code;

  public BusinessException() {
  }

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, HttpStatus status, Integer code) {
    super(message);
    this.status = status;
    this.code = code;
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }
}
