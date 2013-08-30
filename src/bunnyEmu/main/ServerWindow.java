package bunnyEmu.main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.handlers.RealmHandler;
import bunnyEmu.main.utils.PacketLog;
import bunnyEmu.main.utils.PacketLog.PacketType;
import java.awt.Component;

public class ServerWindow {

	private JFrame frame;
	private static JTextArea textArea;
	private static String[][] packets;
	private static Packet[] packetLog;
	
	private static JTable table;
	final static String[] headers = new String[] {"Name", "Opcode", "Size" };
	static ArrayList<PacketType> packetLogTypes = new ArrayList<PacketType>();
	private JPanel commandPanel;

	/**
	 * Launch the application.
	 */
	public static void create() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow window = new ServerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void notifyChange(){
		// A new packet has been logged, do we want to see them?
		packetLog = PacketLog.getPackets(packetLogTypes);
		packets = new String[packetLog.length][3];
		
		for(int i = 0; i < packets.length; i++){
			Packet p = packetLog[i];
			packets[i][0] = p.sOpcode;
			packets[i][1] = String.valueOf(Integer.toHexString(p.nOpcode).toUpperCase());
			packets[i][2] = String.valueOf(p.size);
		}
			
		table.setModel(new DefaultTableModel(packets, headers));
		table.getColumnModel().getColumn(0).setPreferredWidth(220);
	}
	
	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 596, 319);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblBunnyEmu = new JLabel("BunnyEmu");
		lblBunnyEmu.setToolTipText("");
		lblBunnyEmu.setBounds(10, 10, 97, 14);
		frame.getContentPane().add(lblBunnyEmu);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 35, 560, 235);
		frame.getContentPane().add(tabbedPane);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setToolTipText("");
		tabbedPane.addTab("Output", null, outputPanel, null);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textArea.setColumns(65);
		textArea.setRows(14);
		outputPanel.add(textArea);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane (textArea);
		outputPanel.add(scroll);
		
		JPanel packetPanel = new JPanel();
		tabbedPane.addTab("Packets", null, packetPanel, null);
		
		packets = new String[0][3];
		
		
		
		table = new JTable(packets, headers);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowIndex = table.getSelectedRow();
				
				JFrame packetFrame = new JFrame();
				packetFrame.setBounds(100, 100, 350, 300);
				
				JTextArea textArea = new JTextArea();
				textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
				textArea.setColumns(35);
				textArea.setRows(14);
				textArea.append(packetLog[rowIndex].toStringBeautified());
				JScrollPane scroll = new JScrollPane (textArea);
				packetFrame.getContentPane().add(scroll);
				
				packetFrame.setVisible(true);
			}
		});
		packetPanel.setLayout(null);
		
		
		JScrollPane scroll2 = new JScrollPane (table);
		scroll2.setBounds(155, 0, 400, 207);
		scroll2.setPreferredSize(new Dimension(400, 200));
		packetPanel.add(scroll2);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		final JCheckBox checkboxServer = new JCheckBox("Server");
		checkboxServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPacketLogOption(PacketType.SERVER, checkboxServer.isSelected());
			}
		});
		checkboxServer.setBounds(6, 36, 97, 23);
		checkboxServer.setHorizontalAlignment(SwingConstants.LEFT);
		packetPanel.add(checkboxServer);
		
		final JCheckBox checkboxClientImpl = new JCheckBox("Client implemented");
		checkboxClientImpl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPacketLogOption(PacketType.CLIENT_KNOWN_IMPLEMENTED, checkboxClientImpl.isSelected());
			}
		});
		checkboxClientImpl.setHorizontalAlignment(SwingConstants.LEFT);
		checkboxClientImpl.setBounds(6, 62, 127, 23);
		packetPanel.add(checkboxClientImpl);
		
		final JCheckBox checkboxClientUnimpl = new JCheckBox("Client unimplemented");
		checkboxClientUnimpl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPacketLogOption(PacketType.CLIENT_KNOWN_UNIMPLEMENTED, checkboxClientUnimpl.isSelected());
			}
		});
		checkboxClientUnimpl.setHorizontalAlignment(SwingConstants.LEFT);
		checkboxClientUnimpl.setBounds(6, 88, 143, 23);
		packetPanel.add(checkboxClientUnimpl);
		
		final JCheckBox checkboxClientUnk = new JCheckBox("Client unknown");
		checkboxClientUnk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setPacketLogOption(PacketType.CLIENT_UNKNOWN, checkboxClientUnk.isSelected());
			}
		});
		checkboxClientUnk.setHorizontalAlignment(SwingConstants.LEFT);
		checkboxClientUnk.setBounds(6, 114, 127, 23);
		packetPanel.add(checkboxClientUnk);
		
		JPanel infoPanel = new JPanel();
		tabbedPane.addTab("Info", null, infoPanel, null);
		infoPanel.setLayout(null);
		
		final JLabel memoryLabel = new JLabel("Memory usage:");
		memoryLabel.setBounds(30, 11, 195, 22);
		infoPanel.add(memoryLabel);
		
		final JLabel clientsLabel = new JLabel("Clients logged in:");
		clientsLabel.setBounds(30, 66, 195, 22);
		infoPanel.add(clientsLabel);
		
		commandPanel = new JPanel();
		JTextArea commandArea = new JTextArea();
		commandArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		commandArea.setColumns(65);
		commandArea.setRows(14);
		
		DefaultCaret caret2 = (DefaultCaret)commandArea.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll3 = new JScrollPane (commandArea);
		commandPanel.add(scroll3);
		tabbedPane.addTab("Command", null, commandPanel, null);
		
		/* console commands are handled by this thread */
		Runnable loggerRunnable = new ConsoleLoggerGUI(commandArea);
		Thread loggerThread = new Thread(loggerRunnable);
		loggerThread.start();
		
		new Thread(){
			@Override
			public void run(){
				while(true){
					memoryLabel.setText("Kb memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
					clientsLabel.setText("Logged in clients: " + RealmHandler.getAllClientsAllRealms().size());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	private static void setPacketLogOption(PacketType type, boolean checked){
		if(checked && !packetLogTypes.contains(type))
			packetLogTypes.add(type);
		else
			packetLogTypes.remove(type);
		notifyChange();
	}
	
	public static void appendOut(String text){
		if(textArea != null)
			textArea.append(text + "\n");
	}
}
