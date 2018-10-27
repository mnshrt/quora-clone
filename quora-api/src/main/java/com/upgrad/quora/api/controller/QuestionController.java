package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.AuthorizationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    AuthorizationService authorizationService;

    @PostMapping(path = "/question/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") String accessToken,
                                                           final QuestionRequest questionRequest) throws
            AuthorizationFailedException {

        final QuestionEntity questionEntity = new QuestionEntity();
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.getUserAuthTokenEntity(accessToken);

        questionEntity.setUserId(userAuthTokenEntity.getUser());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());




         final QuestionEntity createdQuestionEntity = questionService.createQuestion(questionEntity);
            QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid())
                    .status("QUESTION CREATED");

            return new ResponseEntity<> (questionResponse, HttpStatus.OK);
    }

/*    @GetMapping(path="/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<JSONObject>> getAllQuestions(@RequestHeader("authorization") String accessToken) throws AuthorizationFailedException {

        List<QuestionEntity> questionEntityList = new ArrayList<>();

        UserAuthTokenEntity userAuthTokenEntity = authorizationService.getUserAuthTokenEntity(accessToken);

        if (userAuthTokenEntity != null) {
           questionEntityList = questionService.getAllQuestions();
        }

        return new ResponseEntity(questionEntityList, HttpStatus.OK);

    }*/

    @GetMapping(path="/question/edit/{questionId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(QuestionEditRequest questionEditRequest,@RequestHeader("authorization") String accessToken,@PathVariable String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {


            QuestionEntity questionEntity = questionService.checkQuestion(questionId, accessToken);
            questionEntity.setContent(questionEditRequest.getContent());
            QuestionEntity updatedQuestionEntity = questionService.updateQuestion(questionEntity);

            QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(updatedQuestionEntity.getUuid()).status("QUESTION UPDATED");

            return new ResponseEntity<>(questionEditResponse, HttpStatus.OK);


    }

    @DeleteMapping(path = "/question/delete/{questionId}")
    public ResponseEntity<QuestionDeleteResponse> questionDelete(@RequestHeader("authorization") String accessToken,
                                                             @PathVariable String questionId) throws

            AuthorizationFailedException, InvalidQuestionException {

            String id = questionService.deleteQuestion(questionId,accessToken);

            QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(id)
                    .status("QUESTION DELETED");

            return new ResponseEntity<> (questionDeleteResponse, HttpStatus.OK);
    }

}

