package com.dd.client;

import java.io.IOException;
import java.net.*;

public class Client {
	
	private String name, address, port;
	private InetAddress ip_address;
	private Thread messages;
	private int userid = -1;
	private Socket socket;
	
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
}
