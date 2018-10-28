package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity updateAnswer(AnswerEntity answerEntity) {

        return entityManager.merge(answerEntity);

    }

    public AnswerEntity getAnswerById(String questionId) {
        try {
            String query = "select u from AnswerEntity u where u.uuid = :uuid";
            return entityManager.createQuery(query, AnswerEntity.class)
                    .setParameter("uuid", questionId).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }
    }

    public void deleteAnswerByUUID(String uuid) {

        String query = "delete from AnswerEntity u where u.uuid = :uuid";

        Query finalQuery = entityManager.createQuery(query)
                .setParameter("uuid", uuid);
        int rowsAffected = finalQuery.executeUpdate();
    }


    public List<AnswerEntity> getAllAnswersToQuestion(QuestionEntity questionEntity) {
        try {

            String query = "select u from AnswerEntity u where u.question = :userInput";
            return entityManager.createQuery(query, AnswerEntity.class)
                    .setParameter("userInput", questionEntity).getResultList();

        } catch (NoResultException nre) {

            return null;
        }
    }
}
