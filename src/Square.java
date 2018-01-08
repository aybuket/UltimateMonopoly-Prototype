import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Square {
	
	private Color color;
	private int price;
	private String name;
	private boolean free;
	private int owner;
	private int rent;
	private Square[] colorGroup = new Square[2];
	
	// Square Constructor
	public Square(String n, Color c, int p, int r) {
		color = c;
		price = p;
		name = n;
		free = true;
		owner = -1;
		rent = r;
	}
	
	// Getters and setters
	
	public Square[] getColorGroup() {
		return colorGroup;
	}

	public void addColorGroup(Square s1, Square s2) {
		colorGroup[0] = s1;
		colorGroup[1] = s2;
	}
	
	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	
}

