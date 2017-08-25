/* TreePanel.java
 * Draws the binary tree representing the unsorted cluster model for an individual
 * population. Unused clusters are greyed out.
 */

package p3.views;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

import javax.swing.*;

import p3.Cluster;

@SuppressWarnings("serial")
public class TreePanel extends JPanel {
	
	private Cluster tree;
	private OneClusterPanel clusterPanel;
	private TreePanel leftChildTree, rightChildTree;
	boolean isRoot;
	
	public TreePanel(Cluster tree, boolean isRoot, boolean sortClusters) {
		this.isRoot = isRoot;
		this.tree = tree;
		
		// Only set background if root node, to avoid drawing over node connections
		if(isRoot) {
			this.setBackground(Color.WHITE);
		} else {
			this.setOpaque(false);			
		}
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// Space clusters further appart if higher up tree to avoid drawing
		// connection lines over other clusters
		int insetPadding = 2*tree.getHeight();
		c.insets = new Insets(2*insetPadding,insetPadding,0,insetPadding);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		clusterPanel = new OneClusterPanel(tree, sortClusters);
		this.add(clusterPanel, c);
		
		// If not a leaf, recursively draw children
		if(!tree.isLeaf()) {
			c.anchor = GridBagConstraints.SOUTH;
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 1;
			leftChildTree = new TreePanel(tree.getLeftChild(), false, sortClusters);
			this.add(leftChildTree,c);
			
			c.gridx = 1;
			c.gridy = 1;
			rightChildTree = new TreePanel(tree.getRightChild(),false, sortClusters);
			this.add(rightChildTree,c);
		}
	}
	
	// Get centre point of cluster panel
	public Point getClusterPosition() {
		Point pos = clusterPanel.getLocation();
		
		Dimension size = clusterPanel.getSize();
		pos.setLocation(pos.x+size.width/2, pos.y+size.height/2);
		return pos;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    g2D.setRenderingHints(rh);
		
		// Draw connections from parent to child nodes
		if(!tree.isLeaf()) {
			Point nodePoint = this.getClusterPosition();
			
			Point leftPoint = leftChildTree.getLocation();
			Point leftOffset = leftChildTree.getClusterPosition();
			leftPoint.setLocation(leftPoint.x+leftOffset.x, leftPoint.y+leftOffset.y);
			
			Point rightPoint = rightChildTree.getLocation();
			Point rightOffset = rightChildTree.getClusterPosition();
			rightPoint.setLocation(rightPoint.x+rightOffset.x, rightPoint.y+rightOffset.y);
						
			g2D.setColor(Color.BLACK);
			// Draw a curved line to children to avoid overlapping other clusters
			QuadCurve2D qL = new QuadCurve2D.Float();
			int ctrlXL = (int)((2*leftPoint.x)/3+nodePoint.x/3);
			qL.setCurve(nodePoint.x, nodePoint.y, ctrlXL, nodePoint.y, leftPoint.x, leftPoint.y);
			g2D.draw(qL);
			//g2D.drawLine(nodePoint.x, nodePoint.y, leftPoint.x, leftPoint.y);
			
			QuadCurve2D qR = new QuadCurve2D.Float();
			int ctrlXR = (int)(nodePoint.x/3+(2*rightPoint.x)/3);
			qR.setCurve(nodePoint.x, nodePoint.y, ctrlXR, nodePoint.y, rightPoint.x, rightPoint.y);
			g2D.draw(qR);
			//g2D.drawLine(nodePoint.x, nodePoint.y, rightPoint.x, rightPoint.y);
		}
    	
    	revalidate();
   	}

}
