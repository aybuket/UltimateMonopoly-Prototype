import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



public class Proto extends JPanel{
	private static JFrame frame = new JFrame("Monopoly Proto");
	private static JPanel rollPanel = new JPanel();
	private static JPanel buyRentPanel = new JPanel();
	private static JButton rollR = new JButton("Roll");
	private static JButton rollT = new JButton("Special Roll");
	private static JButton buy = new JButton("Buy");
	private static JButton payRent = new JButton("Pay Rent");
	private static JButton pass = new JButton("Pass");
	private static Board gameBoard = new Board(200,100,1024,768);

	public static void createFrame() {
		// frame settings
		frame.setVisible(true);
		frame.setSize(new Dimension(1180,768));
		frame.setResizable(false);
		// add roll button panel and buy/pay rent panel to the frame
		frame.add(rollPanel);
		frame.add(buyRentPanel);
		frame.add(gameBoard);
		// roll panel settings
		rollPanel.setVisible(true);
		rollPanel.setSize(150, 75);
		rollPanel.setLocation(857, 87);
		// roll buttons settings
		rollR.setVisible(true);
		rollT.setVisible(true);
		// add roll buttons to the panel
		rollPanel.add(rollR);
		rollPanel.add(rollT);
		rollT.setEnabled(false);
		pass.setEnabled(false);
		// Mouse clicked actions
		rollR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameBoard.rollRegular();
				rollR.setEnabled(false);
				if(gameBoard.needRent()) {
					payRent.setEnabled(true);
					pass.setEnabled(false);
				}else if(gameBoard.canBuy()) {
					buy.setEnabled(true);
					pass.setEnabled(true);
				}else if (gameBoard.squeezePlay()) {
					rollT.setText("Squeeze");
					rollT.setEnabled(true);
					pass.setEnabled(false);
				}else if (gameBoard.once()) {
					rollT.setText("Roll Once");
					rollT.setEnabled(true);
					pass.setEnabled(false);
				}else {
					pass.setEnabled(true);

				}
				frame.repaint();
			}
		});
		rollT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rollT.getText()=="Squeeze") {
					gameBoard.squeezeRoll();
					pass.setEnabled(true);
				}
				if(rollT.getText()=="Roll Once") {
					gameBoard.rollOnce();
					pass.setEnabled(true);
				}
				frame.repaint(); 
				rollT.setEnabled(false);
				if (gameBoard.squeezePlay()) {
					rollT.setText("Squeeze");
					rollT.setEnabled(true);
					pass.setEnabled(false);
				}else if (gameBoard.once()) {
					rollT.setText("Roll Once");
					rollT.setEnabled(true);
					pass.setEnabled(false);
				}else {
					pass.setEnabled(true);

				}
				rollT.setText("Special Roll");
				frame.repaint();
			}
		});
		// buy/pay rent panel settings
		buyRentPanel.setVisible(true);
		buyRentPanel.setSize(150,100);
		buyRentPanel.setLocation(857,254);
		// buy/pay rent buttons settings
		buy.setVisible(true);
		buy.setEnabled(false);
		payRent.setVisible(true);
		payRent.setEnabled(false);
		// add buy/pay rent buttons to the panel
		buyRentPanel.add(buy);
		buyRentPanel.add(payRent);
		buyRentPanel.add(pass);
		// Mouse clicked actions
		buy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameBoard.buy();
				frame.repaint();
				buy.setEnabled(false);
				pass.setEnabled(true);
			}
		});
		payRent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameBoard.payRent();
				frame.repaint();
				payRent.setEnabled(false);
				pass.setEnabled(true);
			}
		});
		pass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameBoard.passTurn();
				frame.repaint();
				rollR.setEnabled(true);
				buy.setEnabled(false);
				pass.setEnabled(false);

			}
		});

	}

	// main function
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				createFrame();
			}
		});
	}

}
