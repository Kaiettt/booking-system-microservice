package com.booking.userservice.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.booking.userservice.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.EntityExistsException;

@RestControllerAdvice
public class GlobalHandler {

    /**
     * Handle entity-related exceptions like existing emails or duplicate records.
     */
    @ExceptionHandler({
            EntityExistsException.class,
            EmailAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse<Object>> badRequestException(RuntimeException exception) {
        ErrorResponse<Object> res = new ErrorResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(exception.getMessage());
        res.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Handle validation errors from @Valid annotated request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        ErrorResponse<Object> res = new ErrorResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Bad request");

        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        res.setMessage(errors.size() > 1 ? errors : errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Handle authentication failures like wrong username/password or missing account.
     */
    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ErrorResponse<Object>> handleAuthenticationException(Exception ex) {
        ErrorResponse<Object> res = new ErrorResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value()); // 401
        res.setError(ex.getMessage());
        res.setMessage("Authentication failed.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    /**
     * Handle access-denied errors when a user lacks permissions.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse<Object>> handleUnauthorizedAccessException(AccessDeniedException ex) {
        ErrorResponse<Object> res = new ErrorResponse<>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value()); // 403
        res.setError(ex.getMessage());
        res.setMessage("Unauthorized access. You do not have permission to access this resource.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }
}
