/* PopulationPanel.java
 * Contains each solution panel in current population, along with a label of the index
 * and a border if current population.
 */

package p3.views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

import p3.*;

public class PopulationPanel {
	private JPanel mainPanel;
	
	public PopulationPanel(Population pop, int popIndex, boolean isCurrentPop, P3 p3) {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints popC = new GridBagConstraints();
		popC.insets = new Insets(8,8,8,8);
		popC.gridx = 0;
		popC.gridy = 0;
		mainPanel.add(new JLabel("<html><font size = +1>P<sub><b>"+popIndex+
				"</b></sub></html>"));
		
		ArrayList<Solution> popList = pop.getPopulation();
		for (int i=0; i<pop.size(); i++) {
			Solution sol = popList.get(i);
			
			SolutionPanel solPanel = new SolutionPanel(sol, p3);
			
			popC.gridx = i+1;
			mainPanel.add(solPanel.getPanel(), popC);
		}
		
		if (isCurrentPop) {
			Border blueBorder = BorderFactory.createLineBorder(Color.BLUE, 3);
			mainPanel.setBorder(blueBorder);
		}
		
		
		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				
				ArrayList<int[]> clusterList = pop.getClusterModel();
				StringBuilder s = new StringBuilder();
				//s.append("<html>Cluster Model List for P<sub>"+popIndex+"</sub>:\n");
				s.append("Cluster Model List for P"+popIndex+":\n");
				if (clusterList.size()>0) {
					for (int[] cluster: clusterList){
						s.append(Arrays.toString(cluster)+", ");
					}
					s.delete(s.length()-2, s.length());				
				} else {
					s.append("[]");
				}
				//s.append("</html>");
				
				//JOptionPane.showMessageDialog(mainPanel, s.toString());
				
				JTextArea clusterBox = new JTextArea(s.toString());
				clusterBox.setEditable(false);
				clusterBox.setOpaque(false);
				clusterBox.setColumns(40);
				clusterBox.setLineWrap(true);
				clusterBox.setWrapStyleWord(true);
				JScrollPane scrollPane = new JScrollPane(clusterBox);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());
				scrollPane.setPreferredSize(new Dimension(400,100));
				JOptionPane.showMessageDialog(null, scrollPane, "Cluster List", JOptionPane.INFORMATION_MESSAGE);
				
            }
		});
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
}