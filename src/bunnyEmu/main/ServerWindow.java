package bunnyEmu.main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import bunnyEmu.main.entities.packet.Packet;

public class ServerWindow {

	private JFrame frame;
	private static JTextArea textArea;
	private static String[][] packets;
	private static ArrayList<Packet> packetLog;
	
	private static JTable table;
	final static String[] headers = new String[] {"Name", "Opcode", "Size" };

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
	
	public static void updatePackets(ArrayList<Packet> newPacketLog){
		packetLog = newPacketLog;
		packets = new String[packetLog.size()][3];
		
		for(int i = 0; i < packets.length; i++){
			Packet p = packetLog.get(i);
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
				packetFrame.setBounds(100, 100, 340, 300);
				
				JTextArea textArea = new JTextArea();
				textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
				textArea.setColumns(65);
				textArea.setRows(14);
				textArea.append(packetLog.get(rowIndex).toStringBeautified());
				packetFrame.getContentPane().add(textArea);
				
				packetFrame.setVisible(true);
			}
		});
		

		JScrollPane scroll2 = new JScrollPane (table);
		scroll2.setPreferredSize(new Dimension(400, 200));
		packetPanel.add(scroll2);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public static void appendOut(String text){
		if(textArea != null)
			textArea.append(text + "\n");
	}
}
