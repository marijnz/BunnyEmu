package bunnyEmu.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bunnyEmu.main.db.DatabaseHandler;
import bunnyEmu.main.utils.crypto.HashHelper;
import misc.Logger;

/* handle console commands here */
public class ConsoleLoggerCMD implements Runnable {
	public void run() {
		System.out.println("\nType commands below after >>> indicators.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (true) {
				Logger.writeLog(">>> ", Logger.LOG_TYPE_VERBOSE);

				// not ready to read anything yet
				while (!br.ready()) {
					Thread.sleep(200);
				}

				String command = br.readLine();

				if (command.equals("commands") || command.equals("help")) {
					System.out.println("Available commands are: {'create', 'online', 'shutdown', 'help', 'commands'}.");
				} else if(command.equals("shutdown")) {
					System.out.println("\n!!!Console shutdown imminent!!!");
					System.exit(0);
				} else if(command.equals("online")) {
					System.out.println("This command is not completely implemented yet.");
					//System.out.print("These accounts are online: ");
					//DatabaseHandler.queryOnline();
					//System.out.print("\n");
				} else if(command.contains("create")) {
					String[] accountInfo = command.split(" ");
					
					if(accountInfo.length != 3) {
						System.out.println("Usage for this command is: create account password");
						continue;
					}
					
					/* get userName and hashPW here and then insert into database */
					String userName = accountInfo[1];
					
					if(userName.isEmpty()) {
						Logger.writeLog("Username cannot be empty!", Logger.LOG_TYPE_WARNING);
						continue;
					}
					
					String hashPW = HashHelper.generatePasswordHash(accountInfo);
					
					Boolean result = DatabaseHandler.createAccount(userName, hashPW);
					
					if(result) {
						Logger.writeLog("Account: " + userName + " created successfully!", Logger.LOG_TYPE_VERBOSE);
					} else {
						Logger.writeLog("Failed to create: " + userName + ". Name is probably already taken", Logger.LOG_TYPE_VERBOSE);
					}
				} else if (command.isEmpty()) {
					continue;
				} else {
					Logger.writeLog("Unrecognized command. Try typing 'help'.", Logger.LOG_TYPE_VERBOSE);
				}
			}
		}
		catch (Exception e) {};
	}
}