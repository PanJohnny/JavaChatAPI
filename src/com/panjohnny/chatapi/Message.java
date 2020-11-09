package com.panjohnny.chatapi;

import java.util.UUID;

public class Message {
	private MessageSender sender;
	private String message;
	private UUID uuid;
	private boolean systemMessage=false;
	/**
	 * Used to send messages to other clients
	 * @param sender
	 * @param message
	 */
	public Message(MessageSender sender, String message) {
		this.message=message;
		this.sender=sender;
		uuid=UUID.randomUUID();
	}
	
	/**
	 * Used to send server info messages
	 * @param message
	 */
	public Message(String message) {
		this.message=message;
		systemMessage=true;
	}
	public MessageSender getSender() {
		return sender;
	}
	public String asString() {
		return message;
	}
	public UUID getUuid() {
		return uuid;
	}

	public boolean isSystemMessage() {
		return systemMessage;
	}
}
