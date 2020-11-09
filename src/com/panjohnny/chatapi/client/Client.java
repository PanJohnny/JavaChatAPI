package com.panjohnny.chatapi.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.panjohnny.chatapi.ChatAPI;
import com.panjohnny.chatapi.Message;
import com.panjohnny.chatapi.MessageSender;
import com.panjohnny.chatapi.packets.ChatConfigPacket;
import com.panjohnny.chatapi.packets.ChatJoinPacket;
import com.panjohnny.chatapi.packets.ChatLeftPacket;
import com.panjohnny.chatapi.packets.ChatMessagePacket;
import com.panjohnny.chatapi.packets.Packet;

public class Client implements Runnable{
	private String ip;
	private int port;
	private String name;
	private ChatAPI api;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean configured;
	private UUID uuid = null;
	private Thread thread;
	private Thread thread2;
	private boolean connected;
	private List<Message> history = new LinkedList<Message>();
	/**
	 * Use {@link ChatAPI#createClient(int, String)}
	 * @param api
	 * @param ip
	 * @param port
	 * @param name
	 */
	public Client(ChatAPI api, String ip, int port, String name) {
		this.api=api;
		this.ip=ip;
		this.port=port;
		this.name=name;
	}
	/**
	 * Use to connect client to specified ip and port in constructor also it will configure connection
	 */
	public void connect() {
		try {
			socket=new Socket(ip, port);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			thread=new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						oos.writeObject(new ChatJoinPacket(true));
						Packet p = (Packet) ois.readObject();
						if(p instanceof ChatConfigPacket) {
							ChatConfigPacket cp = (ChatConfigPacket) p;
							uuid=cp.getUUID();
							oos.writeObject(new ChatJoinPacket(uuid, name));
							configured=true;
						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
					try {
						thread.join(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}, "Client connecting thread&&"+name);
			thread2=new Thread(this, "Message handling thread&&"+name);
			thread.start();
			thread2.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Use if {@link Client#isConfigured()} <br>
	 * Advanced api normally use chat history and get message
	 * @return readable packet that server send
	 */
	public Packet readPacket() {
		if(!configured) {
			System.err.println("Cannot call this function when server is not configured!! Client#readPacket();");
			return new Packet();
		}
		try {
			return (Packet)ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			configured=false;
			return new ChatLeftPacket(uuid, "server closed");
		}
	}
	
	public void sendMessage(String content) {
		try {
			oos.writeObject(new ChatMessagePacket(uuid, content));
		} catch (IOException e) {
			connected=false;
			System.out.println("client&&"+name+":// disconecting from server reason: *lost connection*");
		}
	}
	/**
	 * 
	 * @return true if client is connected and configured to server
	 */
	public boolean isConfigured() {
		return configured;
	}
	
	/**
	 * Do not use 
	 * @see {@link Client#connect()}
	 */
	@Override
	public void run() {
		//waits until configured
		while(!configured);
		connected=true;
		while(connected) {
			Packet p = readPacket();
			if(p instanceof ChatMessagePacket) {
				ChatMessagePacket cmp = (ChatMessagePacket) p;
				history.add(new Message(new MessageSender(cmp.getSender(), cmp.getName()), cmp.getContent()));
			}
			if(p instanceof ChatLeftPacket) {
				connected=false;
				ChatLeftPacket clp = (ChatLeftPacket) p;
				String reason = "Disconnected";
				if(clp.hasReason()) reason=clp.getReason();
				history.add(new Message(reason));
			}
		}
		try {
			thread2.join(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	 * 
	 * @return all {@link Message} sent in history 
	 */
	public List<Message> getMessageHistory() {
		return history;
	}
	/**
	 * @return true if client is connected to server
	 */
	public boolean isConnected() {
		return connected;
	}
	
}
