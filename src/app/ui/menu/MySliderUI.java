package app.ui.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JSlider;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSliderUI;

public class MySliderUI extends BasicSliderUI {

	private final float[] fracs = { 0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f };
	private LinearGradientPaint p;

	public MySliderUI(JSlider slider) {
		super(slider);
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put("Slider.thumbHeight", 50); // change height
		defaults.put("Slider.thumbWidth", 50); // change width
	}

	@Override
	public void paintTrack(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle t = trackRect;
		Point2D start = new Point2D.Float(t.x, t.y);
		Point2D end = new Point2D.Float(t.width, t.height);
		Color[] colors = { Color.magenta, Color.blue, Color.cyan, Color.green, Color.yellow, Color.red };
		p = new LinearGradientPaint(start, end, fracs, colors);
		g2d.setPaint(p);
		g2d.setColor(Color.GRAY);
		g2d.drawLine(t.x, t.y + t.height / 2 ,t.x + t.width, t.y + t.height / 2);
	}

	@Override
	public void paintThumb(Graphics g) {
//		super.paintThumb(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle t = thumbRect;
		g2d.setColor(Color.GRAY);
		int tw2 = t.width / 2;
		int th2 = t.height / 4;
		g2d.fillOval(t.x, t.y + th2, t.width, t.width);
	}
}