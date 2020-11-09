package com.panjohnny.chatapi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import com.panjohnny.chatapi.Chat;
import com.panjohnny.chatapi.Message;
import com.panjohnny.chatapi.MessageSender;
import com.panjohnny.chatapi.packets.ChatConfigPacket;
import com.panjohnny.chatapi.packets.ChatJoinPacket;
import com.panjohnny.chatapi.packets.ChatLeftPacket;
import com.panjohnny.chatapi.packets.ChatMessagePacket;
import com.panjohnny.chatapi.packets.Packet;

public class ServerClient implements Runnable{
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Thread thread;
	private UUID uuid;
	private Chat chat;
	private String name;
	private MessageSender thisSender;
	private boolean obtainedChatRequest=false;
	private boolean connected=true;
	public boolean SAFE_CONNECTION=false;
	public ServerClient(Socket socket, Chat chat) {
		this.socket=socket;
		this.uuid=UUID.randomUUID();
		try {
			this.chat=chat;
			this.in=new ObjectInputStream(socket.getInputStream());
			this.out=new ObjectOutputStream(socket.getOutputStream());
			thread=new Thread(this, "ServerClientTh");
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public Packet readPacket() {
		Object o = null;
		try {
			o = in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(o!=null && o instanceof Packet) {
			return (Packet) o;
		} else {
			return new ChatLeftPacket(uuid);
		}
	}
	
	public void sendPacket(Packet p) {
		try {
			out.writeObject(p);
		} catch (IOException e) {
			connected=false;
		}
	}
	@Override
	public void run() {
		while(connected) {
			Packet p = readPacket();
			if(p instanceof ChatJoinPacket) {
				ChatJoinPacket cp = (ChatJoinPacket) p;
				if(cp.isRequest()) {
					if(!obtainedChatRequest) {
						obtainedChatRequest=true;
						sendPacket(new ChatConfigPacket(uuid));
					} else {
						System.err.println("WARNING SOMEONE SENDING MORE JOIN PACKETS!!!");
						System.err.println("For DDOS reasons disconnecting user");
					}
				} else if(obtainedChatRequest) {
					this.name=cp.getName();
					thisSender=new MessageSender(uuid, name);
					SAFE_CONNECTION=true;
				}
			} else if(SAFE_CONNECTION) {
				if(p instanceof ChatLeftPacket) {
					ChatLeftPacket cl = (ChatLeftPacket) p;
					if(cl.getSender()==uuid) {
						localDisconnect();
					}
				} else if(p instanceof ChatMessagePacket) {
					ChatMessagePacket msp = (ChatMessagePacket) p;
					if(msp.getSender()==uuid) {
						chat.send(new Message(thisSender, msp.getContent()), this);
					}
				} else if(p instanceof ChatConfigPacket) {
					System.err.println("WARNING Client sent configuration packet");
					disconnect("Interrupted connection cannot send configuration packet to server");
				}
			}
		}
		try {
			thread.join(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void localDisconnect() {
		chat.getServer().removeClient(this);
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connected=false;
	}
	
	public void disconnect() {
		disconnect("Disconnected");
	}
	public void disconnect(CharSequence reason) {
		try {
			sendPacket(new ChatLeftPacket(uuid, reason+""));
			chat.getServer().removeClient(this);
			socket.close();
			connected=false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getName() {
		return name;
	}
	public MessageSender getAsSender() {
		return thisSender;
	}
	
}
