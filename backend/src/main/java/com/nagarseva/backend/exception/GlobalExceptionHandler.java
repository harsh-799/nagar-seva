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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

        if (errors.get("issueType") != null && errors.get("issueType").contains("Failed to convert property value of type 'java.lang.String")) {
            errors.put("issueType", "Kindly Select Issues From [ WATER, ELECTRICITY, SEWAGE, GARBAGE, OTHER ]");
        }

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

    @ExceptionHandler(InvalidWard.class)
    public ResponseEntity<ErrorResponse> handleInvalidWard(InvalidWard ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Account Creation Failed");

        Map<String, String> errors = new HashMap<>();
        errors.put("wardId",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }

    @ExceptionHandler(CouncillorAlreadyAssignedException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWard(CouncillorAlreadyAssignedException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Ward creation failed");

        Map<String, String> errors = new HashMap<>();
        errors.put("councillorId",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleImageUploadFailure(FileUploadException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(ComplaintNotExistException.class)
    public ResponseEntity<ErrorResponse> handleInvalidComplaint(ComplaintNotExistException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Complaint not found");

        Map<String, String> errors = new HashMap<>();
        errors.put("complaintId",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    @ExceptionHandler(UserMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidComplaint(UserMismatchException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("You are not allowed to access this complaint");

        Map<String, String> errors = new HashMap<>();
        errors.put("userId",ex.getMessage());

        resp.setErrors(errors);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
    }

    @ExceptionHandler(NoFieldProvidedException.class)
    public ResponseEntity<ErrorResponse> handleUpdateComplaintInvalidInput(NoFieldProvidedException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(ComplaintModificationForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleUpdateComplaintAlreadyVerified(ComplaintModificationForbiddenException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(ImageDeletionFailedException.class)
    public ResponseEntity<ErrorResponse> handleImageDeletionWhileUpdatingComplaint(ImageDeletionFailedException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserRole(InvalidUserRoleException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserRole(MethodArgumentTypeMismatchException ex) {
        ErrorResponse resp = new ErrorResponse();
        resp.setSuccess(false);
        resp.setMessage("Invalid issue type or status provided in request URL.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }












}
