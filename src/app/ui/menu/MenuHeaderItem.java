package app.ui.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuHeaderItem extends JPanel{
	
	protected GridBagConstraints gbc = new GridBagConstraints();

	public MenuHeaderItem(String title) {

		this.setBackground(Color.GRAY);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 35));
		this.setMinimumSize(new Dimension(250, 35));

		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(5, 10, 5, 10);

		JLabel jLab = new JLabel(title);
		jLab.setForeground(Color.WHITE);
//		jLab.setFont(jLab.getFont()
//				.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD)));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(jLab, gbc);


	}

}
