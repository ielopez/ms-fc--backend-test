package com.scmspain.services;

import org.springframework.stereotype.Service;

import com.scmspain.entities.Tweet;
import com.scmspain.exception.BadTweetContentException;
import com.scmspain.exception.EmptyPublisherException;

@Service
public class TweetValidator {
	
	private static final String LINK_REGEX = "(http://|https://)+[^\\s]+( |$)";
	private static final int MAX_TWEET_LENGTH = 140;
	
	public void validate(final String publisher, final String tweet){
		if(StringUtil.isNullOrEmpty(publisher)){
			throw new EmptyPublisherException("Publisher must be included!");
		}
		
		if(StringUtil.isNullOrEmpty(tweet)){
			throw new BadTweetContentException("The tweet must not be null!");
		}
		
		if(!isWithinStorageLimit(tweet)){
			throw new BadTweetContentException("The tweet must have up to 140 characters!");
		}
		
	}
	
	public String stripUrls(String tweet){
		return tweet.replaceAll(LINK_REGEX, "");
	}
	
	private boolean isWithinStorageLimit(final String tweet){
		return tweet.length() <= Tweet.MAX_TWEET_STORAGE_LENGTH && 
				stripUrls(tweet).length() <= MAX_TWEET_LENGTH;
	}
}
