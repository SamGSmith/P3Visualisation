/* ClusterPanel
 * panel showing the cluster model, both in terms of as a binary tree 
 * and as the final list used for crossover.
 */

package p3.views;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import p3.Cluster;

public class ClusteringPanel implements ItemListener {
	private JPanel mainPanel = new JPanel();
	private JCheckBox sortedCheckBox = new JCheckBox("Sort Cluster Variables");
	private JLabel treeLabel = new JLabel("Cluster Model Tree", SwingConstants.CENTER);
	private JLabel cListLabel = new JLabel("Cluster Model List", SwingConstants.CENTER);
	private JPanel treePanelContainer = new JPanel(new BorderLayout());
	private JPanel cListPanelContainer = new JPanel(new BorderLayout());
	
	private Cluster tree;
	
	// Constructor, adds components to mainPanel and sets layout
	public ClusteringPanel() {
		cListPanelContainer.setMinimumSize(new Dimension(100, 50));
		
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(2,4,2,4);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.9;
		c.weighty = 0;		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		mainPanel.add(treeLabel, c);
		
		c.weightx = 0;
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
		sortedCheckBox.addItemListener(this);
		mainPanel.add(sortedCheckBox, c);
		
		
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 2;
		c.weightx = 0.9;
		c.weighty = 0.96;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(treePanelContainer, c);
		
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(cListLabel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 0.1;
		c.weighty = 0.01;
		c.gridx = 0;
		c.gridy = 3;
		mainPanel.add(cListPanelContainer, c);
		
	}
	
	public void displayClusterModel(Cluster tree, ArrayList<int[]> clusterList, int popIndex) {

		this.tree = tree;
		
		// Set Tree Panel
		treeLabel.setText("<html>P<sub>"+popIndex+"</sub> Cluster Model Tree</html>");
		drawTree();
		
		// Set Cluster List panel
		cListPanelContainer.removeAll();
		cListLabel.setText("<html>P<sub>"+popIndex+"</sub> Cluster Model List</html>");
		StringBuilder s = new StringBuilder();
		if (clusterList.size()>0) {
			for (int[] cluster: clusterList){
				s.append(Arrays.toString(cluster)+", ");
			}
			s.delete(s.length()-2, s.length());				
		}
		JPanel listPanel = new JPanel();
		listPanel.add(new JLabel(s.toString()));
		JScrollPane listScrollPane = new JScrollPane(listPanel);
		cListPanelContainer.add(listScrollPane);
		
	}
	
	private void drawTree() {
		treePanelContainer.removeAll();
		TreePanel treePanel = new TreePanel(tree, true, sortedCheckBox.isSelected());
		JScrollPane treeScrollPane = new JScrollPane(treePanel);
		treePanelContainer.add(treeScrollPane);
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		drawTree();
		mainPanel.revalidate();
	}
	
}
