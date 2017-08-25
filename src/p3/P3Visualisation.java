/* P3Visualisation.java
 * Run to create a visualisation of the P3 algorithm
 */

package p3;

import p3.views.P3GUI;

public class P3Visualisation {
	
	public static void main(String[] args) {
		P3GUI gui = new P3GUI();
		P3 p3 = new P3();
		P3Controller controller = new P3Controller(gui, p3);	
		
		if(controller!=null) {
			System.out.println("P3 visualisation started.");
		}
	}
}