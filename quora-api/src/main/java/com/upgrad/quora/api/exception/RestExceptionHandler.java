package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - ExceptionHandler for all the exceptions to be implemented.
 */

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Method that implements the exception handler for the SignUpRestrictedException.
     *
     * @param ex      instance of SignUpRestrictedException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException ex,
                                                                   WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Method that implements the exception handler for AuthenticationFailedException
     *
     * @param ex      instance of AuthenticationFailedException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException ex,
                                                                       WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

    /**
     * Method that implements exception handler for SignOutRestrictedException
     *
     * @param ex      instance of SignOutRestrictedException
     * @param request instance of WebRequest
     * @return ResponseEntity with error response
     */

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException ex,
                                                                    WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode())
                .message(ex.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Method that implements exception handler for UserNotFoundException
     *
     * @param ex      instance of UserNotFoundException
     * @param request instance of WebRequest
     * @return ResponseEntity with error response
     */

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ex,
                                                               WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.NOT_FOUND);
    }


    /**
     * Method that implements exception handler for AuthorizationFailedException
     *
     * @param ex      instance of AuthorizationFailedException
     * @param request instance of WebRequest
     * @return ResponseEntity with error response
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException ex,
                                                                      WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

    /**
     * Method that implements exception handler for
     * @param ex      instance of InvalidQuestionException
     * @param request instance of WebRequest
     * @return ResponseEntity with error response
     */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException ex,
                                                                  WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

}
