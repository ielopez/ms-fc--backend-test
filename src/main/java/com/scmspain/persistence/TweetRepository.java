package com.scmspain.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scmspain.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

	List<Tweet> findByDiscardDateIsNotNullOrderByDiscardDateDesc();

	@Query("SELECT T FROM Tweet T WHERE T.pre2015MigrationStatus != 99 and T.discardDate IS NULL ORDER BY ID DESC")
	List<Tweet> findAllTweets();

}
