package com.scmspain.services;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scmspain.configuration.TestConfiguration;
import com.scmspain.exception.BadTweetContentException;
import com.scmspain.exception.TweetNotFoundException;
import com.scmspain.services.dto.TweetDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class TweetServiceTest {
	
	private Predicate<TweetDto> hasDiscardedTweets = new Predicate<TweetDto>(){

		@Override
		public boolean test(TweetDto t) {
			return t.getDiscardDate() != null;
		}
		
	};

	@Autowired
	private TweetService tweetService;

    @Test
    public void shouldInsertANewTweet() throws Exception {
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");
    }

    @Test(expected = BadTweetContentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }
    
    @Test
    public void testLongUrlShouldNotAffect140CharLimit() throws Exception {
        tweetService.publishTweet("Pirate", "This is a really long URL https://www.reallyreallyreallyreallyreallyreallyreallyreallyreallylongurl.com whoa.");
    }
    
    @Test(expected = BadTweetContentException.class)
    public void testUrlOver500CharactersShouldThrowException() throws Exception {
        tweetService.publishTweet("Pirate", "This is a really long URL https://www.thisurlhasoverfivehundredcharactersandicantthinkofanythingtoputheretomakeitevenlongerthanitalreadyisthisurlhasoverfivehundredcharactersandicantthinkofanythingtoputheretomakeitevenlongerthanitalreadyisthisurlhasoverfivehundredcharactersandicantthinkofanythingtoputheretomakeitevenlongerthanitalreadyisthisurlhasoverfivehundredcharactersandicantthinkofanythingtoputheretomakeitevenlongerthanitalreadyisthisurlhasoverfivehundredcharactersandicantthinkofanythingtoputheretomakeitevenlongerthanitalreadyis.com whoa.");
    }
    
    @Test
    public void testRetrievingAllTweetsShouldNotContainDiscardedTweets(){
    	List<TweetDto> tweets = tweetService.listAllTweets();
    	boolean containsDiscarded = tweets.stream().anyMatch(hasDiscardedTweets);
    	
    	Assert.assertTrue("The list should not contain discardedTweets", !containsDiscarded);
    	
    }
    
    @Test
    public void testRetrievingDiscardedTweetsShouldNotContainLiveTweets(){
    	List<TweetDto> tweets = tweetService.listAllDiscardedTweets();
    	boolean hasLiveTweets = tweets.stream().anyMatch(new Predicate<TweetDto>() {

			@Override
			public boolean test(TweetDto t) {
				return t.getDiscardDate() == null;
			}
    		
		});
    	
    	Assert.assertTrue("The list should not contain live tweets", !hasLiveTweets);
    	
    }
    
    @Test(expected=TweetNotFoundException.class)
    public void testRetrieveNonExistingTweetShouldThrowException(){
    	tweetService.getTweet(0l);
    }
    
    @Test
    public void testRetrieveValidTweet(){
    	TweetDto tweet = getSomeTweet();
    	TweetDto retrieved = tweetService.getTweet(tweet.getId());
    	
    	Assert.assertEquals("The retrieved tweet should match the ID", tweet.getId(), retrieved.getId());
    	
    }
    
    @Test(expected=TweetNotFoundException.class)
    public void testRetrieveDiscardedTweetShouldThrowException(){
    	TweetDto tweet = getSomeTweet();
    	tweetService.discardTweet(tweet.getId());
    	tweet = tweetService.getTweet(tweet.getId());
    }
    
    @Test
    public void testDiscardTweetShouldRetrieveTweetAsDiscarded(){
    	TweetDto tweet = getSomeTweet();
    	tweetService.discardTweet(tweet.getId());
    	List<TweetDto> discardedTweets = tweetService.listAllDiscardedTweets();
    	boolean discarded = discardedTweets.stream().anyMatch(new Predicate<TweetDto>(){

			@Override
			public boolean test(TweetDto t) {
				return tweet.getId().equals(t.getId());
			}
    		
    	});
    	
    	Assert.assertTrue("The list should contain the tweet we just discarded!", discarded);
    }
    
    @Test(expected = TweetNotFoundException.class)
    public void testDiscardTweetTwiceShouldThrowException(){
    	TweetDto tweet = getSomeTweet();
    	tweetService.discardTweet(tweet.getId());
    	tweetService.discardTweet(tweet.getId());
    }
    
    /**
     * Saves a tweet and retrieves the first one from the list of available tweets. 
     * @return A tweet.
     */
    private TweetDto getSomeTweet(){
    	tweetService.publishTweet("Yoda", "Do or do not, there is no try");
    	return tweetService.listAllTweets().get(0);
    }
}
