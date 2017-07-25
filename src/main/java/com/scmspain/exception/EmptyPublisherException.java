package com.scmspain.exception;

public class EmptyPublisherException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6344412343423329429L;
	
	public EmptyPublisherException(String message){
		super(message);
	}

}
