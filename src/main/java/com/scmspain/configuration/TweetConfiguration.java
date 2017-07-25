package com.scmspain.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.scmspain.controller.TweetController;
import com.scmspain.services.TweetService;
import com.scmspain.services.TweetValidator;
import com.scmspain.services.adapter.TweetAdapter;

@Configuration
public class TweetConfiguration {
	
	@Bean 
	public TweetValidator getTweetValidator(){
		return new TweetValidator();
	}
	
    @Bean
    public TweetService getTweetService() {
        return new TweetService();
    }
    
    @Bean
    public TweetAdapter getTweetAdapter(){
    	return new TweetAdapter();
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService) {
        return new TweetController(tweetService);
    }
}
