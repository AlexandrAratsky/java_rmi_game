package game.client.gui;

import game.server.GameServer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Toolkit;
import java.util.Random;

import com.jgoodies.forms.factories.FormFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class JClientDialog extends JDialog {
	private JTextField txtRmi;
	private JTextField txtName;
	public JButton okButton;
	public JButton cancelButton;


	public JClientDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JClientDialog.class.getResource("/res/icons/user_5290.png")));
		setTitle("Server connection");
		setResizable(false);
		setBounds(100, 100, 278, 167);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JPanel contentPanel = new JPanel();
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			{
				JLabel lblNewLabel = new JLabel("RMI Server");
				contentPanel.add(lblNewLabel, "2, 2, right, default");
			}
			{
				txtRmi = new JTextField();
				txtRmi.setText(GameServer.RMI_SERVER + GameServer.RMI_NAME);
				contentPanel.add(txtRmi, "4, 2, fill, default");
				txtRmi.setColumns(10);
			}
			{
				JLabel lblNewLabel_1 = new JLabel("Name");
				contentPanel.add(lblNewLabel_1, "2, 4, right, default");
			}
			{
				txtName = new JTextField();
				txtName.setText("Player" + (new Random()).nextInt(20));
				contentPanel.add(txtName, "4, 4, fill, default");
				txtName.setColumns(10);
			}
		}
	}

	public String getName() {
		return txtName.getText();
	}
	public String getRmi() {
		return txtRmi.getText();
	}
	
	
}
