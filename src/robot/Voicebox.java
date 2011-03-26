import java.io.File;

import lejos.nxt.Sound;

/*
 * Voicebox class for Stewie.
 *
 * This class needs to be included in the stewie package,
 * or you need to remove the package declaration in line 1.
 *
 * This class also depends on the .wav files in the trunk/.
 * If they're not in the root directory on the bot, nothing will play..
 *
 * To say something, simply instantiate the Voicebox, then select the 
 * soundbite with say(<number>), and run start() to play the sound. 
 * You then need to do a join() at the end of the program (or wherever)
 * to make sure the thread has finished. otherwise the program freezes.
 *
 * Needs discussion. Alex Shearn
 */
public class Voicebox extends Thread{
	String selected = "win.wav";
	/**
	 * The selection method for the class.
	 * @param name Can be 1/2/3, for win/lose/score.
	 */
	public void say(int name){
		switch (name) {
			case 1: selected = "win.wav"; break;
			case 2: selected = "lose.wav"; break;
			case 3: selected = "score.wav"; break;
			default: System.out.println("Invalid sound choice.");
		}
	}
	/**
	 * Plays the sound file selected with say().
	 */
	void play(){
		File sound = new File(selected);
		if (sound.exists()){
			Sound.playSample(sound, 100);
			try { Thread.sleep(5000);
			} catch (Exception e) {}
		} else {
			System.out.println("soundfile not found...");
			try { Thread.sleep(1000);
			} catch (Exception e) {}
		}
	}
	/**
	 * The method required to run in a seperate thread, to prevent truncation of the sound file.
	 */
	public void run(){
		play();
	}
}
