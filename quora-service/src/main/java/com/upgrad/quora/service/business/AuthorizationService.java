package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthorizationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    UserService userService;

    /**
     * Method to authorize a user based on the given access token
     *
     * @param accessToken assigned to the User
     * @return UserAuthTokenEntity which has the authorisation details of the user
     * @throws AuthorizationFailedException
     */
    public UserAuthTokenEntity getUserAuthTokenEntity(String accessToken) throws AuthorizationFailedException {

        if (userDao.findUserAuthTokenEntityByAccessToken(accessToken) == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to get user details");
            } else {

                return userDao.findUserAuthTokenEntityByAccessToken(accessToken);
            }

        }
    }

}
