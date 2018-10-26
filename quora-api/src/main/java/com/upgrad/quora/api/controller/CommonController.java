package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserProfileService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - Controller for user profile related methods.
 */

@Controller
public class CommonController {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserService userService;

    /**
     * Method that implements the user profile endpoint.
     *
     * @param accessToken String containing access token
     * @param userId String containing Uuid of the user
     * @return ResponseEntity that returns the user details and HTTP status
     * @throws AuthenticationFailedException in cases where the user is not signed in
     * @throws UserNotFoundException in cases where the uuid does not correspond to a registered user
     */

    @GetMapping(path =  "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile (@RequestHeader("authorization") String accessToken ,
                                                           @PathVariable String userId)
            throws AuthenticationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthTokenEntity = userProfileService
                .getUserAuthTokenEntityByAccessToken(accessToken);

        UserEntity userEntity = userService.getUserByUUID(userId);

        final UserDetailsResponse userDetailsResponse = new UserDetailsResponse().userName(userEntity.getUserName())
                .firstName(userEntity.getFirstName()).lastName(userEntity.getLastName())
                .emailAddress(userEntity.getEmailAddress()).contactNumber(userEntity.getContactNumber())
                .country(userEntity.getCountry()).dob(userEntity.getDob()).aboutMe(userEntity.getAboutMe());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
