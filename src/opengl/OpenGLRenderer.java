package opengl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.Threading;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import app.controls.ControlBar3D.Dimension3D;
import app.controls.ControlBar3D.Direction;
import math.Sphere;
import math.model3D.Face3D;
import math.model3D.Object3D;
import math.points.Point3D;
import math.points.Vector;
import sds.structures.SphereSDSNode;

public class OpenGLRenderer implements GLEventListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLU glu = new GLU();
	private GLUT glut = new GLUT();
	
	private float distance = -3f, realDistance = -3f;
	
	private float xAngle = 0f, yAngle = 0f, zAngle = 0f;
	private float visibleXAngle = 0, visibleYAngle = 0, visibleZAngle = 0;
	private float idleXRotation = 0, idleYRotation = 0, idleZRotation = 0;
	
	public List<SphereSDSNode> sphereList;
	private Object3D renderObject;
	
	private Point3D requestRoot;
	
	private Sphere requestRadiusSphere;
	private List<Vector> nearestK, inRange;
	
	private volatile boolean renderSDS, renderFaces, renderVertices, hideEmptySpheres, visualizeRequests;

	public OpenGLRenderer() {
		requestRoot = new Point3D(0,0,0);
		requestRadiusSphere = new Sphere(requestRoot, 0.0);
	}

	@Override
	public void display(GLAutoDrawable drawable) {

		final GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity(); // Reset The View

		realDistance += (distance - realDistance) * 0.1f;
		
		// rotating
		gl.glTranslatef(0, 0, realDistance);
		
		xAngle += idleXRotation;
		yAngle += idleYRotation;
		zAngle += idleZRotation;

		visibleXAngle += Math.min((360 - visibleXAngle) + xAngle, (xAngle - visibleXAngle)) * 0.1f;
		visibleYAngle += Math.min((360 - visibleYAngle) + yAngle, (yAngle - visibleYAngle)) * 0.1f;
		visibleZAngle += Math.min((360 - visibleZAngle) + zAngle, (zAngle - visibleZAngle)) * 0.1f;

		gl.glRotatef(visibleXAngle, 1, 0, 0);
		gl.glRotatef(visibleYAngle, 0, 1, 0);
		gl.glRotatef(visibleZAngle, 0, 0, 1);
		
		if (renderObject == null) return;
		
		// draw requestRoot
		gl.glColor4f(1f, 1.0f, 1.0f, 1f);
		gl.glTranslatef((float) requestRoot.x, (float) requestRoot.y, (float) requestRoot.z);
		glut.glutSolidCube(0.005f);
		gl.glTranslatef((float) -requestRoot.x, (float) -requestRoot.y, (float) -requestRoot.z);
		
		gl.glColor4f(1f, 0.0f, 0.0f, 0.3f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f((float) requestRoot.x + 10, (float) requestRoot.y, (float) requestRoot.z);
			gl.glVertex3f((float) requestRoot.x - 10, (float) requestRoot.y, (float) requestRoot.z);
		gl.glEnd();
		
		gl.glColor4f(0f, 1.0f, 0.0f, 0.3f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f((float) requestRoot.x, (float) requestRoot.y + 10, (float) requestRoot.z);
			gl.glVertex3f((float) requestRoot.x , (float) requestRoot.y - 10, (float) requestRoot.z);
		gl.glEnd();
		
		gl.glColor4f(0f, 0.0f, 1.0f, 0.3f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f((float) requestRoot.x, (float) requestRoot.y, (float) requestRoot.z + 10);
			gl.glVertex3f((float) requestRoot.x, (float) requestRoot.y, (float) requestRoot.z - 10);
		gl.glEnd();
		
		// draw requestSphere
		gl.glTranslatef((float) requestRadiusSphere.center.get(0), (float) requestRadiusSphere.center.get(1), (float) requestRadiusSphere.center.get(2));
		gl.glColor4f(0f, 0.0f, 1.0f, 0.05f);
		glut.glutSolidSphere(requestRadiusSphere.radius, 20, 20);
		gl.glColor4f(0f, 0.0f, 1.0f, 0.2f);
		glut.glutWireSphere(requestRadiusSphere.radius, 20, 20);
		gl.glTranslatef((float) -requestRadiusSphere.center.get(0), (float) -requestRadiusSphere.center.get(1), (float) -requestRadiusSphere.center.get(2));
		

		// drawing kNearest
		if (nearestK != null) {
			gl.glColor3f(0.0f, 1f, 0f);
			for (Vector v : nearestK) {
				gl.glTranslatef((float) v.get(0), (float) v.get(1), (float) v.get(2));
				glut.glutSolidCube(0.01f);
				gl.glTranslatef((float) -v.get(0), (float) -v.get(1), (float) -v.get(2));
			}
		}
		
		// drawing inRange
		if (inRange != null) {
			gl.glColor3f(0.0f, 0f, 1f);
			for (Vector v : inRange) {
				gl.glTranslatef((float) v.get(0), (float) v.get(1), (float) v.get(2));
				glut.glutSolidCube(0.01f);
				gl.glTranslatef((float) -v.get(0), (float) -v.get(1), (float) -v.get(2));
			}
		}
		
		
		//drawing SDSSpheres
		if (sphereList != null && renderSDS){
			for (SphereSDSNode s : sphereList) {
				// gl.glColor3d(red, red, red);
				// red = 0;
				
				if (hideEmptySpheres && !s.isLeaf()) continue;
				
				gl.glTranslatef((float) s.center.get(0), (float) s.center.get(1), (float) s.center.get(2));
				gl.glColor4f(0.96f, 0.75f, 0.26f, 0.05f);
				glut.glutSolidSphere(s.radius, 20, 20);
				gl.glColor4f(0.96f, 0.75f, 0.26f, 0.2f);
				glut.glutWireSphere(s.radius, 20, 20);
				gl.glTranslatef((float) -s.center.get(0), (float) -s.center.get(1), (float) -s.center.get(2));
			}
		}
		

		// drawing object vertices
		if (renderVertices){
			gl.glColor3f(0.34f, 0.89f, 0.98f);
			gl.glBegin(GL.GL_POINTS);
			for (Vector v : renderObject.getVertices()) {
				gl.glVertex3f((float) v.get(0), (float) v.get(1), (float) v.get(2));
			}
			gl.glEnd();
		}
		
		
		// drawing object faces
//		gl.glTranslatef(-0.5f, -0.5f, -0.5f);
		if (renderFaces){
			for (Face3D f : renderObject.getFaces()) {

				Point3D normal = f.p1.minus(f.p2).crossProduct(f.p3.minus(f.p2));
				gl.glNormal3d(normal.x, normal.y, normal.z);

				// surface
				gl.glColor4f(0.34f, 0.89f, 0.98f, 0.1f);
				gl.glBegin(GL.GL_TRIANGLES);	
				gl.glVertex3f((float) f.p1.get(0), (float) f.p1.get(1), (float) f.p1.get(2));
				gl.glVertex3f((float) f.p2.get(0), (float) f.p2.get(1), (float) f.p2.get(2));
				gl.glVertex3f((float) f.p3.get(0), (float) f.p3.get(1), (float) f.p3.get(2));
				gl.glEnd();

				// outline
				gl.glColor4f(0.34f, 0.89f, 0.98f, 0.1f);
				gl.glBegin(GL.GL_LINE_LOOP);
				gl.glVertex3f((float) f.p1.get(0), (float) f.p1.get(1), (float) f.p1.get(2));
				gl.glVertex3f((float) f.p2.get(0), (float) f.p2.get(1), (float) f.p2.get(2));
				gl.glVertex3f((float) f.p3.get(0), (float) f.p3.get(1), (float) f.p3.get(2));
				gl.glEnd();

			}
		}
		
		
		gl.glFlush();

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {}

	@Override
	public void init(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearDepth(1.0f);
//		gl.glEnable(GL2.GL_DEPTH_TEST);
//		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		
		gl.glEnable (GL.GL_BLEND);
		gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		GL2 gl = drawable.getGL().getGL2();

		if (height <= 0)
			height = 1;

		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(45.0f, h, 0.01, 8.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	public synchronized void setRenderObject(Object3D renderObject) {

		this.renderObject = null;
		this.setSDS(null);

		this.resetView();

		this.renderObject = renderObject;

	}
	
	public synchronized void setSDS(List<SphereSDSNode> sdsNodes) {

		boolean oldSDSState = this.renderSDS;
		
		this.setSDSRenderState(false);
		
		this.sphereList = sdsNodes;
		
		this.setSDSRenderState(oldSDSState);		

	}
	
	
	public synchronized void turnXAngle(float degrees){
		xAngle += degrees;
	}
	public synchronized void turnXAngleInstant(float degrees){
		xAngle += degrees;
		visibleXAngle += degrees;
	}
	
	public synchronized void turnYAngle(float degrees){
		yAngle += degrees;
	}
	public synchronized void turnYAngleInstant(float degrees){
		yAngle += degrees;
		visibleYAngle += degrees;
	}
	
	public synchronized void turnZAngle(float degrees){
		zAngle += degrees;
	}
	public synchronized void turnZAngleInstant(float degrees){
		zAngle += degrees;
		visibleZAngle += degrees;
	}
	
	public synchronized void addDistance(float dist){
		distance = Math.min(0.5f, Math.max(-4f, distance + dist));
	}
	
	public synchronized void turnAngle(Dimension3D dim, Direction dir, float angle) {

		if (dir == Direction.DOWN){
			angle *= -1;
		}
		
		switch (dim) {
			case X:	turnXAngle(angle); 	break;
			case Y:	turnYAngle(angle); 	break;
			case Z:	turnZAngle(angle); 	break;
			default:					break;
		}
		
	}
	
	public synchronized void setIdleRotation(Dimension3D dim, Direction dir,float angle){
		
		if (dir == Direction.DOWN){
			angle *= -1;
		}
		
		switch (dim) {
			case X:	idleXRotation = angle; 	break;
			case Y:	idleYRotation = angle; 	break;
			case Z:	idleZRotation = angle; 	break;
			default:					break;
		}
		
	}
	
	public void resetAngles(){
		xAngle = 0;
		yAngle = 0;
		zAngle = 0;
	}
	
	public void resetView(){
		this.resetAngles();
		distance = -3f;
	}
	
	public synchronized void setSDSRenderState(boolean b){
		this.renderSDS = b;
	}
	
	public synchronized void setFacesRenderState(boolean b){
		this.renderFaces = b;
	}
	
	public synchronized void setVerticesRenderState(boolean b){
		this.renderVertices = b;
	}
	
	public synchronized void setEmptySpheresRenderState(boolean b){
		this.hideEmptySpheres = b;
	}
	
	
	public synchronized void setRequestRadius(double radius){
		this.requestRadiusSphere.radius = radius;
	}
	
	public synchronized void setNearestK(List<Vector> nearestK){
		this.nearestK = nearestK;
	}
	
	public synchronized void setInRange(List<Vector> inRange){
		this.inRange = inRange;
	}
	
	public void setRequestRoot(double x, double y, double z){
		this.requestRoot.x = x;
		this.requestRoot.y = y;
		this.requestRoot.z = z;
	}
	
	

}
