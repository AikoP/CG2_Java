package app.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import app.ui.menu.ComboItem;
import app.ui.menu.CoordinateSliderItem;
import app.ui.menu.ExecuteItem;
import app.ui.menu.MenuHeaderItem;
import app.ui.menu.SliderItem;
import app.ui.menu.ToggleChooserItem;
import app.ui.menu.ToggleItem;
import app.utils.OFFReader;
import math.Intervall;
import math.model3D.Object3D;
import math.points.Point3D;
import math.points.Vector;
import opengl.OpenGLRenderer;
import sds.structures.SphereSDSNode;
import sds.utils.SDSManager;

public class AppControl extends JPanel{

	private ToggleItem model_showFaces, model_showVertices, sds_showSDS, sds_hideEmptySheres, requests_benchmark,
			requests_visualize, sds_limitTreeDepth;

	private ComboItem model_selectModel;
	
	private CoordinateSliderItem requests_RequestRoot;
	
	private ExecuteItem sds_buildSDS;
	
	private GridBagConstraints gbc = new GridBagConstraints();

	private JPanel controlContainer;
	
	private OpenGLRenderer openGLRenderer;
	
	private SDSManager sdsManager;
	
	private Object3D renderObject;
	
	public SliderItem requestRadiusSlider;

	public SliderItem requestKSlider;

	private SliderItem sds_bucketSizeSlider;

	private SliderItem sds_treeDepth;
	
	private ToggleChooserItem requestChooser;
	
	private RequestType selectedRequestType;
	
	public enum RequestType{
		NEAREST_K, INRANGE;
	}

	public AppControl(OpenGLRenderer openGLWindow) {

		this.openGLRenderer = openGLWindow;
		
		this.setLayout(new BorderLayout());
		
		controlContainer = new JPanel();
		controlContainer.setLayout(new GridBagLayout());
		controlContainer.setBackground(Color.LIGHT_GRAY);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 1, 1, 1);

		sdsManager = new SDSManager() {
			@Override
			public void doneBuilding(List<SphereSDSNode> sdsNodes) {
				if (openGLWindow != null){
					openGLWindow.setSDS(sdsNodes);
				}
				requestChooser.toggle();
				requestChooser.toggle();
			}

			@Override
			public void doneGetInRange(List<Vector> inRange) {
				if (openGLWindow != null){
					openGLWindow.setInRange(inRange);
				}
			}

			@Override
			public void doneGetNearestK(List<Vector> nearestK) {
				if (openGLWindow != null){
					openGLWindow.setNearestK(nearestK);
				}
			}

		};
		
		initModelControl();

		initSDSControl();

		initRequestControl();
		
		setDefaults();

		this.add(controlContainer, BorderLayout.NORTH);

	}
	
	private void fireRequest(){
		
		if (selectedRequestType == null || openGLRenderer == null || sdsManager == null)
			return;
		
		if (selectedRequestType == RequestType.INRANGE){
			openGLRenderer.setRequestRadius((double) requestRadiusSlider.getSelectedValue());
			sdsManager.getInRange((double) requestRadiusSlider.getSelectedValue());
		} else if (selectedRequestType == RequestType.NEAREST_K){
			sdsManager.getNearestK((int) requestKSlider.getSelectedValue());
		}
	}
	
	public int getMinHeight(){
		return controlContainer.getSize().height;
	}

	private void setDefaults() {

		model_showFaces.setToggle(true);
		model_showVertices.setToggle(true);
		
		sds_showSDS.setToggle(false);
		sds_hideEmptySheres.setToggle(false);
		
		requestChooser.toggle();
		
		sds_limitTreeDepth.setToggle(false);
		
	}
	
	public synchronized void setRenderObject(Object3D renderObject) {

		this.renderObject = renderObject;
		
		if (openGLRenderer == null)
			return;
		
		int vertexCount = renderObject.getVertices().size();
		int faceCount = renderObject.getFaces().size();
				
		System.out.println("vertices: " + vertexCount);
    	System.out.println("faces: " + faceCount);
    	
    	openGLRenderer.setRenderObject(renderObject);
    	
    	openGLRenderer.setNearestK(null);
    	openGLRenderer.setInRange(null);
    	
    	Integer[] requestKVals = new Integer[vertexCount];
		for (int i = 0; i < vertexCount; i++){
			requestKVals[i] = i + 1;
		}
		requestKSlider.updateModel(requestKVals);
		
	}
	
	private void addControl(Component comp){	
		gbc.gridy++;
		controlContainer.add(comp, gbc);
	}

	private void initModelControl() {

		addControl(new MenuHeaderItem("MODEL"));

		model_showFaces = new ToggleItem("Show Faces") {
			@Override
			public void toggleOn() {
				if (openGLRenderer != null){
					openGLRenderer.setFacesRenderState(true);
				}
			}

			@Override
			public void toggleOff() {
				if (openGLRenderer != null){
					openGLRenderer.setFacesRenderState(false);
				}
			}
		};
		addControl(model_showFaces);

		model_showVertices = new ToggleItem("Show Vertices") {
			@Override
			public void toggleOn() {
				if (openGLRenderer != null){
					openGLRenderer.setVerticesRenderState(true);
				}
			}

			@Override
			public void toggleOff() {
				if (openGLRenderer != null){
					openGLRenderer.setVerticesRenderState(false);
				}
			}
		};
		addControl(model_showVertices);

		String[] models = new String[]{"bunny","cow","cube","cube2","dragon","eight","golfball","golfball2","sphere","teapot","torus"};
		
		model_selectModel = new ComboItem("Select Model", models) {
			
			@Override
			public void selectionMade(String selected) {
				System.out.println("selected: " + selected);
				requestKSlider.disable();
				requestRadiusSlider.disable();
				new Thread() {
	                public void run() {
	                	try {
							renderObject = OFFReader.getNormalizedObjectFromResource(selected);
							setRenderObject(renderObject);
						} catch (IOException e) {}
	                }
	            }.start();
			}
		};
		addControl(model_selectModel);

	}

	private void initSDSControl() {

		addControl(new MenuHeaderItem("SDS"));
		
		sds_showSDS = new ToggleItem("Show SDS") {
			@Override
			public void toggleOn() {
				if (openGLRenderer != null){
					openGLRenderer.setSDSRenderState(true);
				}
			}

			@Override
			public void toggleOff() {
				if (openGLRenderer != null){
					openGLRenderer.setSDSRenderState(false);
				}
			}
		};
		addControl(sds_showSDS);

		sds_hideEmptySheres = new ToggleItem("Hide Empty Spheres") {
			@Override
			public void toggleOn() {
				if (openGLRenderer != null){
					openGLRenderer.setEmptySpheresRenderState(true);
				}
			}

			@Override
			public void toggleOff() {
				if (openGLRenderer != null){
					openGLRenderer.setEmptySpheresRenderState(false);
				}
			}
		};
		addControl(sds_hideEmptySheres);

		Integer[] sphereCapVals = new Integer[149];
		for (int i = 0; i < 149; i++){
			sphereCapVals[i] = i + 2;
		}

		sds_bucketSizeSlider = new SliderItem("Bucket Size", sphereCapVals, 98) {
			@Override
			public void valueChanged(Object o) {}
		};
		addControl(sds_bucketSizeSlider);
		
		sds_limitTreeDepth = new ToggleItem("Limit Tree Depth") {
			@Override
			public void toggleOn() {
				sds_treeDepth.enable();
			}
			@Override
			public void toggleOff() {
				sds_treeDepth.disableButStay();
			}
		};
		addControl(sds_limitTreeDepth);
		
		Integer[] treeDepthVals = new Integer[21];
		for (int i = 0; i < 21; i++){
			treeDepthVals[i] = i;
		}
		sds_treeDepth = new SliderItem("Maximum Tree Depth", treeDepthVals, 0) {
			@Override
			public void valueChanged(Object o) {}
		};
		addControl(sds_treeDepth);
		
		sds_buildSDS = new ExecuteItem("BUILD") {
			@Override
			public void execute() {
				if (renderObject != null){
					int maxDepth = sds_limitTreeDepth.isToggled() ? (int) sds_treeDepth.getSelectedValue() : -1;
					sdsManager.build(renderObject.getVertices(), (int) sds_bucketSizeSlider.getSelectedValue(), maxDepth);
				}
			}
		};
		addControl(sds_buildSDS);

	}

	private void initRequestControl() {

		addControl(new MenuHeaderItem("REQUESTS"));

		requestChooser = new ToggleChooserItem("Request Type", "getNearestK", "getInRange") {	
			@Override
			public void toggled(String option) {
				if (option.equals("getNearestK")){
					requestKSlider.enable();
					requestRadiusSlider.disable();
					selectedRequestType = RequestType.NEAREST_K;
				} else if (option.equals("getInRange")){
					if (openGLRenderer != null){
						openGLRenderer.setNearestK(null);
					}
					requestKSlider.disable();
					requestRadiusSlider.enable();
					selectedRequestType = RequestType.INRANGE;
				}
			}
		};
		addControl(requestChooser);
		
		requests_RequestRoot = new CoordinateSliderItem("Request Point", new Intervall(-1.5,1.5)) {
			@Override
			public void valueChanged(double x, double y, double z) {
				sdsManager.setRequestRoot(new Point3D(x, y, z));
				openGLRenderer.setRequestRoot(x, y, z);
				fireRequest();
			}
		};
		addControl(requests_RequestRoot);
        
		Integer[] requestKVals = new Integer[1000];
		for (int i = 0; i < 1000; i++){
			requestKVals[i] = i + 1;
		}
		requestKSlider = new SliderItem("getNearestK", requestKVals, 0) {
			@Override
			public void valueChanged(Object o) {
				fireRequest();
			}
		};
		addControl(requestKSlider);
        
		Double[] requestRadiusVals = new Double[201];
		for (int i = 0; i < 201; i++){
			requestRadiusVals[i] = Math.round((i * 0.01) * 100) / 100.0;
		}
		requestRadiusSlider = new SliderItem("getInRadius", requestRadiusVals, 0) {
			@Override
			public void valueChanged(Object o) {
				fireRequest();
			}
		};
		addControl(requestRadiusSlider);

	}

}
