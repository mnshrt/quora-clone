package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - DAO class with operations for the Users table
 */

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to create user in the DB.
     * @param userEntity UserEntity with details of the user to be created
     * @return UserEntity of the created user
     */

    public UserEntity createUser(UserEntity userEntity) {

        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * Method to find a user by username in the DB.
     * @param userName the username of the user we are trying to find
     * @return If the user is found, return UserEntity of the given user, else return null
     */

    public UserEntity findUserByUserName(String userName) {

        try {

            String query = "select u from UserEntity u where u.userName = :username";
            return entityManager.createQuery(query, UserEntity.class)
                    .setParameter("username", userName).getSingleResult();
        } catch (NoResultException nre) {

            return null;
        }

    }

    /**
     * Method to find a user by email in the DB.
     * @param email the email of the user we are trying to find
     * @return If a user with the given email is found, return UserEntity of that user, else return null
     */

    public UserEntity findUserByEmail(String email) {

        try{

            String query = "select u from UserEntity u where u.emailAddress = :email";
            return entityManager.createQuery(query, UserEntity.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {

            return null;
        }
    }

    public UserEntity findUserByUUID(String uuid) {

        try {
            String query = "select u from UserEntity u where u.uuid = :uuid";
            return entityManager.createQuery(query, UserEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }

    }

    /**
     * Method to delete user by UUID in the db.
     *
     * @param uuid String containing UUID of the user to be deleted.
     */

    public void deleteUserByUUID(String uuid) {

        String query = "delete from UserEntity u where u.uuid = :uuid";

        Query finalQuery = entityManager.createQuery(query)
                .setParameter("uuid", uuid);
        finalQuery.executeUpdate();
    }

    /**
     * Method to create auth token in DB.
     *
     * @param userAuthTokenEntity userAuthTokenEntity with the token to be created
     * @return UserAuthTokenEntity of the created auth token
     */

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {

        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    /**
     * Method to get UserAuthTokenEntity from the db given an access token.
     *
     * @param accessToken String containing access token of user
     * @return UserAuthTokenEntity containing the given access token
     */

    public UserAuthTokenEntity findUserAuthTokenEntityByAccessToken(String accessToken) {

        try{
            String query = "select u from UserAuthTokenEntity u where u.accessToken = :token";
            return entityManager.createQuery(query, UserAuthTokenEntity.class)
                    .setParameter("token", accessToken).getSingleResult();
        } catch (NoResultException nre) {

            return null;
        }

    }
}
