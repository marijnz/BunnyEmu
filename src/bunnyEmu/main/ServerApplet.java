package bunnyEmu.main;

import java.applet.Applet;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerApplet extends Applet implements ActionListener {

	private static final long serialVersionUID = 1L;

	Label label1 = new Label("BunnyEmu is offline");
	
	Label label2 = new Label("IP: ");
	
	public static TextArea debugLabel = new TextArea("Debug ");
	
	Button button1 = new Button("Start");
	
	Server server;

	public ServerApplet(){
		System.out.println("Started BunnyEmu");
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
		
		new Thread(){
			@Override
			public void run() {
				label2.setText("IP: loading");
				while(true){
					if(server.getServerSocket().getInetAddress() != null)
						label2.setText(server.getServerSocket().getInetAddress().toString());
					else
						label2.setText("IP: can't be loaded");
				}
			}
		};
		
	}

}

