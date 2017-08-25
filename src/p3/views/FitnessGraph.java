/* FitnessGraph.java
 * Given a series of fitness values and a fitness function, will plot a scatter 
 * plot of values, with a line tracing the max fitness at each point
 */

package p3.views;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import p3.fitness.*;

@SuppressWarnings("serial")
public class FitnessGraph extends JPanel {

	private ArrayList<Double> fitnessList;
	private FitnessFn ff;
	private double optimalFitness;
	
	public FitnessGraph(ArrayList<Double> fitnessList, FitnessFn ff) {
		this.fitnessList = fitnessList;
		this.ff = ff;
		optimalFitness = ff.getOptimalFitness();		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		drawGraph(g2D);
	}
	
	public void drawGraph(Graphics2D g2D) {
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    g2D.setRenderingHints(rh);
	    
	    Dimension graphSize = this.getSize();
		//sizeLabel.setText(graphSize.toString());
	    
		int axisGap = 30;
		int nudgeDown = 7;
		// Define xScale and yScale, which determine space between points
		double xScale = 0;
		int fListSize = fitnessList.size();
		if(fListSize>0) {
			xScale = (graphSize.width - axisGap)/(double)(fListSize);
		}
		
		double yScale = (graphSize.height - axisGap)/optimalFitness;
		
		//Draw white background only on inside of graph area (not on axes)
		g2D.setColor(Color.WHITE);
		int bgWidth = graphSize.width-axisGap;
		int bgHeight = graphSize.height-axisGap;
	    g2D.fillRect(axisGap, nudgeDown, bgWidth, bgHeight);
		
	    //Determine coordinates of each fitness value
		ArrayList<Point> pointList = new ArrayList<Point>();
		for(int i=0; i<fitnessList.size(); i++) {
			int xPos = (int)(i*xScale+axisGap);
			int yPos = (int)((optimalFitness-fitnessList.get(i))*yScale);
			pointList.add(new Point(xPos, yPos+nudgeDown));
		}
		
		//Draw axes
		g2D.setColor(Color.BLACK);
		int zeroYCoord = graphSize.height-axisGap+nudgeDown;
		g2D.drawLine(axisGap, zeroYCoord, graphSize.width, zeroYCoord);
		g2D.drawLine(axisGap, zeroYCoord, axisGap, nudgeDown);
		
		//Draw max and min values on scales
		int zeroYPos = graphSize.height-axisGap+nudgeDown;
		//Iterations
		g2D.drawString("0", axisGap-3, zeroYPos+12);
		if(fitnessList.size()>1) {
			String iterCount = Integer.toString(fitnessList.size()-1);
			g2D.drawString(iterCount, graphSize.width-20, zeroYPos+12);			
		}
		//Fitness
		g2D.drawString(Double.toString(ff.getMinimumFitness()), 2, zeroYPos);
		g2D.drawString(Double.toString(optimalFitness), 2, nudgeDown+5);
		
		
		//Draw max fitness line
		g2D.setColor(Color.PINK);
		g2D.setStroke(new BasicStroke(2f));
		if(fitnessList.size()>0) {
			Point maxPoint = pointList.get(0);
			g2D.drawLine(axisGap, maxPoint.y, (int) (axisGap+xScale), maxPoint.y);
			for(int i=1; i<pointList.size(); i++) {
				int xCoord = (int)(axisGap+xScale*i)+1;
				
				//Draw vertical line if fitness is greater
				if(pointList.get(i).y < maxPoint.y) {
					g2D.drawLine(xCoord, maxPoint.y, xCoord, pointList.get(i).y);
					maxPoint = pointList.get(i);
				}

				//Draw horizontal line
				g2D.drawLine(xCoord, maxPoint.y, (int) (xCoord+xScale), maxPoint.y);
				
			}
		}
		
		//Draw points
		g2D.setColor(Color.BLUE);
		for(Point p : pointList) {
			g2D.fillOval(p.x-1, p.y-2, 4, 4);
		}
		
	}
	
}
