/* OneClusterPanel.java
 * Panel used within tree to represent a single cluster. Shows bit positions represented
 * by the cluster. Will be greyed out if dead, and outlined in blue otherwise.
 */

package p3.views;

import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

import p3.Cluster;

@SuppressWarnings("serial")
public class OneClusterPanel extends JPanel {

	private String clusterString;
	private boolean isAlive;
	private JLabel stringLabel;
	
	public OneClusterPanel(Cluster cluster, boolean sortCluster) {
		
		//Get cluster value, and if sortCluster, sort a copy of cluster value
		int[] clusterVal = cluster.getClusterValue();
		int[] cValCopy = new int[clusterVal.length];
		System.arraycopy(clusterVal, 0, cValCopy, 0, clusterVal.length);
		if (sortCluster) {
			Arrays.sort(cValCopy);
		}
		
		clusterString = arrToString(cValCopy);
		stringLabel = new JLabel(clusterString, SwingConstants.CENTER);
		isAlive = cluster.isAlive();
				
		if(isAlive){			
			stringLabel.setForeground(Color.BLACK);
		} else {			
			stringLabel.setForeground(Color.LIGHT_GRAY);
		}
		this.add(stringLabel);
	}
	
	private String arrToString(int[] clusterVal) {
		StringBuilder s = new StringBuilder();
		for(int i: clusterVal) {
			s.append(i);
			s.append(',');
		}
		s.delete(s.length()-1, s.length());
		return s.toString();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		Dimension thisSize = this.getSize();
		
		if(isAlive){			
			g2D.setColor(Color.BLUE);
		} else {			
			g2D.setColor(Color.LIGHT_GRAY);
		}
		g2D.drawRoundRect(0, 0, thisSize.width-2, thisSize.height-2, 6, 6);
		
	}

	@Override
	public Dimension getPreferredSize() {
		int xPref = 8+7*clusterString.length();
		int yPref = 26;
		return new Dimension(xPref, yPref);
	}
}
