package com.scmspain.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scmspain.configuration.TestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class TweetValidatorUrlStrippingTest {

	@Autowired
	private TweetValidator tweetValidator;
	
	@Test
	public void testNormalTweet(){
		String tweet = "This is a normal Tweet";
		Assert.assertEquals("The tween should be unaltered", tweet, tweetValidator.stripUrls(tweet));
	}
	
	@Test
	public void testUrlInTheBeginningAndTheEndOfTheTweet(){
		String tweet = "http://www.schibsted.com Is our website. Google it! https://www.google.com";
		Assert.assertEquals("Both URLs should have been stripped away!", "Is our website. Google it! ", tweetValidator.stripUrls(tweet));
	}
	
	@Test
	public void testUrlInTheMiddleOfTheTweet(){
		String tweet = "Visit our website http://www.schibsted.com for more info";
		Assert.assertEquals("The URL in the middle should be removed!", "Visit our website for more info", tweetValidator.stripUrls(tweet));
	}
	
	@Test
	public void testManyUrlsWithinTheTweet(){
		String tweet = "Site http://bit.ly/asd another https://something.com and yet another http://www.a.com";
		Assert.assertEquals("The URL in the middle should be removed!", "Site another and yet another ", tweetValidator.stripUrls(tweet));
	}
	
	@Test
	public void testTweetLengthShouldBeFourCharactersLong(){
		String tweet = "Hey http://foogle.co";
		Assert.assertEquals("The URL in the middle should be removed!", 4, tweetValidator.stripUrls(tweet).length());
	}
	
}
