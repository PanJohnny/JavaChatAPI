package com.panjohnny.chatapi;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.panjohnny.chatapi.packets.ChatMessagePacket;
import com.panjohnny.chatapi.server.ChatServer;
import com.panjohnny.chatapi.server.ServerClient;

/**
 * Chat object that provides easier access to messages and handling stuff
 *
 */
public class Chat {
	private String name;
	@SuppressWarnings("unused")
	private ChatAPI api;
	private List<Message> history = new LinkedList<Message>();
	private ChatServer server;
	public Chat(ChatAPI api, String name, String ip, int port) {
		this.name=name;
		this.api=api;
		this.server = new ChatServer(this, ip, port);
	}
	/**
	 * Starts chat server
	 */
	public void start() {
		server.start();
	}
	/**
	 * Stops chat server
	 */
	public void stop() {
		server.stop();
	}
	/**
	 * @return chat history
	 */
	public List<Message> getChatHistory() {
		return history;
	}
	/**
	 * 
	 * @param limit how many messages will be returned
	 * @return last messages of the size of limit
	 */
	public List<Message> getLast(int limit) {
		try {
			return history.subList(history.size()-1-limit, history.size()-1);
		} catch(Exception e) {
			System.err.println("Couldn't get "+limit+" messages returning whole history");
			return history;
		}
	}
	/**
	 * Sends message to chat
	 * @param message
	 * @param notTo can be null
	 */
	public void send(Message message,@Nullable ServerClient notTo) {
		history.add(message);
		if(message.isSystemMessage()) {
			server.getConnectedClients().forEach(c -> {
				if(c.SAFE_CONNECTION) {
					c.sendPacket(new ChatMessagePacket(message.getSender().getName(), message.asString()));
				}
			});
		} else {
			server.getConnectedClients().forEach(c -> {
				if(c.SAFE_CONNECTION) {
					c.sendPacket(new ChatMessagePacket("", message.asString()));
				}
			});
		}
	}
	/**
	 * Removes message from chat
	 * @param message
	 * @return true if removal was success
	 */
	public boolean remove(Message message) {
		if(!history.contains(message)) return false;
		history.remove(message);
		return true;
	}
	/**
	 * 
	 * @return name of the chat
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return server
	 */
	public ChatServer getServer() {
		return server;
	}
}
