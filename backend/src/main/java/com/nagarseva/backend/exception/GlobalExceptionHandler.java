package com.nagarseva.backend.exception;

import com.nagarseva.backend.dto.ErrorResponse;
import com.nagarseva.backend.dto.LoginUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleLogin_RegisterValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Validation failed");
        resp.setErrors(errors);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(UsernameAlreadyTaken.class)
    public ResponseEntity<LoginUserResponse> handleUsernameAlreadyTaken(UsernameAlreadyTaken ex) {
        LoginUserResponse response = new LoginUserResponse();
        response.setSuccess(false);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<LoginUserResponse> handleBadCredentials(BadCredentialsException ex) {
        LoginUserResponse response = new LoginUserResponse();
        response.setSuccess(false);
        response.setMessage("Invalid Credentials!");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handlePasswordUpdation(InvalidPasswordException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }
}
