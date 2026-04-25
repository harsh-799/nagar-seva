package com.nagarseva.backend.exception;

import com.nagarseva.backend.dto.ErrorResponse;
import com.nagarseva.backend.dto.LoginUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyTaken(UsernameAlreadyTaken ex) {

        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Account cannot be created");

        Map<String, String> errors = new HashMap<>();
        errors.put("username",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Invalid Credentials");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handlePasswordUpdation(InvalidPasswordException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(InvalidUserCreation.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserCreation(InvalidUserCreation ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Account Creation Failed");

        Map<String, String> errors = new HashMap<>();
        errors.put("Role",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleEmptyRoleSelection(HttpMessageNotReadableException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Validation failed");

        Map<String, String> errors = new HashMap<>();
        errors.put("role","Role is empty or invalid");

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Ward Creation Failed");

        Map<String, String> errors = new HashMap<>();
        errors.put("councillorId",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    @ExceptionHandler(WardCouncillorRoleMismatchException.class)
    public ResponseEntity<ErrorResponse> handleWardCouncillorRoleMismatch(WardCouncillorRoleMismatchException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Ward Creation Failed");

        Map<String, String> errors = new HashMap<>();
        errors.put("councillorId",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }
    @ExceptionHandler(WardAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleWardCouncillorRoleMismatch(WardAlreadyExistsException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Ward Creation Failed");

        Map<String, String> errors = new HashMap<>();
        errors.put("wardName",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }


}
