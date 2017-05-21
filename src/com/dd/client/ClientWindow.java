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

public class ClientWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextArea txtArea;
	
	private String name;

	/**
	 * Create the frame.
	 */
	public ClientWindow(String name, String address, String port) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.name = name;
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
					sendMessage(textField.getText());
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
					sendMessage(textField.getText());
				}
			}
		});
		btnSend.setBounds(580, 369, 69, 23);
		contentPane.add(btnSend);
		setVisible(true);
		displayMessage("User: "+name+ " was authenticated on ["+address+":"+port+"]");
	}
	
	private void sendMessage(String message) {
		displayMessage("["+name+"]: "+ message);
		textField.setText("");
	}
	
	private void displayMessage(String message) {
		txtArea.append(message + "\n\r");
	}
}
