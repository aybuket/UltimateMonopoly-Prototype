import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Die extends JPanel{
	private Random rand = new Random();
	private int die1;
	private int die2;

	// Die constructor
	public Die() {
		die1 = -1;
		die2 = -1;
	}
	// roll only regular dice
	public void rollRegular() {
		die1 = rand.nextInt(6)+1;
		die2 = rand.nextInt(6)+1;
	}
	// get the total of regular dice
	public int getRegularTotal() {
		return die1+die2;
	}

	// regular double or not
	public boolean isDouble() {
		return die1==die2;
	}

	// get the label of the dice for GUI
	public String getDice(int i) {
		if(die1==-1) {
			return "R";
		}
		if(i == 1) {
			return die1+"";
		}else{
			return die2+"";
		}
	}
}