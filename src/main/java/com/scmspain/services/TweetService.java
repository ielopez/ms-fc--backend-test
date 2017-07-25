package com.scmspain.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;

import com.scmspain.entities.Tweet;
import com.scmspain.exception.TweetAlreadyDiscardedException;
import com.scmspain.exception.TweetNotFoundException;
import com.scmspain.persistence.TweetRepository;
import com.scmspain.services.adapter.TweetAdapter;
import com.scmspain.services.dto.TweetDto;

@Service
@Transactional
public class TweetService {
    
	private static final Long HIDDEN = 99L;

	@Autowired
    private MetricWriter metricWriter;

    @Autowired
    private TweetRepository tweetRepository;
    
    @Autowired
    private TweetAdapter tweetAdapter;
    
    @Autowired
    private TweetValidator tweetValidator;

	/**
      Push tweet to repository
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
      Result - recovered Tweet
    */
    public void publishTweet(String publisher, String text) {
    	tweetValidator.validate(publisher, text);
        Tweet tweet = new Tweet();
        tweet.setTweet(text);
        tweet.setPublisher(publisher);
        this.tweetRepository.save(tweet);
        this.metricWriter.increment(new Delta<Number>("published-tweets", 1));
    }
    
    /**
      Recover tweet from repository
      Parameter - id - id of the Tweet to retrieve
      Result - retrieved Tweet
    */
    public TweetDto getTweet(Long id) {
      Tweet tweet = this.findTweetById(id);
      this.metricWriter.increment(new Delta<Number>("times-retrieving-single-tweet", 1));
      return tweetAdapter.toDto(tweet);
      
    }
    
    private Tweet findTweetById(Long id){
    	Tweet tweet = this.tweetRepository.findOne(id);
        if(shouldHideTweet(tweet)){
      	  throw new TweetNotFoundException(String.format("Tweet %s not found", id));
        }
        
        return tweet;
    }
    
    /**
     * Given a tweet, this method return whether is not meant to be shown to the user.
     * @param tweet The tweet to evaluate
     * @return True if {@code tweet} has a pre-2015 migration status 99 or has been discarded. 
     */
    private boolean shouldHideTweet(Tweet tweet){
    	return tweet == null ||tweet.getPre2015MigrationStatus() == HIDDEN || tweet.getDiscardDate() != null;
    }

    /**
      Recover tweet from repository
      Result - retrieved Tweet
    */
    public List<TweetDto> listAllTweets() {
        List<Tweet> result = tweetRepository.findAllTweets();    
        this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
        return tweetAdapter.toDto(result);
    }

    /**
     * Recover all discarded tweets fom the repository
     * 
     * @return a list of all tweets marked as discarded sorted by discard date in descending order.
     */
    public List<TweetDto> listAllDiscardedTweets() {
		List<Tweet> discarded = tweetRepository.findByDiscardDateIsNotNullOrderByDiscardDateDesc();
		this.metricWriter.increment(new Delta<Number>("times-queried-discarded-tweets", 1));
        
		return tweetAdapter.toDto(discarded);
	}

    /**
     * Discards a tweet by ID
     * @param tweetId The unique ID of the tweet.
     */
	public void discardTweet(Long tweetId) {
		Tweet tweet = this.findTweetById(tweetId);
		if(tweet.getDiscardDate() !=null){
			throw new TweetAlreadyDiscardedException(String.format("Tweet %s is already discarded!", tweetId));
		}
		
		tweet.setDiscardDate(LocalDateTime.now());
		tweetRepository.save(tweet);
		this.metricWriter.increment(new Delta<Number>("times-discarded-tweets", 1));
		
	}
	
}
