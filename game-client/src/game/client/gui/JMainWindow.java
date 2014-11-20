package game.client.gui;

import game.client.gui.elements.JBoardArea;
import game.client.gui.elements.JChatPanel;
import game.client.gui.elements.JEndDialog;
import game.client.gui.elements.JOptionsPanel;
import game.client.gui.elements.JScoreTable;
import game.enigne.Player;
import game.server.GameServer;
import game.server.impl.ServerCallbacksAdapter;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

@SuppressWarnings("serial")
public class JMainWindow extends JFrame {

	private JPanel contentPane;
	private GameServer server;
	private static int playerID = -1;
	private static String playerName;
	
	private JBoardArea gameArea;
	private JScoreTable scoreArea;
	private JChatPanel chat;
	private JOptionsPanel optons;
	private JPanel scoreBoard;

	/**
	 * Create the frame.
	 */
	public JMainWindow(GameServer gameServer, String name) {
		setTitle("Goridors 1.0: " + name);
		this.server = gameServer;
		
		try {
			server.addCallbackListener(new ServerCallbacksAdapter() {
				private static final long serialVersionUID = -956862938989184585L;
				@Override
				public void boardChanege() throws RemoteException {
					gameArea.repaint();
				}
				@Override
				public void nextPlayer(Player next) throws RemoteException {
					scoreArea.nextPlayer(next.getID());
					if (next.getID() == playerID) gameArea.on();
					else gameArea.off();
				}
				@Override
				public void updateScore(int ID, int score) throws RemoteException {
					scoreArea.upgradeScore(ID, score);
				}
				@Override
				public void startGame() throws RemoteException {
					scoreArea = new JScoreTable(server.getPlayerslist());
					scoreBoard.add(scoreArea, BorderLayout.CENTER);
					scoreArea.setVisible(true);
					gameArea.setVisible(true);
					chat.setVisible(true);
					optons.setVisible(true);
				}
				@Override
				public void newMessageForChat(String name, String text) throws RemoteException {
					chat.newMessage(name, text);
				}
				@Override
				public void gameOver(int idWinner) throws RemoteException {
					gameArea.endGame();
					scoreArea.setEnabled(false);
					optons.setEnabled(false);
					chat.setEnabled(false);
					JEndDialog dialog = new JEndDialog(playerID, server.getPlayerslist());
					dialog.setBounds(getBounds().x, getBounds().x, 260, 260);
					dialog.setVisible(true);
				}
			});
			playerID = server.addPlayer(name);
        	playerName = name;
		} catch (RemoteException e) { e.printStackTrace(); }	
		
		initGUI(server);
	}
	
	private void initGUI(GameServer board) {
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(JMainWindow.class.getResource("/res/icons/package_games_board_9247.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 700, 500);
		contentPane =  new JPanel();
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(100dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		setContentPane(contentPane);
		
		JPanel gameBoard = new JPanel();
		TitledBorder title1 = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Game Board");
		title1.setTitleJustification(TitledBorder.LEFT);
		gameBoard.setBorder(title1);
		gameBoard.setLayout(new BorderLayout(0, 0));
		contentPane.add(gameBoard, "2, 2, 2, 3, fill, fill");
		
		gameArea = new JBoardArea(board);
		gameArea.setVisible(false);
		gameBoard.add(gameArea, BorderLayout.CENTER);
		
		
		scoreBoard = new JPanel();
		scoreBoard.setLayout(new BorderLayout(0, 0));
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Score Table");
		title.setTitleJustification(TitledBorder.CENTER);
		scoreBoard.setBorder(title);
		contentPane.add(scoreBoard, "5, 2, fill, fill");
		
		final JPanel optionsPanel = new JPanel();
		contentPane.add(optionsPanel, "5, 4, fill, fill");
		optionsPanel.setLayout(new BorderLayout(0, 0));
		TitledBorder title3 = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Options");
		title3.setTitleJustification(TitledBorder.LEFT);
		optionsPanel.setBorder(title3);
				
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		optionsPanel.add(tabbedPane);
		
		JLayeredPane optionsGraphics = new JLayeredPane();
		tabbedPane.addTab("Graphics", null, optionsGraphics, null);
		optionsGraphics.setLayout(new BorderLayout(0, 0));
		
		optons = new JOptionsPanel(gameArea);
		optons.setVisible(false);
		optionsGraphics.add(optons, BorderLayout.CENTER);
		
		JLayeredPane connectionPanel = new JLayeredPane();
		tabbedPane.addTab("Chat", null, connectionPanel, null);
		connectionPanel.setLayout(new BorderLayout(0, 0));
		chat = new JChatPanel();
		chat.setVisible(false);
		chat.addNewMessageListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					server.newMessage("[" + playerID + "] " + playerName, chat.getTextMessage());
				} catch (RemoteException e1) { e1.printStackTrace(); }
			}
		});
		connectionPanel.add(chat, BorderLayout.CENTER);
		
		JLayeredPane aboutPanel = new JLayeredPane();
		tabbedPane.addTab("About", null, aboutPanel, null);
	}

}
