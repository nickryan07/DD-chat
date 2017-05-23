package com.dd.client;

import java.io.IOException;
import java.net.*;

public class Client {
	
	private String name, address, port;
	private InetAddress ip_address;
	private Thread messages;
	private int userid = -1;
	private DatagramSocket socket;
	
	public Client(String name, String address, String port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}

	public String getPort() {
		return port;
	}
	
	public boolean connect(String address) {
		try {
			socket = new DatagramSocket();
			ip_address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String receiveData() {
		byte[] input = new byte[512];
		DatagramPacket packets = new DatagramPacket(input, input.length);
		try{
			socket.receive(packets);
		} catch(IOException ei) {
			ei.printStackTrace();
		}
		String output = new String(packets.getData());
		return output;
	}
	
	public String sendData(final byte[] bytes) {
		messages = new Thread("Messages") {
			public void run() {
				DatagramPacket message = new DatagramPacket(bytes, bytes.length, ip_address, Integer.parseInt(port));
				try {
					socket.send(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		messages.start();
		return "";
	}
	
	public void terminate() {
		new Thread() {
			public void run() {
				synchronized(socket) {
					socket.close();
				}
			}
		}.start();
	}
	
	public void setID(int ID) {
		this.userid = ID;
	}
	
	public int getID() {
		return userid;
	}
}
