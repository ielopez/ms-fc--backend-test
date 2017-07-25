package com.scmspain.exception;

public class TweetAlreadyDiscardedException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 582618732654601626L;

	public TweetAlreadyDiscardedException(String message){
		super(message);
	}
}
