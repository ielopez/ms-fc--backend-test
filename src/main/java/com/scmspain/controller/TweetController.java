package com.scmspain.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.scmspain.controller.command.DiscardTweetCommand;
import com.scmspain.controller.command.PublishTweetCommand;
import com.scmspain.services.TweetService;
import com.scmspain.services.dto.TweetDto;

@RestController
public class TweetController {
    private TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/tweet")
    public List<TweetDto> listAllTweets() {
        return this.tweetService.listAllTweets();
    }

    @PostMapping("/tweet")
    @ResponseStatus(CREATED)
    public void publishTweet(@RequestBody PublishTweetCommand publishTweetCommand) {
        this.tweetService.publishTweet(publishTweetCommand.getPublisher(), publishTweetCommand.getTweet());
    }
    
    @GetMapping("/discard")
    public List<TweetDto> listAllDiscardedTweets(){
    	return this.tweetService.listAllDiscardedTweets();
    }
    
    @PostMapping(name="/discard", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(OK)
    public void discardTweet(@RequestBody DiscardTweetCommand discardTweetCommand){
    	this.tweetService.discardTweet(discardTweetCommand.getTweet());
    }
    

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Object invalidArgumentException(IllegalArgumentException ex) {
        return new Object() {
            public String message = ex.getMessage();
            public String exceptionClass = ex.getClass().getSimpleName();
        };
    }
}
