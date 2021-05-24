package cab.snapp.warehouse.controller;

import cab.snapp.warehouse.service.model.BusinessException;
import cab.snapp.warehouse.to.ErrorTo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestErrorHandlerAdvice extends ResponseEntityExceptionHandler {


  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  public ResponseEntity<?> handleStorageFileNotFound(HttpServletRequest request,
      HttpServletResponse response, BusinessException be) {

    if (logger.isDebugEnabled()) {
      logger.debug("Business exception occurred!", be);
    }

    ErrorTo errorTo = new ErrorTo(be.getMessage(), be.getCode());

    return new ResponseEntity<>(errorTo, be.getStatus());
  }
}
