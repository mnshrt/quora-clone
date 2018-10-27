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

            return new ResponseEntity<QuestionResponse> (questionResponse, HttpStatus.OK);
    }

/*    @GetMapping(path="/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions(@RequestHeader("authorization") String accessToken) throws AuthorizationFailedException {
        List<QuestionEntity> questionEntityList = new ArrayList<>();
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.getUserAuthTokenEntity(accessToken);
        StringBuilder sb = new StringBuilder("");
        if (userAuthTokenEntity != null) {
            questionEntityList = questionService.getAllQuestions();
            //Adding new content for testing
            List<JSONObject> entities = new ArrayList<JSONObject>();
            StringBuilder sb = new StringBuilder("");
            //ResponseEntity<QuestionEntity> responseEntity = new ResponseEntity<>()
            for (QuestionEntity n : questionEntityList) {
                QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
                questionDetailsResponse.setId(n.getUuid());
                questionDetailsResponse.setContent(n.getContent());


                sb.append(questionDetailsResponse.toString());
            }
        }

        return new ResponseEntity(, HttpStatus.OK);

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
}

