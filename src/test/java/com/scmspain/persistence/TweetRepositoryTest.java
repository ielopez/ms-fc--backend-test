package com.scmspain.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scmspain.configuration.TestConfiguration;
import com.scmspain.entities.Tweet;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class TweetRepositoryTest {
	
	@Autowired
	private TweetRepository tweetRepository;
	
	@Test	
	public void testSaveSingleTweetShouldPersistTheTweet(){
		Tweet tweet = getTweet();
		tweetRepository.save(tweet);
		Assert.assertTrue("This tweet should have an ID already", tweet.getId()!=null);
	}
	
	
	@Test
	public void testFindByIdShouldReturnMatchingIdTweet(){
		Tweet tweet = getTweet();
		tweetRepository.save(tweet);
		Tweet saved = tweetRepository.findOne(tweet.getId());
		Assert.assertEquals("The IDs should be equal", saved.getId(), tweet.getId());
	}
	
	@Test
	public void testFindAllPostMigrationTweetsShouldNotRetrieveTweetWithPostMigrationStatus99(){
		Tweet normalTweet = getTweet();
		tweetRepository.save(normalTweet);
		List<Tweet> everyTweet = tweetRepository.findAll();
		Tweet badTweet = getTweet();
		badTweet.setPre2015MigrationStatus(99L);
		tweetRepository.save(badTweet);
		List<Tweet> postMigrationTweets = tweetRepository.findAllTweets();
		Assert.assertEquals("This should have returned one less tweet!", everyTweet.size(), postMigrationTweets.size());
		
	}
	
	@Test
	public void testDiscardTweetShouldRetrieveDiscardedTweetsInDescendingOrder(){
		Tweet firstTweet = getTweet();
		firstTweet.setDiscardDate(LocalDateTime.now());
		tweetRepository.save(firstTweet);
		
		Tweet secondTweet = getTweet();
		secondTweet.setDiscardDate(LocalDateTime.now());
		tweetRepository.save(secondTweet);
		
		Tweet thirdTweet = getTweet();
		tweetRepository.save(thirdTweet);
		
		
		List<Tweet> discardedTweets = tweetRepository.findByDiscardDateIsNotNullOrderByDiscardDateDesc();
		Assert.assertEquals("The first discarded tweet should be the most recently discarded one", secondTweet.getId(), discardedTweets.get(0).getId());
	}
	
	private Tweet getTweet(){
		Tweet tweet = new Tweet();
		tweet.setPublisher("Guybrush");
		tweet.setTweet("Look! A three-headed monkey!");
		
		return tweet;
	}
	

}
