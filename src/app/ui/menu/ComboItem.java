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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class ComboItem extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected GridBagConstraints gbc = new GridBagConstraints();
	
	protected JComboBox<String> jComb;
	
	protected boolean opened;

	public ComboItem(String title, String[] options) {

		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 45));
		this.addMouseListener(this);

		gbc.fill = GridBagConstraints.BOTH;
//		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 0.4;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 20, 10, 10);

		JLabel jLab = new JLabel(title);
		jLab.setFont(jLab.getFont().deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD)));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(jLab, gbc);
		
		jComb = new JComboBox<>(options);
//		jComb.setPreferredSize(new Dimension(500, 550));
//		jComb.setSize(new Dimension(200, 30));
		jComb.setBackground(new Color(230, 230, 230));
		jComb.setBorder(BorderFactory.createEmptyBorder());
		jComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectionMade((String) jComb.getSelectedItem());
			}
		});
		
		gbc.weightx = 0.6;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		this.add(jComb, gbc);

	}
	
	public abstract void selectionMade(String selected);

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		this.setBackground(new Color(240, 240, 240));
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		jComb.showPopup();
		this.setBackground(new Color(235, 235, 235));
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.setBackground(new Color(240, 240, 240));
		
	}

}
