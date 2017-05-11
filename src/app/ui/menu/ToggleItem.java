package app.ui.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class ToggleItem extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean toggeled;
	
	protected GridBagConstraints gbc = new GridBagConstraints();
	
	protected Toggle tog;
	
	public ToggleItem(String title){
		
		this.addMouseListener(this);
		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 45));
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(5, 20, 5, 10);
		
		JLabel jLab = new JLabel(title);
		jLab.setFont(jLab.getFont().deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD)));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(jLab, gbc);
		
		tog = new Toggle();
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		this.add(tog, gbc);
		
	}
	
	public void toggle(){
		
		tog.toggle();
		
		if (toggeled){
			toggleOff();
		} else {
			toggleOn();
		}
		toggeled = !toggeled;
		System.out.println(toggeled);
	}
	
	public abstract void toggleOn();
	public abstract void toggleOff();
	
	public boolean isToggled(){
		return toggeled;
	}
	
	public void setToggle(boolean b){
		
		toggeled = b;
		tog.setToggle(b);
		
		if (toggeled){
			toggleOn();
		} else {
			toggleOff();
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
