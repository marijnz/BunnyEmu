package bunnyEmu.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bunnyEmu.main.db.DatabaseHandler;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.crypto.HashHelper;

/* handle console commands here */
public class ConsoleLoggerCMD implements Runnable {
	public void run() {
		Log.log("\nType commands below after >>> indicators.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (true) {
				Log.log(">>> ");

				// not ready to read anything yet
				while (!br.ready()) {
					Thread.sleep(200);
				}

				String command = br.readLine();

				if (command.equals("commands") || command.equals("help")) {
					Log.log("Available commands are: {'create', 'online', 'shutdown', 'help', 'commands'}.");
				}
				else if (command.equals("shutdown")) {
					Log.log("\n!!!Console shutdown imminent!!!");
					System.exit(0);
				}
				else if (command.equals("online")) {
					Log.log("This command is not completely implemented yet.");
					//System.out.print("These accounts are online: ");
					//DatabaseHandler.queryOnline();
					//System.out.print("\n");
				}
				else if (command.contains("create")) {
					String[] accountInfo = command.split(" ");
					
					if (accountInfo.length != 3) {
						Log.log("Usage for this command is: create account password");
						continue;
					}
					
					/* get userName and hashPW here and then insert into database */
					String userName = accountInfo[1];
					
					if (userName.isEmpty()) {
						Log.log("Username cannot be empty!");
						continue;
					}
					
					String hashPW = HashHelper.generatePasswordHash(accountInfo);
					
					Boolean result = DatabaseHandler.createAccount(userName, hashPW);
					
					if (result) {
						Log.log(Log.INFO, "Account: " + userName + " created successfully!");
					}
					else {
						Log.log(Log.INFO, "Failed to create: " + userName + ". Name is probably already taken");
					}
				}
				else if (command.isEmpty()) {
					continue;
				}
				else {
					Log.log("Unrecognized command. Try typing 'help'.");
				}
			}
		}
		catch (Exception e) {};
	}
}