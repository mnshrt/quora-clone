package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }


    public List<QuestionEntity> getAllQuestions() {

        try {

            String query = "select q from QuestionEntity q";
            return entityManager.createQuery(query, QuestionEntity.class)
                    .getResultList();
        } catch (NoResultException nre) {

            return null;
        }
    }

    public QuestionEntity getQuestionById(String questionId) {
        try {
            String query = "select u from QuestionEntity u where u.uuid = :uuid";
            return entityManager.createQuery(query, QuestionEntity.class)
                    .setParameter("uuid", questionId).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }
    }

    public QuestionEntity updateQuestion(QuestionEntity questionEntity) {
        return entityManager.merge(questionEntity);
    }
}
