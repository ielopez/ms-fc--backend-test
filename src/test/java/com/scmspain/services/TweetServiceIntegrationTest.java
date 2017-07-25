package com.scmspain.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scmspain.configuration.TestConfiguration;
import com.scmspain.services.dto.TweetDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class TweetServiceIntegrationTest {
	
	@Autowired
	private TweetService tweetService;

	@Test
	public void testRetrieveAllTweetsShouldNotIncludeDiscardedTweets(){
		tweetService.publishTweet("LeChuck", "You fight like a dairy farmer");
		tweetService.publishTweet("Guybrush", "How appropriate. You fight like a cow.");
		tweetService.publishTweet("Elaine", "Oh please");
		List<TweetDto> tweets = tweetService.listAllTweets();
		TweetDto discardable = tweets.get(0);
		tweetService.discardTweet(discardable.getId());
		tweets = tweetService.listAllTweets();
		
		Assert.assertTrue("The list of tweets shouldn´t contain the discarded tweet", !tweets.contains(discardable));
		
	}
}
