package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - Controller for user related methods.
 */

@Controller
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Method that implements the user signup endpoint.
     * @param signupUserRequest to get user credentials
     * @return ResponseEntity to indicate whether sign up is successful or not
     * @throws SignUpRestrictedException in cases where username already exists, or email is already registered
     */

    @PostMapping(path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signUp(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        final UserEntity userEntity = new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setRole("nonadmin");
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmailAddress(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());


        String userNameExists = String.valueOf(userService.getUserByUserName(signupUserRequest.getUserName()));
        String emailExists = String.valueOf(userService.getUserByEmail(signupUserRequest.getEmailAddress()));


        // If username exists or user with given email exists, throw SignUpRestrictedException
        // Else, create user and send response
        if (!userNameExists.equals("null")) {

            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        } else if (!emailExists.equals("null")) {

            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        } else {

            final UserEntity createdUserEntity = userService.createUser(userEntity);
            SignupUserResponse userResponse = new SignupUserResponse()
                    .id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");

            return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
        }

    }

}
