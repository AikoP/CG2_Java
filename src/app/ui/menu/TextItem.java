package app.ui.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.util.Collections;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class TextItem extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected GridBagConstraints gbc = new GridBagConstraints();

	public TextItem(String title) {

		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(250, 45));

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 0.4;
		gbc.weighty = 1;
		gbc.insets = new Insets(5, 20, 5, 10);

		JLabel jLab = new JLabel(title);
		jLab.setFont(jLab.getFont().deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD)));

		gbc.gridx = 0;
		gbc.gridy = 0;
//		gbc.anchor = GridBagConstraints.WEST;
		this.add(jLab, gbc);
		
		
		
		JTextField jTp = new JTextField();
		jTp.setText("100");
		PlainDocument doc = (PlainDocument) jTp.getDocument();
		doc.setDocumentFilter(new MyIntFilter());
//		textWrap.add(jTp, BorderLayout.CENTER);
		
//		JScrollPane textWrap = new JScrollPane(jTp);
//		textWrap.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//		textWrap.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		gbc.weightx = 0.6;
		gbc.gridx = 1;
		gbc.gridy = 0;
//		gbc.anchor = GridBagConstraints.EAST;
		this.add(jTp, gbc);

	}
	
	class MyIntFilter extends DocumentFilter {
		@Override
		   public void insertString(FilterBypass fb, int offset, String string,
		         AttributeSet attr) throws BadLocationException {

		      Document doc = fb.getDocument();
		      StringBuilder sb = new StringBuilder();
		      sb.append(doc.getText(0, doc.getLength()));
		      sb.insert(offset, string);

		      if (test(sb.toString())) {
		         super.insertString(fb, offset, string, attr);
		      } else {
		         // warn the user and don't allow the insert
		      }
		   }

		   private boolean test(String text) {
		      try {
		         Integer.parseInt(text);
		         return true;
		      } catch (NumberFormatException e) {
		         return false;
		      }
		   }
		   
		   @Override
		   public void replace(FilterBypass fb, int offset, int length, String text,
		         AttributeSet attrs) throws BadLocationException {

		      Document doc = fb.getDocument();
		      StringBuilder sb = new StringBuilder();
		      sb.append(doc.getText(0, doc.getLength()));
		      sb.replace(offset, offset + length, text);

		      if (test(sb.toString())) {
		         super.replace(fb, offset, length, text, attrs);
		      } else {
		         // warn the user and don't allow the insert
		      }

		   }

		   @Override
		   public void remove(FilterBypass fb, int offset, int length)
		         throws BadLocationException {
		      Document doc = fb.getDocument();
		      StringBuilder sb = new StringBuilder();
		      sb.append(doc.getText(0, doc.getLength()));
		      sb.delete(offset, offset + length);

		      if (test(sb.toString())) {
		         super.remove(fb, offset, length);
		      } else {
		         // warn the user and don't allow the insert
		      }

		   }
		   
	}
		   

}
