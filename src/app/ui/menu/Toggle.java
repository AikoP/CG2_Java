package app.ui.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Toggle extends JPanel{
	
	private JPanel leftPane, rightPane;
	private boolean toggled;
	
	private GridBagConstraints gbc = new GridBagConstraints();
	
	public Toggle(){
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		
		gbc.fill = GridBagConstraints.BOTH;
		
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new GridBagLayout());
		this.setMaximumSize(new Dimension(100, 20));
		this.setPreferredSize(new Dimension(35, 15));
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		leftPane = new JPanel();
		leftPane.setBackground(new Color(230, 230, 230));
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(leftPane, gbc);
		
		
		rightPane = new JPanel();
		rightPane.setBackground(new Color(255, 114, 89));
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(rightPane, gbc);
		
	}
	
	public synchronized void toggle(){
		
		if (toggled){
			leftPane.setBackground(new Color(230, 230, 230));
			rightPane.setBackground(new Color(255, 114, 89));
		} else {
			leftPane.setBackground(new Color(157, 244, 65));
			rightPane.setBackground(new Color(230, 230, 230));
		}
		toggled = !toggled;
		
	}

	public void setToggle(boolean b) {
		if (b != toggled){
			toggle();
		}
	}
	

}
