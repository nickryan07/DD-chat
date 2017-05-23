package com.dd.server;

public class Launcher {

	public Launcher(int port) {
		new Server(port);
	}

	public static void main(String[] args) {
		int port;
		if (args.length != 1) {
			System.out.println("Usage: java -jar ChernoChatServer.jar [port]");
			return;
		}
		port = Integer.parseInt(args[0]);
		new Launcher(port);
	}
	
}
