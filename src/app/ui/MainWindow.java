package app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.jogamp.opengl.Threading;

import app.controls.AppControl;
import app.controls.ControlBar3D;
import app.utils.OFFReader;
import opengl.OpenGLRenderer;
import opengl.OpenGLWrapWindow;

public class MainWindow extends JFrame implements DropTargetListener {
	
	GridBagConstraints gbc = new GridBagConstraints();
	
	private AppControl appControls;
	
	public MainWindow(){
		
		super("CG2 SDS");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new GridBagLayout());
		
		DropTarget dt = new DropTarget(this, this);
		
		OpenGLRenderer openGLRenderer = new OpenGLRenderer();
		OpenGLWrapWindow openGLWrapWindow = new OpenGLWrapWindow(openGLRenderer);
        
        ControlBar3D control3D = new ControlBar3D(openGLRenderer);        
		appControls = new AppControl(openGLRenderer);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
        this.getContentPane().add(openGLWrapWindow, gbc);
        
        gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
        this.getContentPane().add(control3D, gbc);
        
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.anchor = GridBagConstraints.EAST;
        this.getContentPane().add(appControls, gbc);
        
        this.setSize(1500, 900);
        this.setMinimumSize(new Dimension(1000, 1058));
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
		
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	System.out.println(Threading.isOpenGLThread());
		        new MainWindow();
		    }

		});

	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent evt) {
		try {
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            List<File> droppedFiles = (List<File>)
                evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : droppedFiles) {
            	
            	String extension = "";
            	int i = file.getName().lastIndexOf('.');
            	if (i > 0) {
            	    extension = file.getName().substring(i+1);
            	}
            	
                if (extension.equals("off")){
                	System.out.println("dropped : " + file.getAbsolutePath());
                	appControls.requestKSlider.disable();
                	appControls.requestRadiusSlider.disable();
    				new Thread() {
    	                public void run() {
    	                	try {
    	                		appControls.setRenderObject(OFFReader.getNormalizedObjectFromExt(file.getAbsolutePath()));
    						} catch (IOException e) {
    							System.out.println("failed to load model from file..");
    						}
    	                }
    	            }.start();
                }
                
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
