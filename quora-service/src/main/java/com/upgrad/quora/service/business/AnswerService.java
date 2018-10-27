package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidAnswerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    AnswerDao answerDao;

    /**
     * Method to create a new user.
     *
     * @param answerEntity the AnswerEntity to be created
     * @return created UserEntity
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final AnswerEntity answerEntity) {

        return answerDao.createAnswer(answerEntity);

    }




    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity checkAnswer(String answerId, String accessToken) throws AuthorizationFailedException,InvalidAnswerException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);


        if (userAuthTokenEntity == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to edit the answer");
            } else {

                String user_id = userAuthTokenEntity.getUser().getUuid();


                AnswerEntity existingAnswerEntity = answerDao.getAnswerById(answerId);

                if (existingAnswerEntity == null) {
                    throw new InvalidAnswerException("ANS-001", "Entered answer uuid does not exist");
                } else if (!user_id.equals(existingAnswerEntity.getUuid())) {
                    throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
                } else {
                    return existingAnswerEntity;
                }
            }
        }
    }

    public AnswerEntity updateAnswer(AnswerEntity answerEntity) {
        return answerDao.updateAnswer(answerEntity);
    }

    public String deleteAnswer(String answerId, String accessToken) throws AuthorizationFailedException, InvalidAnswerException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);


        if (userAuthTokenEntity == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to delete the answer");
            } else {

                String user_id = userAuthTokenEntity.getUser().getUuid();


                AnswerEntity existingAnswerEntity = answerDao.getAnswerById(answerId);

                if (existingAnswerEntity == null) {
                    throw new InvalidAnswerException("ANS-001", "Entered answer uuid does not exist");
                } else if (!user_id.equals(existingAnswerEntity.getUuid())) {
                    throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can delete the answer");
                } else {
                    answerDao.deleteAnswerByUUID(answerId);
                    return (answerId);
                }
            }
        }


    }


}
