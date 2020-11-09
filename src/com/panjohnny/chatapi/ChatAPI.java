package com.panjohnny.chatapi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.panjohnny.chatapi.client.Client;

public class ChatAPI {
	//blocks instancing
	private ChatAPI() {}
	private static final ChatAPI instance = new ChatAPI();
	private static List<Chat> chats=new ArrayList<Chat>();
	private static List<Client> clients=new ArrayList<Client>();
	/**
	 * Creates new chat
	 * @param name
	 * @param ip
	 * @param port
	 * @return id of chat
	 */
	public static int createNewChat(String name, String ip, int port) {
		chats.add(new Chat(instance, name, ip, port));
		return chats.size()-1;
	}
	/**
	 * Gets the chat referred to {@link ChatAPI#createNewChat(String, String, int)}
	 * @param chat id
	 * @return chat
	 */
	public static Chat getChat(int chat) {
		return chats.get(chat);
	}
	/**
	 * Creates new client and connects it to chat
	 * @param chat
	 * @param name
	 * @return id of client
	 */
	public static int createClient(String ip, int port, String name) {
		clients.add(new Client(instance, ip, port, name));
		return clients.size()-1;
	}
	/**
	 * Gets client referred to {@link ChatAPI#createClient(int, String)}
	 * @param client
	 * @return
	 */
	public static Client getClient(int client) {
		return clients.get(client);
	}
	/**
	 * Runs code for each of the chats
	 * @param consumer
	 */
	public static void forEachChats(Consumer<? super Chat> consumer) {
		chats.forEach(consumer);
	}
	
	/**
	 * Runs code for each of the clients
	 * @param consumer
	 */
	public static void forEachClients(Consumer<? super Client> consumer) {
		clients.forEach(consumer);
	}
}
