package strategy;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import plan.path.Finder;
import plan.path.pathfinding.Path;

public class Output {
	JFrame frame = new JFrame("Path Output");
	JPanel panel = new JPanel();
	// JLabel pathLabel = new JLabel("Blank path", JLabel.HORIZONTAL);
	JTextArea pathLabel = new JTextArea("");
	long lastTime = 0;
	long nowTime = 0;
	int counter = 0;
	
	public Output(){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setResizable(false);
		Dimension windowSize = new Dimension(900, 545);
		frame.setMinimumSize(windowSize);
		frame.setPreferredSize(windowSize);
		pathLabel.setLineWrap(false);
		panel.add(pathLabel);
		pathLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		frame.add(panel);
		frame.pack();
        frame.setVisible(true);
        lastTime = System.currentTimeMillis();
	}

	/**
	 * Terminal Printout of the full path from the object to the goal.
	 * It uses a 64x34 matrix for easy display. 
	 * 
	 * @param path Path that needs to be drawn.
	 */
	public boolean fullPath(Path path, Finder finder, Point2D.Float ballposition, Point2D.Float [] wayPoints){
		nowTime = System.currentTimeMillis();
		counter++;
		if (nowTime - lastTime > 1000){
			System.err.println(">> Plans per second ="+counter);
			lastTime = System.currentTimeMillis();
			counter = 0;
		}
		
		if (path == null){
			return true;
		}
		System.out.println("path: -------------------------------");
		
		char[][] matrix = new char[64][34];
		for (int x = 0; x < 64; x++){
			for(int y = 0; y < 34; y++){
				if(finder.map.getTerrain(x,y) == 1){
					matrix[(int) x][(int) y] = '#';
				}
				if(finder.map.getTerrain(x,y) == 3){
						matrix[(int) x][(int) y] = '+';
				}
				
				if ((finder.map.getTerrain(x,y) != 3) && (finder.map.getTerrain(x,y) != 1)){
						matrix[(int) x][(int) y] = '.';
				}
				if (finder.map.getTerrain(x,y) == 4){
						matrix[(int) x][(int) y] = '%';
				}
			}
		}
		
		//System.out.println("it dies here");
	
		for (int i = 0;  i<path.getLenght(); i++){

		//	System.out.println("Mor acilea la : " + path.getStep(i).getX() + " | " + path.getStep(i).getY() + " | " + i);
			matrix[(int)(path.getStep(i).getX())][(int)(path.getStep(i).getY())] = '*';
		}
		
		//System.out.println("or does it die here?");		

		/* Adding the waypoints on the path planning:  */

		for (int s = 0; s<wayPoints.length; s++){
			matrix[(int) (wayPoints[s].x/10)][(int) (wayPoints[s].y/10)] = '|';
		}

		matrix[(int) ballposition.x/10][(int) ballposition.y/10] = '+';
		String string = "";
		for (int i=0;i<34;i++) {
			for (int j=0;j<64;j++) {
				string = string+" "+ matrix[j][i];
			}
			string = string +"\n";
		}
		int endChar;
		try {
			int lines = pathLabel.getLineCount();
			endChar = lines * pathLabel.getLineEndOffset(lines);
		} catch(BadLocationException e){
			endChar = 1;
		}
		System.out.println(endChar);
		pathLabel.setText(string);
		frame.repaint();
		System.out.println("path: -------------------------------");
		return true;
	}

	public boolean simplePrint(Point2D.Float[] waypoints){
		if (waypoints == null){
			return true;
		}

		for(int i = 0; i<waypoints.length; i++ ){
			System.out.println(i +  ": " + waypoints[i].x + " | " + waypoints[i].y + " -> " + (waypoints[i].x/10) + " | " + (waypoints[i].y/10)); 
	
	} return true;
}
	/**
	 * It outputs the set of coordinates that make up a path. 
	 * 
	 * @param path The path consisting of the steps, and the coordinates of the steps that make the path.
	 */
/*	
	public static void pathCoordinates(Path path){
		
		for (int i=0; i<path.getLenght(); i++){
			 System.out.println(path.getStep(i).getX() + " | " + path.getStep(i).getY());
			 }
	}

*/

	
}
