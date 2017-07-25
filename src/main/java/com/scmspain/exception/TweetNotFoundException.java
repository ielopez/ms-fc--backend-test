package com.scmspain.exception;

public class TweetNotFoundException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 582618732654601626L;

	public TweetNotFoundException(String message){
		super(message);
	}
}
