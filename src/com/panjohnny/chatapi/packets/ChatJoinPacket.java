package com.panjohnny.chatapi.packets;

import java.util.UUID;

@SuppressWarnings("serial")
public class ChatJoinPacket extends Packet{
	private UUID sender;
	private boolean request = false;
	private String name="none";
	/**
	 * <h1> PHASE 2 </h1>
	 * Sent by user to register his name
	 * @param sender
	 * @param name
	 */
	public ChatJoinPacket(UUID sender, String name) {
		this.sender=sender;
		this.name=name;
	}
	/**
	 * <h1> PHASE 1 </h1>
	 * Send by user to request his uuid
	 * @param request
	 */
	public ChatJoinPacket(boolean request) {
		this.request=request;
	}
	/**
	 * 
	 * @return uuid of user
	 */
	public UUID getSender() {
		return sender;
	}
	/**
	 * 
	 * @return true if this packet is request
	 */
	public boolean isRequest() {
		return request;
	}
	
	/**
	 * 
	 * @return name if this packet is not request or name is specified else returns {@code none}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return true if packet has name
	 */
	public boolean hasName() {
		return name!=null;
	}
}
