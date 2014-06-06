package bunnyEmu.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bunnyEmu.main.db.DatabaseHandler;
import bunnyEmu.main.utils.Logger;
import bunnyEmu.main.utils.crypto.HashHelper;

/* handle console commands here */
public class ConsoleLoggerCMD implements Runnable {
	public void run() {
		Logger.writeError("\nType commands below after >>> indicators.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (true) {
				Logger.writeError(">>> ");

				// not ready to read anything yet
				while (!br.ready()) {
					Thread.sleep(200);
				}

				String command = br.readLine();

				if (command.equals("commands") || command.equals("help")) {
					Logger.writeError("Available commands are: {'create', 'online', 'shutdown', 'help', 'commands'}.");
				}
				else if (command.equals("shutdown")) {
					Logger.writeError("\n!!!Console shutdown imminent!!!");
					System.exit(0);
				}
				else if (command.equals("online")) {
					Logger.writeError("This command is not completely implemented yet.");
					//System.out.print("These accounts are online: ");
					//DatabaseHandler.queryOnline();
					//System.out.print("\n");
				}
				else if (command.contains("create")) {
					String[] accountInfo = command.split(" ");
					
					if (accountInfo.length != 3) {
						Logger.writeError("Usage for this command is: create account password");
						continue;
					}
					
					/* get userName and hashPW here and then insert into database */
					String userName = accountInfo[1];
					
					if (userName.isEmpty()) {
						Logger.writeError("Username cannot be empty!");
						continue;
					}
					
					String hashPW = HashHelper.generatePasswordHash(accountInfo);
					
					Boolean result = DatabaseHandler.createAccount(userName, hashPW);
					
					if (result) {
						Logger.writeError("Account: " + userName + " created successfully!");
					}
					else {
						Logger.writeError("Failed to create: " + userName + ". Name is probably already taken");
					}
				}
				else if (command.isEmpty()) {
					continue;
				}
				else {
					Logger.writeError("Unrecognized command. Try typing 'help'.");
				}
			}
		}
		catch (Exception e) {};
	}
}