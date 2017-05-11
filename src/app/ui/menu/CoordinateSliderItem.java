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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JSpinner.DefaultEditor;

import math.Intervall;

public abstract class CoordinateSliderItem extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SlideSpinner controlX, controlY, controlZ;
	private double x, y, z;
	
	private JButton resetButton;
	
	protected GridBagConstraints gbc = new GridBagConstraints();

	public CoordinateSliderItem(String title, Intervall bounds) {

		final CoordinateSliderItem parentRef = this;

		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 200));

		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 20, 5, 10);

		JLabel jLab = new JLabel(title);
		jLab.setFont(jLab.getFont()
				.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD)));
		jLab.setOpaque(true);

		gbc.weightx = 1;
		gbc.weighty = 0.2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(jLab, gbc);
		gbc.gridwidth = 1;
		
		Double[] steps = new Double[301];
		for (int i = 0; i< 301; i++){
			steps[i] = Math.round((i * 0.01 - 1.5) * 100) / 100.0;
		}
		
		controlX = new SlideSpinner(steps) {
			@Override
			public void valueChanged(Object o) {
				x = (double) o;
				parentRef.valueChanged(x, y, z);
			}
		};
		controlX.setIndex(150);
		
		controlY = new SlideSpinner(steps) {
			@Override
			public void valueChanged(Object o) {
				y = (double) o;
				parentRef.valueChanged(x, y, z);
			}
		};
		controlY.setIndex(150);
		
		controlZ = new SlideSpinner(steps) {
			@Override
			public void valueChanged(Object o) {
				z = (double) o;
				parentRef.valueChanged(x, y, z);
			}
		};
		controlZ.setIndex(150);
		
		String[] labels = new String[]{"X:", "Y:", "Z:"};
		SlideSpinner[] controls = new SlideSpinner[]{controlX, controlY, controlZ};
		
		gbc.weighty = 0.2;
		for (int i = 0; i< 3; i++){
			
			gbc.insets = new Insets(5, 20, 5, 5);
			
			gbc.weightx = 0.0;
			gbc.fill = GridBagConstraints.VERTICAL;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridx = 0;
			gbc.gridy = i + 1;
			this.add(new JLabel(labels[i]), gbc);
			
			gbc.insets = new Insets(5, 5, 5, 5);
			
			gbc.weightx = 0.2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.gridx = 1;
			gbc.gridy = i + 1;
			this.add(controls[i].getSpinner(), gbc);
			
			gbc.insets = new Insets(5, 5, 5, 10);
			
			gbc.weightx = 0.8;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.EAST;
			gbc.gridx = 2;
			gbc.gridy = i + 1;
			this.add(controls[i].getSlider(), gbc);
				
		}
		
		JPanel buttonWrap = new JPanel();
		buttonWrap.setLayout(new GridBagLayout());
		buttonWrap.setBackground(Color.GRAY);
		
		resetButton = new JButton("RESET");
		resetButton.setFocusPainted(false);
		resetButton.setBorderPainted(false);
		resetButton.setBackground(Color.LIGHT_GRAY);
		
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(1, 1, 1, 1);
		buttonWrap.add(resetButton, gbc);
		
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.gridwidth = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 4;
		this.add(buttonWrap, gbc);
	
	}
	
	public void reset(){
		controlX.setIndex(150);
		controlY.setIndex(150);
		controlZ.setIndex(150);
	}
	
	public abstract void valueChanged(double x, double y, double z);
	
}
