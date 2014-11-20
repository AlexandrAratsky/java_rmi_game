package game.client.gui.elements;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class JPanelAbout extends JPanel {

	public JPanelAbout() {
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("\u0418\u0433\u0440\u0430 \"\u041A\u043E\u0440\u0438\u0434\u043E\u0440\u0447\u0438\u043A\u0438\"");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblNewLabel, BorderLayout.NORTH);
		
		JLabel lblNewLabel_1 = new JLabel("Author Aratsky Alexandr ");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblNewLabel_1, BorderLayout.SOUTH);
		
		final JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBackground(UIManager.getColor("Button.background"));
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textPane.setText("\u0412 \u0445\u043E\u0434\u0435 \u0438\u0433\u0440\u044B \u0438\u0433\u0440\u0430\u044E\u0449\u0438\u0435 \u043F\u043E\u043E\u0447\u0435\u0440\u0435\u0434\u043D\u043E \u043F\u0440\u043E\u0432\u043E\u0434\u044F\u0442 \u0432\u0435\u0440\u0442\u0438\u043A\u0430\u043B\u044C\u043D\u044B\u0435 \u0438\u043B\u0438 \u0433\u043E\u0440\u0438\u0437\u043E\u043D\u0442\u0430\u043B\u044C\u043D\u044B\u0435 \u043B\u0438\u043D\u0438\u0438 \u0434\u043B\u0438\u043D\u043E\u0439 \u0432 \u043E\u0434\u043D\u0443 \u043A\u043B\u0435\u0442\u043A\u0443. \u0417\u0430\u0434\u0430\u0447\u0430 \u2013 \u0437\u0430\u043C\u043A\u043D\u0443\u0442\u044C \u0441\u0432\u043E\u0435\u0439 \u043B\u0438\u043D\u0438\u0435\u0439 \u043A\u043B\u0435\u0442\u043A\u0443. \u0418\u0433\u0440\u043E\u043A, \u043A\u043E\u0442\u043E\u0440\u043E\u043C\u0443 \u044D\u0442\u043E \u0443\u0434\u0430\u043B\u043E\u0441\u044C, \u0441\u0442\u0430\u0432\u0438\u0442 \u0432 \u043A\u043B\u0435\u0442\u043A\u0435 \u0441\u0432\u043E\u0439 \u0443\u0441\u043B\u043E\u0432\u043D\u044B\u0439 \u0437\u043D\u0430\u043A. \u0412\u0434\u043E\u0431\u0430\u0432\u043E\u043A, \u0442\u0430\u043A\u043E\u0439 \u0438\u0433\u0440\u043E\u043A \u043F\u043E\u043B\u0443\u0447\u0430\u0435\u0442 \u0434\u043E\u043F\u043E\u043B\u043D\u0438\u0442\u0435\u043B\u044C\u043D\u044B\u0439 \u0445\u043E\u0434.\r\n\u0418\u0433\u0440\u0430 \u0438\u0434\u0435\u0442 \u0434\u043E \u0442\u0435\u0445 \u043F\u043E\u0440, \u043F\u043E\u043A\u0430 \u043D\u0435 \u0431\u0443\u0434\u0435\u0442 \u0437\u0430\u043F\u043E\u043B\u043D\u0435\u043D\u043E \u0432\u0441\u0435 \u043F\u043E\u043B\u0435. \u041F\u043E\u0441\u043B\u0435 \u044D\u0442\u043E\u0433\u043E \u043F\u043E\u0434\u0441\u0447\u0438\u0442\u044B\u0432\u0430\u044E\u0442, \u00AB\u0447\u044C\u0438\u0445\u00BB \u043A\u043B\u0435\u0442\u043E\u043A \u043D\u0430 \u043F\u043E\u043B\u0435 \u0431\u043E\u043B\u044C\u0448\u0435. \u042D\u0442\u043E\u0442 \u0438\u0433\u0440\u043E\u043A \u0438 \u0441\u0442\u0430\u043D\u043E\u0432\u0438\u0442\u0441\u044F \u043F\u043E\u0431\u0435\u0434\u0438\u0442\u0435\u043B\u0435\u043C.");
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setMaximumSize(new Dimension(205, 32767));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

	}

}
