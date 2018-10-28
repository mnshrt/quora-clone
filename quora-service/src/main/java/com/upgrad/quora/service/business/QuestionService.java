package com.upgrad.quora.service.business;

import com.sun.media.jfxmedia.logging.Logger;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.time.ZonedDateTime;
import java.util.List;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity checkQuestion(String accessToken, String questionId) throws AuthorizationFailedException,InvalidQuestionException {
       //String test ="eyJraWQiOiJjNTBjOTA5Yy1kZDI1LTQwMTEtODg4YS02M2JhY2NmMTFmOGMiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdWQiOiI0NmI2NzQ0OS05Y2ZhLTQ3OGQtYmYxYy01MGUxMTM2YzE1M2MiLCJpc3MiOiJodHRwczovL3F1b3JhLmlvIiwiZXhwIjoxNTQwNzA0LCJpYXQiOjE1NDA2NzV9.yLtGjQhuMx2YscB6U7zxIRU2FL4yISLlS6GIfs3YzzbFT45rxoZ5sdFcZivV3riC4WLWwa0JUIpffBkVnSZ_RA";

        UserAuthTokenEntity userAuthTokenEntity =userDao.findUserAuthTokenEntityByAccessToken(accessToken);

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

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
          return questionDao.updateQuestion(questionEntity);
    }

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
                     return(questionId);
                }
            }
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(String accessToken, String userId) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);


        if(userDao.findUserByUUID(userId)== null){

            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");

        }else if (userAuthTokenEntity == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to get all questions posted by a specific user");
            } else {

                UserEntity userEntity = userAuthTokenEntity.getUser();
               // long user_Id = userAuthTokenEntity.getUser().getId();
                List<QuestionEntity> questionEntityList = questionDao.getQuestionByUser(userEntity);

                return questionEntityList;
            }
        }
    }
}
