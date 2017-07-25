package com.scmspain.exception;

public class BadTweetContentException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1247293726137088001L;
	
	public BadTweetContentException(String message){
		super(message);
	}

}
