package com.panjohnny.chatapi.packets;

import java.util.UUID;

@SuppressWarnings("serial")
public class ChatLeftPacket extends Packet{
	private UUID sender;
	private String reason=null;
	/**
	 * This wouldn't kick user it will send him disconnect reason or user will send this if he disconnects to let server know. It is used to prevent from attacks. Server will normally disconnect user
	 */
	public ChatLeftPacket(UUID sender, String reason) {
		this.sender=sender;
		this.reason=reason;
	}
	
	/**
	 * Used by user to end connection {@code localDisconnect} <br>
	 * Don't used by server
	 * @param sender
	 */
	public ChatLeftPacket(UUID sender) {
		this.sender=sender;
	}
	
	public boolean hasReason() {
		return reason!=null;
	}
	
	/**
	 * 
	 * @return if reason is specified return reason if not returns {@code unspecified}
	 */
	public String getReason() {
		return hasReason()?reason:"unspecified";
	}
	/**
	 * @return UUID of sender
	 */
	public UUID getSender() {
		return sender;
	}
}