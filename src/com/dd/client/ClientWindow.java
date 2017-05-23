package com.dd.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class ClientWindow extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextArea txtArea;
	
	private Thread run, listen;
	
	//private String name;
	private boolean online = false;
	
	private Online users;
	
	private Client client;

	/**
	 * Create the frame.
	 */
	public ClientWindow(String name, String address, String port) {
		client = new Client(name, address, port);
		boolean connect = client.connect(address);
		if(!connect)
			System.err.println("Connection attempt failed");
		createWindow();
		displayMessage("Attempting a connection to " + address + ":" + port + ", user: " + name);
		String connection = "@c@" + name + "@e@";
		client.sendData(connection.getBytes());
		users = new Online();
		online = true;
		run = new Thread(this, "Online");
		run.start();
	}
	
	private void createWindow() {
		setTitle("DD Chat Client");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(665, 440);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtArea = new JTextArea();
		txtArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(txtArea);
		scroll.setBounds(10, 11, 639, 343);
		contentPane.add(scroll);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k) {
				if(k.getKeyCode() == KeyEvent.VK_ENTER && textField.getText().length() >= 1) {
					//sendMessage(textField.getText());
					sendMessage(textField.getText(), true);
				}	
			}
		});
		textField.setBounds(10, 370, 556, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField.getText().length() >= 1) {
					//sendMessage(textField.getText());
					sendMessage(textField.getText(), true);
				}
			}
		});
		btnSend.setBounds(578, 368, 69, 26);
		contentPane.add(btnSend);
		setVisible(true);
		//displayMessage("User: ")); was authenticated on ["+address+":"+port+"]");
	}
	
	public void run() {
		listen();
	}
	
	public void listen() {
		listen = new Thread("Listening") {
			public void run() {
				while(online) {
					String message = client.receiveData();
					if(message.startsWith("@c@")) {
						client.setID(Integer.parseInt(message.split("@c@|@e@")[1]));
						displayMessage("Successfully connected to server: " + client.getID());
					} else if (message.startsWith("@m@")) {
						String text = message.substring(3);
						text = text.split("@e@")[0];
						displayMessage(text);
					} else if (message.startsWith("@i@")) {
						String text = "@i@" + client.getID() + "@e@";
						sendMessage(text, false);
					} else if (message.startsWith("@u@")) {
						String[] u = message.split("@u@|@n@|@e@");
						users.update(Arrays.copyOfRange(u, 1, u.length - 1));
					}
				}
			}
		};
		listen.start();
	}
	
	private void sendMessage(String message, boolean msg) {
		//displayMessage("["+name+"]: "+ message);
		if(msg) {
			message = "[" + client.getName() + "]: " + message;
			message = "@m@" + message + "@e@";
			textField.setText("");
		}
		client.sendData(message.getBytes());
	}
	
	private void displayMessage(String message) {
		txtArea.append(message + "\n\r");
	}
}
