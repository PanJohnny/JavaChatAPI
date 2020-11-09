package com.panjohnny.chatapi.packets;

import java.util.UUID;

@SuppressWarnings("serial")
public class ChatConfigPacket extends Packet{
	private UUID uuid;
	/**
	 * Used by server to send user UUID
	 * @param uuid
	 */
	public ChatConfigPacket(UUID uuid) {
		this.uuid=uuid;
	}
	/**
	 * 
	 * @return users uuid
	 */
	public UUID getUUID() {
		return uuid;
	}
}
