package BTTH1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DropMode;
import java.awt.SystemColor;

public class Chat_TCPClient {
	private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String username;
	private JFrame frmChatBox;
	private JTextField textTen;
	private JTextArea txtSend;
	private JTextArea txtReceive;

	public void startClient() {
        try {
            this.socket = new Socket("localhost",6000);
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos= new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat_TCPClient window = new Chat_TCPClient();
					window.frmChatBox.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Chat_TCPClient(){
		initialize();
		try {
			startClient();
			ListenMess m = new ListenMess();m.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	public void getUsername(String username) {
		this.username = username;
	}
	
	private void initialize() {
		frmChatBox = new JFrame();
		frmChatBox.getContentPane().setBackground(SystemColor.activeCaption);
		frmChatBox.setTitle("Chat box");
		frmChatBox.setBounds(100, 100, 540, 456);
		frmChatBox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatBox.getContentPane().setLayout(null);
		
		textTen = new JTextField();
		textTen.setBounds(84, 27, 165, 33);
		frmChatBox.getContentPane().add(textTen);
		textTen.setColumns(10);
		
		JButton btnChange = new JButton("Change");
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textTen.enable();
			}
		});
		btnChange.setFont(new Font("Consolas", Font.BOLD, 12));
		btnChange.setBounds(279, 29, 84, 33);
		frmChatBox.getContentPane().add(btnChange);
		
		JButton btnConfirm = new JButton("Connect");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textTen.disable();
				getUsername(textTen.getText().toString());
				try {
					dos.writeUTF(username);
					dos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				listenForMessage();
			}
		});
		txtSend = new JTextArea();
		txtSend.setBounds(10, 376, 399, 33);
		frmChatBox.getContentPane().add(txtSend);
		
		txtReceive = new JTextArea();
		txtReceive.setBounds(10, 103, 506, 233);
		frmChatBox.getContentPane().add(txtReceive);
		
		JLabel lblNewLabel = new JLabel("Nh\u1EADn:");
		lblNewLabel.setFont(new Font("Consolas", Font.BOLD, 19));
		lblNewLabel.setBounds(10, 77, 170, 26);
		frmChatBox.getContentPane().add(lblNewLabel);
		
		JLabel lblGi = new JLabel("G\u1EEDi: ");
		lblGi.setFont(new Font("Consolas", Font.BOLD, 19));
		lblGi.setBounds(10, 351, 170, 26);
		frmChatBox.getContentPane().add(lblGi);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
//				listenForMessage();
				txtSend.setText("");
			}
		});
		btnNewButton.setFont(new Font("Consolas", Font.BOLD, 21));
		btnNewButton.setBounds(431, 372, 85, 33);
		frmChatBox.getContentPane().add(btnNewButton);
		
		btnConfirm.setFont(new Font("Consolas", Font.BOLD, 12));
		btnConfirm.setBounds(367, 29, 84, 33);
		frmChatBox.getContentPane().add(btnConfirm);
		
		JLabel lblTn = new JLabel("T\u00EAn:");
		lblTn.setFont(new Font("Consolas", Font.BOLD, 19));
		lblTn.setBounds(10, 31, 85, 26);
		frmChatBox.getContentPane().add(lblTn);
		
	}
	
	public void sendMessage() {
		String messageToSend = txtSend.getText().toString();
        try {
            if(socket.isConnected()) {
            	dos.writeUTF(username + ": " + messageToSend);
                dos.flush();
                txtReceive.setText(txtReceive.getText() + "\n" + messageToSend);
            }  
        } catch (IOException e) {

        }
    }
	class ListenMess extends Thread{
		public void run() {
			String msgFromGroupChat;            
            while (true) {
                try {
                    msgFromGroupChat = dis.readUTF();
                    txtReceive.setText(txtReceive.getText()+ "\n" +msgFromGroupChat);
                    System.out.println(msgFromGroupChat);
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
		}
	}
}

