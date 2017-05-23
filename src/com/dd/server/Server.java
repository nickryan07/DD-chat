package com.dd.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {

	private int port;
	private boolean online = false;
	private final int MAX_ATTEMPTS = 6;
	
	private DatagramSocket socket;
	
	private List<ServerLauncher> connections = new ArrayList<ServerLauncher>();
	private List<Integer> responses = new ArrayList<Integer>();
	private Thread main, handler, send, receive;
	
	Server(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		main = new Thread(this, "Main");
		main.start();
	}

	public void run() {
		online = true;
		handleClients();
		receive();
		System.out.println("Server launched on port number " + port);
		Scanner scanner = new Scanner(System.in);
		while (online) {
			String command = scanner.nextLine();
			if (!command.startsWith("@")) {
				sendToAll("@m@Server: " + command + "@e@");
				continue;
			}
			command = command.substring(1);
			if (command.equals("quit")) {
				quit();
			}
		}
		scanner.close();
	}
	
	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		//if (raw) System.out.println(string);
		if (string.startsWith("@c@")) {
			//UUID id = UUID.randomUUID();
			int id = Identification.getIdentifier();
			String name = string.split("@c@|@e@")[1];
			System.out.println(name + "(" + id + ") connected!");
			connections.add(new ServerLauncher(name, packet.getAddress(), packet.getPort(), id));
			String ID = "@c@" + id;
			send(ID, packet.getAddress(), packet.getPort());
		} else if (string.startsWith("@m@")) {
			sendToAll(string);
		} else if (string.startsWith("@d@")) {
			String id = string.split("@d@|@e@")[1];
			disconnect(Integer.parseInt(id), true);
		} else if (string.startsWith("@i@")) {
			responses.add(Integer.parseInt(string.split("@i@|@e@")[1]));
		} else {
			System.out.println(string);
		}
	}
	
	private void handleClients() {
		handler = new Thread("Client Handler") {
			public void run() {
				while(online) {
					sendToAll("@i@server");
					setState();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < connections.size(); i++) {
						ServerLauncher c = connections.get(i);
						if (!responses.contains(c.getID())) {
							if (c.attempt >= MAX_ATTEMPTS) {
								disconnect(c.getID(), false);
							} else {
								c.attempt++;
							}
						} else {
							responses.remove(new Integer(c.getID()));
							c.attempt = 0;
						}
					}
				}
			}
		};
		handler.start();
	}
	
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while(online) {
					byte[] data = new byte[512];
					DatagramPacket received = new DatagramPacket(data, data.length);
					try {
						socket.receive(received);
					} catch(SocketException e) {
					} catch(IOException e) {
						
					}
					process(received);
				}
			}
		};
		receive.start();
	}
	
	private void setState() {
		if(connections.size() <= 0) {
			return;
		}
		String users = "@u@";
		for (int i = 0; i < connections.size() - 1; i++) {
			users += connections.get(i).name + "@n@";
		}
		users += connections.get(connections.size() - 1).name + "@e@";
		sendToAll(users);
	}
	
	private void quit() {
		for(int i = 0; i < connections.size(); i++) {
			disconnect(connections.get(i).getID(), true);
		}
		online = false;
		socket.close();
	}
	
	private void disconnect(int id, boolean state) {
		ServerLauncher l = null;
		boolean real = false;
		for(int i = 0; i < connections.size(); i++) {
			if(connections.get(i).getID() == id) {
				l = connections.get(i);
				connections.remove(i);
				real = true;
				break;
			}
		}
		if(!real)
			return;
		String message = "";
		if (state) {
			message = "Client " + l.name + " (" + l.getID() + ") @ " + l.address.toString() + ":" + l.port + " disconnected.";
		} else {
			message = "Client " + l.name + " (" + l.getID() + ") @ " + l.address.toString() + ":" + l.port + " timed out.";
		}
		System.out.println(message);
	}
	
	private void sendToAll(String message) {
		if (message.startsWith("@m@")) {
			String text = message.substring(3);
			text = text.split("@e@")[0];
			System.out.println(message);
		}
		for (int i = 0; i < connections.size(); i++) {
			ServerLauncher client = connections.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}

	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void send(String message, InetAddress address, int port) {
		message += "@e@";
		send(message.getBytes(), address, port);
	}
}
