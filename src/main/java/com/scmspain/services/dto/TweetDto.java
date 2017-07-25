package com.scmspain.services.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TweetDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -529560821217898961L;

	private Long id;

    private String publisher;

    private String tweet;

    private Long pre2015MigrationStatus;
    
    @JsonIgnore
    private LocalDateTime discardDate;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the tweet
	 */
	public String getTweet() {
		return tweet;
	}

	/**
	 * @param tweet the tweet to set
	 */
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	/**
	 * @return the pre2015MigrationStatus
	 */
	public Long getPre2015MigrationStatus() {
		return pre2015MigrationStatus;
	}

	/**
	 * @param pre2015MigrationStatus the pre2015MigrationStatus to set
	 */
	public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
		this.pre2015MigrationStatus = pre2015MigrationStatus;
	}

	/**
	 * @return the discardDate
	 */
	public LocalDateTime getDiscardDate() {
		return discardDate;
	}

	/**
	 * @param discardDate the discardDate to set
	 */
	public void setDiscardDate(LocalDateTime discardDate) {
		this.discardDate = discardDate;
	}
    
}
