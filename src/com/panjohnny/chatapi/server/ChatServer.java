package com.panjohnny.chatapi.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.panjohnny.chatapi.Chat;

public class ChatServer implements Runnable{
	private Chat chat;
	private Thread thread;
	private boolean running=false;
	private String ip;
	private int port;
	private ServerSocket socket;
	private List<ServerClient> connectedClients = new ArrayList<ServerClient>();
	public ChatServer(Chat chat, String ip, int port) {
		this.chat=chat;
		this.ip=ip;
		this.port=port;
	}
	/**
	 * @deprecated use  {@link Chat#start()}
	 */
	public void start() {
		running=true;
		thread=new Thread(this, "chatserver-"+chat.getName());
		thread.start();
	}
	/**
	 * @deprecated use {@link Chat#stop()}
	 */
	public void stop() {
		try {
			running=false;
			thread.join(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
		try {
			socket=new ServerSocket();
			socket.bind(new InetSocketAddress(ip, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(running) {
			try {
				Socket c = socket.accept();
				ServerClient client = new ServerClient(c, chat);
				connectedClients.add(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void kickClient(ServerClient client, CharSequence reason) {
		client.disconnect(reason);
	}
	/**
	 * Please dont use this method! <br>
	 * Warning only used by API
	 * @param client
	 * Removes client from connectedClients
	 * @apiNote This will mess things up if you were bitch lol
	 */
	public void removeClient(ServerClient client) {
		connectedClients.remove(client);
	}
	
	public List<ServerClient> getConnectedClients() {
		return connectedClients;
	}
	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
}
