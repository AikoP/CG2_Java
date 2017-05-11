package opengl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.Threading;
import com.jogamp.opengl.util.FPSAnimator;

public class OpenGLWrapWindow extends JPanel implements MouseListener {

	private Point mousePosition;
	
	private OpenGLRenderer openGLRenderer;
	
	public OpenGLWrapWindow(OpenGLRenderer openGLRenderer) {
		
		this.openGLRenderer = openGLRenderer;
		this.setBackground(Color.BLACK);

        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        GLWindow window = GLWindow.create(caps);
        
        final FPSAnimator animator = new FPSAnimator(window, 60, true);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent arg0) {
                // Use a dedicate thread to run the stop() to ensure that the
                // animator stops before program exits.
                new Thread() {
                    @Override
                    public void run() {
                        if (animator.isStarted())
                            animator.stop();    // stop the animator loop
                        System.exit(0);
                    }
                }.start();
            }
        });
        
        window.addMouseListener(this);
        
        window.setSize(200, 200);
        
        mousePosition = MouseInfo.getPointerInfo().getLocation();
        
        animator.start();  // start the animator loop

        window.addGLEventListener(openGLRenderer);
        
        NewtCanvasAWT newtCanvasAWT = new NewtCanvasAWT(window);
        
        Container container1 = new Container();
        container1.setLayout(new BorderLayout());
        container1.add(newtCanvasAWT, BorderLayout.CENTER);
        
        System.out.println(Threading.isOpenGLThread());
        
        this.setLayout(new BorderLayout());
        this.add(container1, BorderLayout.CENTER);
        this.setOpaque(false);

	}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseDragged(MouseEvent me) {

			int xTranslate = mousePosition.x - me.getX();
			int yTranslate = mousePosition.y - me.getY();
			
			mousePosition.x = me.getX();
			mousePosition.y = me.getY();

			openGLRenderer.turnXAngleInstant(yTranslate * 0.2f);
			openGLRenderer.turnYAngleInstant(xTranslate * 0.2f);
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mousePressed(MouseEvent me) {
			mousePosition.x = me.getX();
			mousePosition.y = me.getY();
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseWheelMoved(MouseEvent me) {
			
			float[] notches = me.getRotation();
			openGLRenderer.addDistance(notches[0] * 0.2f + notches[1] * 0.2f);
			
		}
		
		
		


}
