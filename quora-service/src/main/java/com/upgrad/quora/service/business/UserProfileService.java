package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - This class is the Service class for user profile related methods.
 */

@Service
public class UserProfileService {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    /**
     * Method to get UserAuthTokenEntity by access token.
     *
     * @param accessToken String containing access token
     * @return UserAuthTokenEntity corresponding to the access token
     * @throws AuthenticationFailedException in cases where the user has not signed in or is signed out.
     */

    public UserAuthTokenEntity getUserAuthTokenEntityByAccessToken(String accessToken) throws AuthenticationFailedException {

        if (userDao.findUserAuthTokenEntityByAccessToken(accessToken) == null) {

            throw new AuthenticationFailedException("ATHR-001", "User has not signed in");
        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthenticationFailedException("ATHR-002",
                        "User is signed out.Sign in first to get user details");
            } else {

                return userDao.findUserAuthTokenEntityByAccessToken(accessToken);
            }

        }
    }
}
