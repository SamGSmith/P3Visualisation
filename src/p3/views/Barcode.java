/* Barcode.java
 * extends JPanel, when given a binary string creates a barcode representation of that
 * string with lines, white representing 1, black 0.
 */

package p3.views;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Barcode extends JPanel {
	
	private String bits;
	
	public Barcode(String bits) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.bits = bits;
	}
	
	public void drawBarcode(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw a 2 pixel wide line for each bit in solution
		for (int i=0; i<bits.length(); i++) {
			Color lineColor = (bits.charAt(i)=='0') ? Color.BLACK : Color.WHITE; 
			g2d.setColor(lineColor);
			g2d.setStroke(new BasicStroke(2f));
			g2d.drawLine(2+i*2, 2, 2+i*2, 32);
		}
		
		//Manually Draw a border
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(1f));
		g2d.drawRect(0, 0, 1+2*bits.length(), 33);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBarcode(g);
	}
	
	@Override
	public Dimension getPreferredSize() {
        return new Dimension(2+bits.length()*2, 34);
    }
	
}