package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Manish Rout (https://github.com/mnshrt)
 * Description - DAO class with operations for the Question table
 */

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to create a new QuestionEntity
     *
     * @param questionEntity contains the questionEntity to be persisted
     * @return QuestionEntity that has been persisted in the database
     */

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * Method to get the List of All the questions
     *
     * @return List<QuestionEntity>
     */

    public List<QuestionEntity> getAllQuestions() {

        try {

            String query = "select q from QuestionEntity q";
            return entityManager.createQuery(query, QuestionEntity.class)
                    .getResultList();
        } catch (NoResultException nre) {

            return null;
        }
    }

    /**
     * Method to get the QuestionEntity by uuid
     *
     * @param questionId
     * @return QuestionEntity
     */

    public QuestionEntity getQuestionById(String questionId) {
        try {
            String query = "select u from QuestionEntity u where u.uuid = :uuid";
            return entityManager.createQuery(query, QuestionEntity.class)
                    .setParameter("uuid", questionId).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }
    }

    /**
     * Method to update the content of the Question
     *
     * @param questionEntity
     * @return QuestionEntity after updating
     */

    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {

        entityManager.merge(questionEntity);
        return questionEntity;

    }

    /**
     * Method to delete a question associated with the uuid
     *
     * @param uuid of the question to be deleted
     */

    public void deleteUserByUUID(String uuid) {

        String query = "delete from QuestionEntity u where u.uuid = :uuid";
        //dont mention the entity classname in createQuery() for deletion as it doesnt return data just the rows affected
        Query finalQuery = entityManager.createQuery(query)
                .setParameter("uuid", uuid);
        int rowsAffected = finalQuery.executeUpdate();
    }


    /**
     * @param user UserEntity to compare in Question table
     * @return List<QuestionEntities> associated with the given UserEntity
     */


    public List<QuestionEntity> getQuestionByUser(UserEntity user) {
        try {
            //use u.user rather than u.u_id otherwise throws IllegalArgumentException
            //here user is the UserEntity property in the QuestionEntity class
            String query = "select u from QuestionEntity u where u.user = :userInput";
            return entityManager.createQuery(query, QuestionEntity.class)
                    .setParameter("userInput", user).getResultList();

        } catch (NoResultException nre) {

            return null;
        }
    }
}
