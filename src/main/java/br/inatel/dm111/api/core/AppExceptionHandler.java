package br.inatel.dm111.api.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<AppError> handleEntity(ApiException exception, WebRequest request) {
        return new ResponseEntity<>(buildError(exception), exception.getStatus());
    }

    private AppError buildError(ApiException exception) {
        return new AppError(exception.getErrorCode(), exception.getMessage());
    }
}
