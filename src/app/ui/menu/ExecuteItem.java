package app.ui.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class ExecuteItem extends JPanel {
	
	protected GridBagConstraints gbc = new GridBagConstraints();
	
	public ExecuteItem(String title){
		
		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 45));
//		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		JPanel buttonWrap = new JPanel();
		buttonWrap.setLayout(new GridBagLayout());
		buttonWrap.setBackground(Color.GRAY);
		
		JButton jb = new JButton(title);
		jb.setFocusPainted(false);
		jb.setBorderPainted(false);
		jb.setBackground(new Color(157, 244, 65));
		
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				execute();
			}
		});
		
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(1, 1, 1, 1);
		buttonWrap.add(jb, gbc);
		
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(buttonWrap, gbc);
		
	}
	
	public abstract void execute();

}
