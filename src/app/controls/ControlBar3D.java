package app.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;


import opengl.OpenGLRenderer;

public class ControlBar3D extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7269971765962308726L;
	
	GridBagConstraints gbc = new GridBagConstraints();
	
	public enum Dimension3D{
		X, Y, Z;
	}
	
	public enum Direction{
		UP, DOWN;
	}
	
	public ControlBar3D(OpenGLRenderer g){
		
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.GRAY);
		this.setPreferredSize(new Dimension(700, 60));
		this.setMinimumSize(new Dimension(700, 60));
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 1, 0, 1);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(700, 60));
		controlPanel.setLayout(new GridBagLayout());
		controlPanel.setBackground(Color.GRAY);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 0;

		JButton xUP = getControlButtonAction("X Up", Dimension3D.X, Direction.UP, g);
		controlPanel.add(xUP, gbc);
		
		JButton xDOWN = getControlButtonAction("X Down", Dimension3D.X, Direction.DOWN, g);
		gbc.gridx = 1;
		controlPanel.add(xDOWN, gbc);
		
		JButton yUP = getControlButtonAction("Y Up", Dimension3D.Y, Direction.UP, g);
		gbc.gridx = 2;
		controlPanel.add(yUP, gbc);
		
		JButton yDOWN = getControlButtonAction("Y Down", Dimension3D.Y, Direction.DOWN, g);
		gbc.gridx = 3;
		controlPanel.add(yDOWN, gbc);
		
		JButton zUP = getControlButtonAction("Z Up", Dimension3D.Z, Direction.UP, g);
		gbc.gridx = 4;
		controlPanel.add(zUP, gbc);
		
		JButton zDOWN = getControlButtonAction("Z Down", Dimension3D.Z, Direction.DOWN, g);
		gbc.gridx = 5;
		controlPanel.add(zDOWN, gbc);
		
		gbc.gridx = 6;
		JButton reset = getControlButton("RESET");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.resetView();
			}
		});
		controlPanel.add(reset, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(controlPanel, gbc);
		
	}
	
	public JButton getControlButton(String title){
		
		JButton jb = new JButton(title);
		jb.setPreferredSize(new Dimension(200, 50));
		jb.setMinimumSize(new Dimension(100, 50));
		
		jb.setBackground(new Color(220,220,220));
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setForeground(Color.DARK_GRAY);
		
		return jb;
	}

	public JButton getControlButtonAction(String title, Dimension3D dim, Direction dir, OpenGLRenderer g){
		
		JButton jb = new JButton(title);
		jb.setPreferredSize(new Dimension(200, 50));
		jb.setMinimumSize(new Dimension(100, 50));
		
		jb.setBackground(new Color(100,100,100));
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setForeground(Color.WHITE);
		
		float smallAngle = 2, bigAngle = 15;
		
		jb.addMouseListener(new MouseAdapter() {
			boolean mousePressed;
			boolean locked = false;
			int counter = 0;
	        public void mousePressed(MouseEvent me) {
	            if (me.getButton() == MouseEvent.BUTTON1){
	            	mousePressed = true;
	            	new Thread() {
		                public void run() {
		                    while (mousePressed) {
		                        try {
									Thread.sleep(10);
									counter++;
								} catch (InterruptedException e) {}
		                        if (counter > 30){
		                        	g.turnAngle(dim, dir, smallAngle);
		                        }
		                    }
		                }

		            }.start();
	            } else if (me.getButton() == MouseEvent.BUTTON3){
	            	if (locked) {
	            		locked = false;
	            		g.setIdleRotation(dim, dir, 0);
	            		jb.setBackground(new Color(100,100,100));
	            		jb.setForeground(Color.WHITE);
	            	} else {
	            		locked = true;
	            		jb.setBackground(Color.WHITE);
	            		jb.setForeground(Color.DARK_GRAY);
	            		g.setIdleRotation(dim, dir, smallAngle / 8);
	            	}
	            	
	            }
	            
	        }
	        public void mouseReleased(MouseEvent e) {
	            if (mousePressed && counter <= 30){
	            	g.turnAngle(dim, dir, bigAngle);
	            }
	            mousePressed = false;
	            counter = 0;
	        }
	    });
		
		return jb;
	}

}
