package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

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



/*    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions() {
        return questionDao.getAllQuestions();
    }*/

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity checkQuestion(String questionId, String accessToken) throws AuthorizationFailedException,InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.findUserAuthTokenEntityByAccessToken(accessToken);


        if (userAuthTokenEntity == null) {

            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        } else {

            String logoutAt = String.valueOf(userDao.findUserAuthTokenEntityByAccessToken(accessToken)
                    .getLogoutAt());

            if (!logoutAt.equals("null")) {

                throw new AuthorizationFailedException("ATHR-002",
                        "User is signed out.Sign in first to edit the question");
            } else {

                String user_id = userAuthTokenEntity.getUser().getUuid();


                QuestionEntity existingQuestionEntity = questionDao.getQuestionById(questionId);

                if (existingQuestionEntity == null) {
                    throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
                } else if (!user_id.equals(existingQuestionEntity.getUuid())) {
                    throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
                } else {
                    return existingQuestionEntity;
                }
            }
        }
    }

    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
          return questionDao.updateQuestion(questionEntity);
    }
}
