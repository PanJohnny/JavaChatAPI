package com.panjohnny.chatapi.packets;

import java.util.UUID;

@SuppressWarnings("serial")
public class ChatMessagePacket extends Packet{
	private UUID sender;
	private String content;
	private String name;
	/**
	 * Used by client to send messages
	 * @param sender
	 * @param content
	 */
	public ChatMessagePacket(UUID sender, String content) {
		this.sender=sender;
		this.content=content;
	}
	/**
	 * Used by server to resent messages
	 * @param name
	 * @param content
	 */
	public ChatMessagePacket(String name, String content) { 
		this.name=name;
	}
	/**
	 * 
	 * @return content of message
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return uuid of user
	 */
	public UUID getSender() {
		return sender;
	}
	public String getName() {
		return name;
	}
}
