package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Manish Rout (https://github.com/mnshrt)
 * Description - This class is the Service class for question related methods.
 */
@Service
public class QuestionService {

    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    AuthorizationService authorizationService;

    /**
     * Method to create a new user.
     *
     * @param questionEntity the QuestionEntity to be created
     * @return created UserEntity
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final QuestionEntity questionEntity) {

        return questionDao.createQuestion(questionEntity);

    }

    /**
     * Method to get all the questions
     *
     * @param accessToken accessToken assigned to the user
     * @return List<QuestionEntity> list of all the questions associated with the user
     * @throws AuthorizationFailedException
     */


    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {
            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to get all questions");
            } else {
                return questionDao.getAllQuestions();
            }
        }


    }

    /**
     * @param accessToken accessToken assigned to the user
     * @param questionId  the uuid of the question
     * @return QuestionEntity
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity checkQuestion(String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);

        if (userAuthTokenEntity == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {

            String logoutAt = String.valueOf(userAuthTokenEntity
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to edit the question");
            } else {

                UserEntity user = userAuthTokenEntity.getUser();


                QuestionEntity existingQuestionEntity = questionDao.getQuestionById(questionId);

                if (existingQuestionEntity == null) {
                    throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
                } else if (!user.equals(existingQuestionEntity.getUser())) {
                    throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
                } else {
                    return existingQuestionEntity;
                }
            }
        }
    }

    /**
     * Method to update a question
     *
     * @param questionEntity to be updated
     * @return updated questionEntity
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
        return questionDao.updateQuestion(questionEntity);
    }

    /**
     * Method to delete a given question
     *
     * @param questionId  uuid of the question to be deleted
     * @param accessToken accessId assigned to the user
     * @return uuid of the deleted question
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteQuestion(String questionId, String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);


        if (userAuthTokenEntity == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to delete the question");
            } else {

                UserEntity user = userAuthTokenEntity.getUser();


                QuestionEntity existingQuestionEntity = questionDao.getQuestionById(questionId);

                if (existingQuestionEntity == null) {
                    throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
                } else if (!user.equals(existingQuestionEntity.getUser())) {
                    throw new AuthorizationFailedException("ATHR-003", "Only the question owner can delete the question");
                } else {
                    questionDao.deleteUserByUUID(questionId);
                    return (questionId);
                }
            }
        }
    }

    /**
     * Method to get all the questions by a given user
     *
     * @param accessToken accessToken assigned to the user
     * @param userId      uuid of the user
     * @return List<QuestionEntity> list of all the questions by the corresponding user
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(String accessToken, String userId) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);


        if (userDao.findUserByUUID(userId) == null) {

            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");

        } else if (userAuthTokenEntity == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to get all questions posted by a specific user");
            } else {

                UserEntity userEntity = userAuthTokenEntity.getUser();

                List<QuestionEntity> questionEntityList = questionDao.getQuestionByUser(userEntity);

                return questionEntityList;
            }
        }
    }
}
