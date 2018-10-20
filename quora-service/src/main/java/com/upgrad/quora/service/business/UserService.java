package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - This class is the Service class for user related methods.
 */

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method to create a new user.
     * @param userEntity the UserEntity to be created
     * @return created UserEntity
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(final UserEntity userEntity) {

        // If password is null, we will give the user a default password
        String password = userEntity.getPassword();
        if(password == null) {

            userEntity.setPassword("quora@123");
        }

        // Generate salt and encrypt the password before creating the user
        String[] encryptPassword = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        String salt = encryptPassword[0];
        userEntity.setSalt(salt);
        String encryptedPassword = PasswordCryptographyProvider.encrypt(encryptPassword[1],salt);
        userEntity.setPassword(encryptedPassword);
        return userDao.createUser(userEntity);
    }

    /**
     * Method to get user by username.
     * @param userName the username of the user we are trying to find
     * @return UserEntity of given user
     */

    public UserEntity getUserByUserName(String userName){

        return userDao.findUserByUserName(userName);
    }

    /**
     * Method to get user by email.
     * @param email email of the user we are trying to search for
     * @return UserEntity of user with given email
     */

    public UserEntity getUserByEmail(String email) {

        return userDao.findUserByEmail(email);
    }

}
