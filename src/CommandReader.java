import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import server.twitter.SoccerEvent;
import server.twitter.UpdateTwitter;
import server.twitter.TwitPicBot;
import strategy.CurrentGlobalState;

/**
 * The Control gui for pausing/unpausing, and tweeting.
 * @author shearn89
 *
 */
public class CommandReader implements ActionListener{
	JButton pauseButton = new JButton("pause");
	JButton attackButton = new JButton("attack");
	JButton defendButton = new JButton("defend");
	JButton returnButton = new JButton("unpause/return");
	JButton ballButton = new JButton("reset ball");
	JButton quitButton = new JButton("quit");
	JButton tweetGoal = new JButton("tw/Goal");
	JButton tweetStart = new JButton("tw/Start Match");
	JButton tweetWin = new JButton("tw/Win");
	JButton tweetLoss = new JButton("tw/Loss");
	JButton tweetScreen = new JButton("tw/Screen");
	
	UpdateTwitter twitter = UpdateTwitter.getInstance();
	
	/**
	 * Initializes everything, creating the gui and showing it.
	 */
	public void create(){
		JFrame frame = new JFrame("Controls");
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	pauseButton.addActionListener(this);
    	attackButton.addActionListener(this);
    	defendButton.addActionListener(this);
    	returnButton.addActionListener(this);
    	ballButton.addActionListener(this);
    	quitButton.addActionListener(this);
    	
    	tweetGoal.addActionListener(this);
    	tweetStart.addActionListener(this);
    	tweetWin.addActionListener(this);
    	tweetLoss.addActionListener(this);
    	tweetScreen.addActionListener(this);
    	
        panel.add(pauseButton);
        panel.add(attackButton);
        panel.add(defendButton);
        panel.add(returnButton);
        panel.add(ballButton);
        panel.add(quitButton);
        
        panel2.add(tweetGoal);
        panel2.add(tweetStart);
        panel2.add(tweetWin);
        panel2.add(tweetLoss);
        panel2.add(tweetScreen);
        
        GridLayout holder = new GridLayout(2,0);
		JPanel panelHolder = new JPanel(holder);
		
		panelHolder.add(panel);
		panelHolder.add(panel2);
		
        frame.add(panelHolder);
        frame.setAlwaysOnTop(true);
        frame.setMaximumSize(new Dimension(600,100));

        frame.pack();
        frame.setVisible(true);  
	}

	/**
	 * The event handlers for the buttons.
	 */
	public void actionPerformed(ActionEvent e){
		CurrentGlobalState cs = CurrentGlobalState.getInstance();
		if (e.getSource() == pauseButton){
			System.out.println(">> [pause] pressed");
			cs.pause(true);
			cs.penalty('e');
		} else if (e.getSource() == attackButton){
			cs.pause(true);
			System.out.println(">> [attack] pressed");
			cs.penalty('a');
		} else if (e.getSource() == defendButton){
			cs.pause(true);
			System.out.println(">> [defend] pressed");
			cs.penalty('d');
		} else if (e.getSource() == returnButton){
			System.out.println(">> [return] pressed");
			cs.penalty('e');
			cs.pause(false);
		} else if (e.getSource() == ballButton){
			System.out.println(">> [reset ball] pressed");
			cs.resetBallLocation();
		} else if (e.getSource() == quitButton){
			System.out.println(">> [quit] pressed");
			cs.quit = true;
			System.exit(0);
		} else if (e.getSource() == tweetGoal ){
			System.out.println(">> [tweet goal] pressed");
			twitter.stewieDid(SoccerEvent.score);
		} else if (e.getSource() == tweetStart){
			System.out.println(">> [tweet start] pressed");
			twitter.stewieDid(SoccerEvent.beginMatch);
		} else if (e.getSource() == tweetWin){
			System.out.println(">> [tweet win] pressed");
			twitter.stewieDid(SoccerEvent.winMatch);
		} else if (e.getSource() == tweetLoss){
			System.out.println(">> [tweet loss] pressed");
			twitter.stewieDid(SoccerEvent.loseMatch);
		} else if (e.getSource() == tweetScreen){
			try {
	            Robot robot = new Robot();
	            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	            BufferedImage bi=robot.createScreenCapture(new Rectangle(dim));
	            ImageIO.write(bi, "jpg", new File("screenshot.jpg"));
	            twitter.stewieThought();
	        } catch (AWTException ex) {
	            ex.printStackTrace();
	        } catch (IOException ex2) {
	            ex2.printStackTrace();
	        }
		}
	}
}
