package com.scmspain.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scmspain.configuration.TestConfiguration;
import com.scmspain.exception.BadTweetContentException;
import com.scmspain.exception.EmptyPublisherException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class TweetValidatorTest {

	@Autowired
	private TweetValidator validator;
	
	@Test
	public void testValidTweet(){
		validator.validate("Guybrush", "Soon you'll be wearing my sword like a shish kebab");
	}
	
	@Test(expected=EmptyPublisherException.class)
	public void testBlankPublisherShoudThrowEmptyPublisherException(){
		validator.validate("", "Soon you'll be wearing my sword like a shish kebab");
	}
	
	@Test(expected=EmptyPublisherException.class)
	public void testNullPublisherShoudThrowEmptyPublisherException(){
		validator.validate(null, "Soon you'll be wearing my sword like a shish kebab");
	}
	
	@Test(expected=BadTweetContentException.class)
	public void testTweetLongerThan140CharsShoudThrowBadContentException(){
		validator.validate("Guybrush", "Soon you'll be wearing my sword like a shish kebab. You fight like a cow. "+
				"Soon you'll be wearing my sword like a shish kebab. You fight like a cow. ");
	}
	
	@Test
	public void testTweetWithHttpLinkOver140CharsShouldBeValid(){
		validator.validate("SCM Spain", "Here's a cool website for you to visit. It's our website! http://www.schibsted.com share it now with your friends and family and get to know us!");
	}
	
	@Test
	public void testTweetWithHttpsLinkOver140CharsShouldBeValid(){
		validator.validate("SCM Spain", "Here's a cool website for you to visit. It's our website! https://www.schibsted.com share it now with your friends and family and get to know us!");
	}
	
	@Test
	public void testTweetWithHttpsLinkOver140CharsAtTheEndShouldBeValid(){
		validator.validate("SCM Spain", "Here's a cool website for you to visit. It's our website! Share it now with your friends and family and get to know us! https://www.schibsted.com");
	}
	
	@Test(expected=BadTweetContentException.class)
	public void testTweetWithBadHttpLinkOver140CharsShouldFail(){
		validator.validate("SCM Spain", "Here's a cool website for you to visit. It's our website! htt p://www.schibsted.com share it now with your friends and family and get to know us!");
	}
	
	@Test(expected=BadTweetContentException.class)
	public void testTweetWithBadHttpsLinkOver140CharsShouldFail(){
		validator.validate("SCM Spain", "Here's a cool website for you to visit. It's our website! https:// www.schibsted.com share it now with your friends and family and get to know us!");
	}
	
}
