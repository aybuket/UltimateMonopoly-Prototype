import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Player {
	private String name;
	private int money;
	private ArrayList<Square> places;
	private ArrayList<Cards> extraCards;
	private int position = 0;
	private Color c;
	private int numberOfPlace;
	private int numberOfCards;

	// Player constructor
	public Player() {
		this.name = null;
		money = 3200;
		places = new ArrayList<Square>();
		extraCards = new ArrayList<Cards>();
		numberOfPlace = 0;
		numberOfCards = 0;
	}
	
	// Player constructor with name
	public Player(String name) {
		this.name = name;
		money = 3200;
		places = null;
		extraCards = null;
		numberOfPlace = 0;
		numberOfCards = 0;
	}
	// buy the free place, if the place is full or price is too high return false
	public void buy(int price, Square place) {
		money -= price;
		places.add(place);
		numberOfPlace++;
		}
	// pay the rent
	public void payRent(int price) {
		money -= price;
	}
	
	public void receiveRent(int rent) {
		money += rent;
	}
	// Getters and setters
	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	public int getPosition() {
		return position;
	}
	
	public void setPosition(int p) {
		position += p;
		position = position %20;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getMoney() {
		return money;
	}


	public void setMoney(int money) {
		this.money += money;
	}


	public ArrayList<Square> getPlaces() {
		return places;
	}

	public void addPlace(Square place) {
		places.add(place);
		numberOfPlace++;

	}
	
	public ArrayList<Cards> getExtraCards() {
		return extraCards;
	}


	public void addExtraCard(Cards card) {
		extraCards.add(card);
		numberOfCards++;
	}

	public int getNumberOfPlace() {
		return numberOfPlace;
	}

	public void setNumberOfPlace(int numberOfPlace) {
		this.numberOfPlace = numberOfPlace;
	}

	public int getNumberOfCards() {
		return numberOfCards;
	}

	public void setNumberOfCards(int numberOfCards) {
		this.numberOfCards = numberOfCards;
	}


}
