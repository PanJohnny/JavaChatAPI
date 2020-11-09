package com.panjohnny.chatapi;

import java.util.UUID;

public class MessageSender {
	private UUID uuid;
	private String name;
	public MessageSender(UUID uuid, String name) {
		this.uuid=uuid;
		this.name=name;
	}
	public UUID getUuid() {
		return uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
