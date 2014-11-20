package game.client;

import game.client.gui.JClientDialog;
import game.client.gui.JMainWindow;
import game.server.GameServer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.Remote;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class GameClient {

	private static void trySetNimbusLook() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
		        if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
		} catch (Exception e) {
		    try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } catch (Exception ex) { ex.printStackTrace(); }
		}
	}
	
	private static void startGame(String rmi, String name) {
		GameServer server = null;
		try {
        	Remote RemoteObject = Naming.lookup(rmi);
        	server = (GameServer)RemoteObject;
        	if (server == null) throw new Exception("RemoteObject return null");
        	else JOptionPane.showMessageDialog(null, "Server connected");
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null, "Client exception: " + e.toString(), 
        			"Cinnection failed", JOptionPane.ERROR_MESSAGE);
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            System.exit(-1);
        }		
		JMainWindow window = new JMainWindow(server, name);
		window.setVisible(true);
	}
	
	public static void main(String[] args) {
		
		trySetNimbusLook();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final JClientDialog dialog = new JClientDialog();
					dialog.setVisible(true);
					dialog.okButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
							String rmi = dialog.getRmi();
							String name = dialog.getName();
							dialog.dispose();
							startGame(rmi, name);
						}
					});
				} catch (Exception e) { e.printStackTrace(); }
			}
		});

	}

}
