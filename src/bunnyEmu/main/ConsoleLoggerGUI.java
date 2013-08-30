package bunnyEmu.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;

import bunnyEmu.main.db.DatabaseHandler;
import bunnyEmu.main.utils.crypto.HashHelper;

/**
 * Command part of the GUI
 * 
 * @author Wazy
 *
 */
/* handle console commands here */
public class ConsoleLoggerGUI implements Runnable {
	
	/* The ConsoleLogger output */
	private JTextArea textArea;
	private String command = "";
	
	public ConsoleLoggerGUI(JTextArea textArea){
		this.textArea = textArea;
	}
	
	public void run() {
		textArea.append("Type commands below after >>> indicators.\n");
		try {
			textArea.addKeyListener(new KeyAdapter() {
            	private StringBuilder commandBuilder = new StringBuilder();
	            @Override
	            public void keyTyped(KeyEvent e) {
	                if (((int) e.getKeyChar()) == 10) {
	                	command = commandBuilder.toString();
	                	commandBuilder.setLength(0);
	                }
	                else {
	                	commandBuilder.append(e.getKeyChar());
	                }
	                super.keyTyped(e);
	            }
	        });
			while (true) {
				textArea.append(">>> ");
				// not ready to read anything yet
				
				while (command.isEmpty()) {
					Thread.sleep(200);
				}

				if (command.equals("commands") || command.equals("help")) {
					textArea.append("Available commands are: {'create', 'online', 'shutdown', 'help', 'commands'}.\n");
				}
				else if (command.equals("shutdown")) {
					textArea.append("\n!!!Console shutdown imminent!!!\n");
					System.exit(0);
				}
				else if (command.equals("online")) {
					textArea.append("This command is not completely implemented yet.\n");
					//System.out.print("These accounts are online: ");
					//DatabaseHandler.queryOnline();
					//System.out.print("\n");
				}
				else if (command.contains("create")) {
					String[] accountInfo = command.split(" ");
					
					if (accountInfo.length != 3) {
						textArea.append("Usage for this command is: create account password\n");
						command="";
						continue;
					}
					
					/* get userName and hashPW here and then insert into database */
					String userName = accountInfo[1];
					
					if (userName.isEmpty()) {
						textArea.append("Username cannot be empty!\n");
						continue;
					}
					
					String hashPW = HashHelper.generatePasswordHash(accountInfo);
					
					Boolean result = DatabaseHandler.createAccount(userName, hashPW);
					
					if (result) {
						textArea.append("Account: " + userName + " created successfully!\n");
					}
					else {
						textArea.append("Failed to create: " + userName + ". Name is probably already taken.\n");
					}
				}
				else {
					textArea.append("Unrecognized command. Try typing 'help'.\n");
				}
				command = "";
			}
		}
		catch (Exception e) {};
	}
}
