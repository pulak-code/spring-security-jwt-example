package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.ErrorResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidCredentialsException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UnauthorizedAccessException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserAlreadyExistsException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserNotFoundException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserServiceException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.WeakPasswordException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
			WebRequest request) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex,
			WebRequest request) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(WeakPasswordException.class)
	public ResponseEntity<ErrorResponse> handleWeakPasswordException(UserAlreadyExistsException ex,
			WebRequest request) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(UserServiceException.class)
	public ResponseEntity<ErrorResponse> handle(UserServiceException ex, WebRequest request) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				message);
		return new ResponseEntity<>(response, status);
	}

}
