package app.ui.menu;

import java.awt.Color;

import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.ui.menu.MySliderUI;

public abstract class SlideSpinner {
	
	private JSpinner spinner;
	private JSlider slider;
	
	private Object[] values;
	
	public SlideSpinner(Object[] values){
		
		this.values = values;
		final SlideSpinner parentRef = this;
		
		SpinnerListModel numModel = new SpinnerListModel(values);
		spinner = new JSpinner(numModel);
		((DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
		((DefaultEditor) spinner.getEditor()).getTextField().setBackground(Color.WHITE);
		
		slider = new JSlider(1, values.length);
		slider.setSnapToTicks(true);
		slider.setUI(new MySliderUI(slider));
		slider.setFocusable(false);
		slider.setOpaque(false);
		
		
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				valueChanged(spinner.getValue());

				int index = 0;
				for (Object o : parentRef.getValues()) {
					if (o == spinner.getValue())
						break;
					index++;
				}
				
				slider.setValue(index + 1);

			}
		});
		
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spinner.setValue(parentRef.getValues()[slider.getValue() - 1]);
			}
		});
		
	}
	
	public void enable(){
		slider.setEnabled(true);
		spinner.setEnabled(true);
	}
	
	public void disable(){
		slider.setValue(1);
		slider.setEnabled(false);
		spinner.setEnabled(false);
	}
	
	public void disableButStay(){
		slider.setEnabled(false);
		spinner.setEnabled(false);
	}
	
	public void setIndex(int i){
		if (i < values.length){
			slider.setValue(i + 1);
			spinner.setValue(values[i]);
		}
	}
	
	public JSlider getSlider(){
		return slider;
	}
	
	public JSpinner getSpinner(){
		return spinner;
	}
	
	public Object[] getValues(){
		return this.values;
	}
	
	public Object getSelectedValue(){
		return spinner.getValue();
	}
	
	public void updateModel(Object[] values){
		
		this.values = values;
		
		slider.setMaximum(values.length);
		
		SpinnerListModel model = new SpinnerListModel(values);
		spinner.setModel(model);
		
		slider.setValue(1);
		
	}
	
	public abstract void valueChanged(Object o);
	

}
