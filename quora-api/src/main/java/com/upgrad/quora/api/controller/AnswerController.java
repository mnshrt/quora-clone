package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.AuthorizationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
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
public class AnswerController {
    @Autowired
    QuestionService questionService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    AnswerService answerService;

    @PostMapping(path = "/question/{questionId}answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") String accessToken,
                                                       final AnswerRequest answerRequest) throws
            AuthorizationFailedException {

        final AnswerEntity answerEntity = new AnswerEntity();
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.getUserAuthTokenEntity(accessToken);

        //answerEntity.setUserId(userAuthTokenEntity.getUser());
        answerEntity.setUser(userAuthTokenEntity.getUser());
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setDate(ZonedDateTime.now());


        final AnswerEntity createdAnswerEntity = answerService.createAnswer(answerEntity);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid())
                .status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(AnswerEditRequest answerEditRequest, @RequestHeader("authorization") String accessToken, @PathVariable String answerId)
            throws AuthorizationFailedException, InvalidAnswerException {


        AnswerEntity answerEntity = answerService.checkAnswer(answerId, accessToken);
        answerEntity.setAns(answerEditRequest.getContent());
        AnswerEntity updatedAnswerEntity = answerService.updateAnswer(answerEntity);

        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(updatedAnswerEntity.getUuid()).status("ANSWER UPDATED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

        @DeleteMapping(path = "/answer/delete/{answerId}")
        public ResponseEntity<AnswerDeleteResponse> answerDelete(@RequestHeader("authorization") String accessToken,
                @PathVariable String answerId) throws
        AuthorizationFailedException, InvalidAnswerException {

            String id = answerService.deleteAnswer(answerId,accessToken);

            AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(id)
                    .status("ANSWER DELETED");

            return new ResponseEntity<AnswerDeleteResponse> (answerDeleteResponse, HttpStatus.OK);


        }
}