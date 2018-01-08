import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Board extends JPanel{
	private Square[] board;
	private int width;
	private int height;
	private int offsetX;
	private int offsetY;
	private Player[] players = new Player[4];
	private Die die = new Die();
	private int moveSq = 0;
	private static int turn = 0;
	private boolean rolled = false;
	private boolean squeeze = true;
	private boolean rollOnce = true;
	private boolean rollOnceGotDouble = false;
	//	private Cards cards;


	// Board constructor
	public Board(int offsetX, int offsetY, int width, int height){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;
		board = new Square[20];
		createPlayers();
		setup();
		//		cards = new Cards();
	}

	// rolls just regular dice and updates the current player
	public void rollRegular() {
		die.rollRegular();
		moveSq = die.getRegularTotal();
		int position = players[turn].getPosition();
		if(position+moveSq>19) {
			players[turn].setMoney(board[0].getPrice());
		}
		players[turn].setPosition(moveSq);
		rolled = true;
	}

	// check if the place is sold and player needs to pay rent
	public boolean needRent() {
		int position = players[turn].getPosition();
		boolean isMine = turn == board[position].getOwner();
		return !board[position].isFree() && !isMine;
	}

	// check if the place can be buyable
	public boolean canBuy() {
		int position = players[turn].getPosition();
		if(position == 0 || position == 2 || position == 5 || position == 7 || position == 10 || position == 12 || position == 15 || position == 18){
			return false;
		}
		if(players[turn].getMoney()<board[position].getPrice()) {
			return false;
		}
		return board[position].isFree();
	}

	// If the player is on the squeeze play square, then activate the squeeze flag
	public boolean squeezePlay() {
		int position = players[turn].getPosition();
		if(position == 15 && squeeze) {
			squeeze = false;
			return true;
		}
		squeeze = true;
		return false;
	}

	public void squeezeRoll() {
		die.rollRegular();
		int total =  die.getRegularTotal();
		int money;
		if(total==2 || total==12) {
			money = 200;
		}else if (total==3 || total==4 || total==10 || total==11) {
			money = 100;
		}else {
			money = 50;
		}
		for(int i=0; i<players.length;i++) {
			if(i==turn) {
				players[i].setMoney(money);
			}else{
				players[i].setMoney(-1*money);
			}
		}
	}

	public void rollOnce() {
		die.rollRegular();
		if(die.isDouble()) {
			players[turn].setMoney(100);
			rollOnceGotDouble = true;
		}else {
			rollOnceGotDouble = false;
		}
	}
	public boolean once() {
		int position = players[turn].getPosition();
		if(position == 5 && rollOnce) {
			rollOnce = false;
			return true;
		}
		rollOnce = true;
		return false;
	}

	// pay rent function
	public void payRent() {
		int position = players[turn].getPosition();
		Square place = board[position];
		Player owner = players[place.getOwner()];
		Player tenant = players[turn];
		int rent = place.getRent();
		owner.receiveRent(rent);
		tenant.payRent(rent);
	}

	// returns die
	public Die getDie() {
		return die;
	}

	// pass turn if the die is not double 
	public void passTurn() {
		if(!die.isDouble() || rollOnceGotDouble) {
			turn = (turn+1)%4;
		}
	}

	// buy function
	public void buy() {
		Player p = players[turn];
		int position = p.getPosition();
		Square place = board[position];
		int price = place.getPrice();
		place.setFree(false);
		place.setOwner(turn);
		p.buy(price, place);
		Square cg1 = place.getColorGroup()[0];
		Square cg2 = place.getColorGroup()[1];
		// if all color group is belong to same player, triple the rents
		if(cg1.getOwner() == turn && cg2.getOwner() == turn) {
			place.setRent(place.getRent()*3);
			cg1.setRent(cg1.getRent()*3);
			cg2.setRent(cg2.getRent()*3);
		}
	}

	//	public void chance(Card c) {
	//		int price = c.getPrice();
	//		int fromOthers = c.getGetFromOthers();
	//		int move = c.getMove();
	//		if(fromOthers != 0) {
	//			for(int i=0; i<players.length; i++) {
	//				if(i==turn) {
	//					players[i].setMoney(price);
	//				}else {
	//					players[i].setMoney(fromOthers);
	//				}
	//			}
	//		}
	//		if(move != 0) {
	//			int position = players[turn].getPosition();
	//			if(position+move>19) {
	//				players[turn].setMoney(board[0].getPrice());
	//			}
	//			players[turn].setPosition(move);
	//		}
	//	}

	// GUI Paint function
	public void paint(Graphics g) {
		int rectWidth = 137;
		int rectHeight = 111;
		int xCoor = 20;
		int yCoor = 50;
		//drawing the community and chance card places
		g.setFont(new Font("Courier New", 1, 20));
		g.drawRect(xCoor+rectWidth*2, yCoor+rectHeight+rectHeight/2, rectWidth*2, rectHeight);
		g.drawString("Community Chest", xCoor+rectWidth*2+rectWidth/3, yCoor+rectHeight*2);
		g.drawRect(xCoor+rectWidth*2, yCoor+rectHeight*3+rectHeight/2, rectWidth*2, rectHeight);
		g.drawString("Chance", xCoor+rectWidth*2+2*rectWidth/3, yCoor+rectHeight*4);
		// drawing all squares
		for(int i=0; i<6; i++) {
			g.setFont(new Font("Courier New", 1, 10));
			//upside 
			createSquare(g,i,xCoor+i*rectWidth,yCoor,rectWidth,rectHeight);
			writeString(g,i,xCoor+i*rectWidth+5,yCoor,rectWidth,rectHeight);
			//downside
			createSquare(g,15-i,xCoor+i*rectWidth,yCoor+5*rectHeight, rectWidth, rectHeight);
			writeString(g,15-i,xCoor+i*rectWidth+5,yCoor+5*rectHeight,rectWidth,rectHeight);
			if(i!=0 && i!=5) {
				//leftside
				createSquare(g,20-i,xCoor, yCoor+i*rectHeight, rectWidth, rectHeight);
				writeString(g,20-i,xCoor+5,yCoor+i*rectHeight,rectWidth, rectHeight);
				//rightside
				createSquare(g, 5+i, xCoor+5*rectWidth, yCoor+i*rectHeight, rectWidth, rectHeight);
				writeString(g,5+i, xCoor+5*rectWidth+5,yCoor+i*rectHeight, rectWidth, rectHeight);
			}
		}
		// drawing dice
		for(int i=0; i<2; i++) {
			g.setColor(Color.lightGray);
			g.fillRoundRect(xCoor+6*rectWidth+35+i*60, yCoor+rectHeight+17, 50, 50, 15,15);
			g.setColor(Color.black);
			String s = die.getDice(i+1);
			g.setFont(new Font("Courier New", 1, 40));
			g.drawString(s, xCoor+6*rectWidth+35+i*60+15, yCoor+rectHeight+53);
			g.drawRoundRect(xCoor+6*rectWidth+35+i*60, yCoor+rectHeight+17, 50, 50, 15,15);
		}
		// all player related parts (player board, pawns)
		g.setFont(new Font("Courier New", 1, 10));
		for(int i=0; i<4; i++) {
			// place pawns
			putPlayerPawn(xCoor, yCoor, rectWidth, rectHeight, i, g);
			// color the current player and if player is on the community chest or chance square, paint the card place
			if(turn==i) {
				// color the current player
				g.setColor(players[i].getC());
				g.fillRect(xCoor+6*rectWidth+15, yCoor+2*rectHeight+i*2*rectHeight/3+97, 150, rectHeight/3);
				// color the community chest card place if current player is on 
				g.setFont(new Font("Courier New", 1, 20));
				if( (players[turn].getPosition()==2 || players[turn].getPosition()==12) && rolled) {
					g.setColor(Color.black);
					g.fillRect(xCoor+rectWidth*2, yCoor+rectHeight+rectHeight/2, rectWidth*2, rectHeight);
					g.setColor(Color.white);
					g.drawString("Community Chest", xCoor+rectWidth*2+rectWidth/3, yCoor+rectHeight*2);
				}
				// color the chance card place if current player is on 
				if( (players[turn].getPosition()==7 || players[turn].getPosition()==18) && rolled) {
					g.setColor(Color.black);
					g.fillRect(xCoor+rectWidth*2, yCoor+rectHeight*3+rectHeight/2, rectWidth*2, rectHeight);
					g.setColor(Color.white);
					//					Card chance = cards.randomChance();
					//					String name = chance.getName();
					//					String text = chance.getText();
					//					chance(chance);
					g.drawString("Chance", xCoor+rectWidth*2+2*rectWidth/3, yCoor+rectHeight*4);
				}
				g.setFont(new Font("Courier New", 1, 10));
				rolled = false;
			}
			// draw the player board
			g.setColor(Color.black);
			g.drawRect(xCoor+6*rectWidth+15, yCoor+2*rectHeight+i*2*rectHeight/3+97, 150, rectHeight/3);
			String name = players[i].getName();
			if(name==null) {
				name = "Player "+(i+1);
			}
			g.drawString(name, xCoor+6*rectWidth+35, yCoor+2*rectHeight+rectHeight/5+i*2*rectHeight/3+97);
			g.drawRect(xCoor+6*rectWidth+15, yCoor+2*rectHeight+rectHeight/3+i*2*rectHeight/3+97, 150, rectHeight/3);
			g.drawString(players[i].getMoney()+"$", xCoor+6*rectWidth+35, yCoor+2*rectHeight+rectHeight/5+rectHeight/3+i*2*rectHeight/3+97);
			// draw assests board
			g.setColor(players[i].getC());
			g.fillRect(1024, yCoor+rectHeight/3+i*(rectHeight+rectHeight/3), rectWidth, rectHeight/3);
			g.setColor(Color.black);
			g.drawRect(1024, yCoor+rectHeight/3+i*(rectHeight+rectHeight/3), rectWidth, rectHeight/3);
			g.drawRect(1024, yCoor+2*rectHeight/3+i*(rectHeight+rectHeight/3), rectWidth, rectHeight);
			g.drawString(name, 1065, yCoor+rectHeight/5+rectHeight/3+i*(rectHeight+rectHeight/3));
			ArrayList<Square> places = players[i].getPlaces();
			for(int j=0; j<places.size(); j++) {
				Square p = places.get(j);
				Color c = p.getColor();
				g.setColor(c);
				g.drawString(p.getName(), 1030, yCoor+2*rectHeight/3+i*(rectHeight+rectHeight/3)+15*(j+1));			
			}
		}
	}

	// places the pawn to the calculated places 
	private void putPlayerPawn(int x, int y, int w, int h, int p, Graphics g) {
		int position = players[p].getPosition()%20;
		Color c = players[p].getC();
		int xCoordinate;
		int yCoordinate;
		g.setColor(c);
		// calculation for upper side of the board
		if(position<6) {
			xCoordinate = x+position*w+p*30+15;
			yCoordinate = y+h/2+5;
			// calculation for right side of the board
		}else if(position <10) {
			xCoordinate = x+5*w+p*30+15;
			yCoordinate = y+(position-5)*h+h/2+5;
			// calculation for bottom side of the board
		}else if(position<16) {
			xCoordinate = x+(15-position)*w+p*30+15;
			yCoordinate = y+5*h+h/2+5;
			// calculation for left side of the board
		}else {
			xCoordinate = x+p*30+15;
			yCoordinate = y+(20-position)*h+h/2+5;
		}
		g.fillOval(xCoordinate,yCoordinate,20,20);
		g.setColor(Color.black);
		g.drawOval(xCoordinate, yCoordinate, 20, 20);
	}
	// creates a colored box with border
	private void createSquare(Graphics g, int i, int x, int y, int w, int h) {
		g.setColor(board[i].getColor());
		g.fillRect(x, y, w,h/3);
		g.setColor(Color.black);
		g.drawRect(x, y, w, h);
	}
	// places the string on the colored part of the square
	private void writeString(Graphics g, int i, int x, int y, int w, int h) {
		g.setColor(Color.white);
		String name = board[i].getName();
		int price = board[i].getPrice();
		int rent = board[i].getRent();
		if(price != 0 && name != "Go" ){
			g.drawString(name, x, y+h/7);
			g.drawString("Price: "+price+"$ Rent: "+rent+"$", x, y+h/4);
		}else {
			g.drawString(name, x, y+h/5);
		}
	}
	// manuel setup of the squares
	private void setup() {
		board[0] = new Square("Go", Color.gray, 200, 0);
		board[1] = new Square("Oriental Avenue", new Color(73, 165, 253), 100,6);
		board[2] = new Square("Community Chest", Color.black, 0,0);
		board[3] = new Square("Vermont", new Color(73, 165, 253), 100,6);
		board[4] = new Square("Connecticut Avenue", new Color(73, 165, 253), 120,8);
		board[5] = new Square("Roll once", Color.black, 0,0);
		board[6] = new Square("St. Charles Place", new Color(255, 89, 183), 140,10);
		board[7] = new Square("Chance", Color.black, 0,0);
		board[8] = new Square("States Avenue", new Color(255, 89, 183), 140,10);
		board[9] = new Square("Virginia Avenue", new Color(255, 89, 183), 160,12);
		board[10] = new Square("Free Parking", Color.black, 0,0);
		board[11] = new Square("St. James Place", new Color(255, 128, 0), 180,14);
		board[12] = new Square("Community Chest", Color.black, 0,0);
		board[13] = new Square("Tennessee Avenue", new Color(255, 128, 0), 180,14);
		board[14] = new Square("New York Avenue", new Color(255, 128, 0), 200,16);
		board[15] = new Square("Squeeze Play", Color.black, 0,0);
		board[16] = new Square("Pacific Avenue", new Color(153, 228, 1), 300,26);
		board[17] = new Square("North Carolina Avenue", new Color(153, 228, 1), 300,26);
		board[18] = new Square("Chance", Color.black, 0,0);
		board[19] = new Square("Pennsylvania Avenue", new Color(153, 228, 1), 320,28);
		// blue color group
		board[1].addColorGroup(board[3], board[4]);
		board[3].addColorGroup(board[1], board[4]);
		board[4].addColorGroup(board[1], board[3]);
		// pink color group
		board[6].addColorGroup(board[8], board[9]);
		board[8].addColorGroup(board[6], board[9]);
		board[9].addColorGroup(board[6], board[8]);
		// orange color group
		board[11].addColorGroup(board[13], board[14]);
		board[13].addColorGroup(board[11], board[14]);
		board[14].addColorGroup(board[11], board[13]);
		// green color group
		board[16].addColorGroup(board[17], board[19]);
		board[17].addColorGroup(board[16], board[19]);
		board[19].addColorGroup(board[16], board[17]);
	}
	// creates 4 unnamed player
	private void createPlayers() {
		for(int i=0; i<4; i++) {
			Player pl = new Player();
			players[i] = pl;
		}
		players[0].setC(new Color(249, 179, 179));
		players[1].setC(new Color(168, 237, 223));
		players[2].setC(new Color(247, 240, 163));
		players[3].setC(new Color(209, 170, 247));
	}
}
