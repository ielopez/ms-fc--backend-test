package com.scmspain.services.adapter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scmspain.entities.Tweet;
import com.scmspain.services.dto.TweetDto;

@Service
public class TweetAdapter {

	private static final Function<Tweet, TweetDto> TO_DTO = new Function<Tweet, TweetDto>(){

		@Override
		public TweetDto apply(Tweet source) {
			TweetDto target = new TweetDto();
			target.setId(source.getId());
			target.setPublisher(source.getPublisher());
			target.setTweet(source.getTweet());
			target.setDiscardDate(source.getDiscardDate());
			return target;
		}
		
	};

	public List<TweetDto> toDto(List<Tweet> tweets){
		return tweets.stream().map(TO_DTO).collect(Collectors.toList());
	}
}
