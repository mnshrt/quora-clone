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
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    AuthorizationService authorizationService;



    @PostMapping(path = "/question/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") String accessToken,
                                                           final QuestionRequest questionRequest) throws
            AuthorizationFailedException {

        final QuestionEntity questionEntity = new QuestionEntity();
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.getUserAuthTokenEntity(accessToken);

        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setUserId(userAuthTokenEntity.getUser());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());

        final QuestionEntity createdQuestionEntity = questionService.createQuestion(questionEntity);
            QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid())
                    .status("QUESTION CREATED");

            return new ResponseEntity<> (questionResponse, HttpStatus.OK);
    }




    @GetMapping(path="/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") String accessToken) throws AuthorizationFailedException {

         List<QuestionEntity> questionEntityList = questionService.getAllQuestions(accessToken);

        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<QuestionDetailsResponse>();
         if(!questionEntityList.isEmpty()) {

             for (QuestionEntity n : questionEntityList) {
                 QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
                 questionDetailsResponse.setId(n.getUuid());
                 questionDetailsResponse.setContent(n.getContent());

                 questionDetailsResponseList.add(questionDetailsResponse);
             }
           //  return new ResponseEntity<>(questionDetailsResponseList, HttpStatus.OK);
         }

        return new ResponseEntity<>(questionDetailsResponseList, HttpStatus.OK);

    }

    @GetMapping(path="/question/edit/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@RequestHeader("authorization") String accessToken,@PathVariable String questionId,QuestionEditRequest questionEditRequest)
            throws AuthorizationFailedException, InvalidQuestionException {


            QuestionEntity questionEntity = questionService.checkQuestion(accessToken, questionId);
            questionEntity.setContent(questionEditRequest.getContent());
            QuestionEntity updatedQuestionEntity = questionService.updateQuestion(questionEntity);

            QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(updatedQuestionEntity.getUuid()).status("QUESTION EDITED");

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

    @GetMapping(path= "/all/{userId}")
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") String accessToken,
                                                                         @PathVariable String userId) throws AuthorizationFailedException, UserNotFoundException {

        List<QuestionEntity> questionEntityList = questionService.getAllQuestionsByUser(accessToken, userId);

        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<QuestionDetailsResponse>();
        for (QuestionEntity n : questionEntityList) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            questionDetailsResponse.setId(n.getUuid());
            questionDetailsResponse.setContent(n.getContent());

            questionDetailsResponseList.add(questionDetailsResponse);
        }
        return new ResponseEntity<>(questionDetailsResponseList, HttpStatus.OK);


    }
}

