package bunnyEmu.main;

import java.applet.Applet;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import bunnyEmu.main.enums.LogType;
import bunnyEmu.main.utils.Logger;

public class ServerApplet extends Applet implements ActionListener {

	private static final long serialVersionUID = 1L;

	Label label1 = new Label("BunnyEmu is offline");
	Label label2 = new Label("IP: ");
	public static TextArea debugLabel = new TextArea("- Set your realmlist to localhost \n - Login with any username and passsword: \"password \"");
	Button button1 = new Button("Start");
	
	Server server;

	public ServerApplet(){
		Logger.writeLog("Started BunnyEmu", LogType.VERBOSE);
		this.setLayout(new FlowLayout());
		this.add(button1);
		this.add(label1);
		//this.add(label2);
		this.add(debugLabel);
		debugLabel.setLocation(100, 200);
		label2.setLocation(0, 60);
		this.add(label2);
		
		button1.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(){
			@Override
			public void run() {
				label1.setText("BunnyEmu online");
				server = new Server();
				server.launch();
				label1.setText("BunnyEmu stopped");
			}
		}.start();
		
		
	}

}

