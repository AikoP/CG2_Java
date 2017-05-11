package app.ui.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.util.Collections;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SpinnerListModel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicSliderUI;

public abstract class SliderItem extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SlideSpinner control;
	
	private Object[] values;
	
	private JLabel jLab;
	
	protected GridBagConstraints gbc = new GridBagConstraints();

	public SliderItem(String title, Object[] values, int defaultIndex) {

		this.values = values;
		final SliderItem parentRef = this;
		
		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 75));
	
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 20, 5, 10);

		jLab = new JLabel(title);
		jLab.setFont(jLab.getFont()
				.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD)));
		jLab.setOpaque(false);

		gbc.weightx = 0.4;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(jLab, gbc);
		
		control = new SlideSpinner(values) {
			
			@Override
			public void valueChanged(Object o) {
				parentRef.valueChanged(o);
			}
		};
		control.setIndex(defaultIndex);
		
		gbc.weightx = 0.6;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		this.add(control.getSpinner(), gbc);
		
		gbc.insets = new Insets(0, 20, 0, 10);
		gbc.weighty = 0.5;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(control.getSlider(), gbc);

	}
	
	public void enable(){
		this.setBackground(new Color(240, 240, 240));
		jLab.setForeground(Color.BLACK);
		control.enable();
	}
	
	public void disable(){
		this.setBackground(new Color(230, 230, 230));
		jLab.setForeground(Color.GRAY);
		control.disable();
	}
	
	public void disableButStay(){
		this.setBackground(new Color(230, 230, 230));
		jLab.setForeground(Color.GRAY);
		control.disableButStay();
	}
	
	public Object[] getValues(){
		return this.values;
	}
	
	public Object getSelectedValue(){
		return control.getSelectedValue();
	}
	
	public void updateModel(Object[] values){	
		control.updateModel(values);
	}
	
	public abstract void valueChanged(Object o);
	

}
