package com.dd.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;

	/**
	 * Create the frame.
	 */
	public LoginWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 365);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(168, 135, 158, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtAddress = new JTextField();
		txtAddress.setEditable(false);
		txtAddress.setEnabled(false);
		txtAddress.setText("127.0.0.1");
		txtAddress.setBounds(168, 177, 158, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		JLabel lblVer = new JLabel("Ver 1.0");
		lblVer.setBounds(428, 312, 46, 14);
		contentPane.add(lblVer);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(107, 138, 46, 14);
		contentPane.add(lblName);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setBounds(107, 180, 54, 14);
		contentPane.add(lblAddress);
		
		JLabel lblDdChatLogin = new JLabel("D&D Chat Login");
		lblDdChatLogin.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblDdChatLogin.setBounds(164, 26, 166, 50);
		contentPane.add(lblDdChatLogin);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = txtName.getText();
				String address = txtAddress.getText();
				String port = txtPort.getText();	
				authenticate(name, address, port);
			}
		});
		btnLogin.setBounds(202, 262, 89, 23);
		contentPane.add(btnLogin);
		
		txtPort = new JTextField();
		txtPort.setEditable(false);
		txtPort.setEnabled(false);
		txtPort.setText("9001");
		txtPort.setBounds(215, 217, 64, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(107, 220, 46, 14);
		contentPane.add(lblPort);
	}
	
	private void authenticate(String name, String address, String port) {
		if(name.length() < 1) {
			message("Please enter a valid username");
			return;
		}
		dispose();
		new ClientWindow(name, address, port);
		//System.out.println("User: "+name+ " was authenticated on ["+address+":"+port+"]");
	}
	
	private void message(String s){
		JOptionPane.showMessageDialog(null, s);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
