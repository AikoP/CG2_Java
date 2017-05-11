package app.ui.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class ToggleChooserItem extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean toggeled;
	
	protected GridBagConstraints gbc = new GridBagConstraints();
	
	private String option1, option2;
	private JLabel option1Lab, option2Lab;
	
	private final Color BLUE = new Color(119, 191, 255);
	private final Color LIGHT_GRAY = new Color(250, 250, 250);
	
	public ToggleChooserItem(String title, String option1, String option2){
		
		this.addMouseListener(this);
		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 75));
		
		this.option1 = option1;
		this.option2 = option2;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 20, 5, 10);
		
		JLabel jLab = new JLabel(title);
		jLab.setFont(jLab.getFont().deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD)));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(jLab, gbc);
		
		JPanel wrapPanel = new JPanel();
		wrapPanel.setLayout(new GridBagLayout());
		wrapPanel.setBackground(Color.GRAY);
		
		option1Lab = new JLabel(option1);
		option1Lab.setOpaque(true);
		option1Lab.setBackground(LIGHT_GRAY);
		option1Lab.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		option2Lab = new JLabel(option2);
		option2Lab.setOpaque(true);
		option2Lab.setBackground(BLUE);
		option2Lab.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(1, 1, 1, 1);
		wrapPanel.add(option1Lab, gbc);
		gbc.gridx = 1;
		wrapPanel.add(option2Lab, gbc);
		
		gbc.insets = new Insets(5, 0, 10, 0);
		gbc.gridwidth = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 4;
		this.add(wrapPanel, gbc);
		
	}
	
	public void toggle(){
		
		if (toggeled){
			toggled(option2);
			option1Lab.setBackground(LIGHT_GRAY);
			option2Lab.setBackground(BLUE);
		} else {
			toggled(option1);
			option1Lab.setBackground(BLUE);
			option2Lab.setBackground(LIGHT_GRAY);
		}
		toggeled = !toggeled;
		System.out.println(toggeled);
	}
	
	public abstract void toggled(String option);
	
	public void setToggle(boolean b){
		
		toggeled = b;
		
		if (toggeled){
			toggled(option2);
			option1Lab.setBackground(LIGHT_GRAY);
			option2Lab.setBackground(BLUE);
		} else {
			toggled(option1);
			option1Lab.setBackground(LIGHT_GRAY);
			option2Lab.setBackground(BLUE);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.setBackground(new Color(240, 240, 240));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocusInWindow();
		this.toggle();
		this.setBackground(new Color(235, 235, 235));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.setBackground(new Color(240, 240, 240));
	}
}