package socialDistanceShopSampleSolution;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.atomic.*;

//No modification was made in this class.
//When it comes to multi-threading, if two or more threads try to get and update the value at the same time, it may result in lost updates.
//When two or more threads are trying to access the variables in this class, a lock is placed on the variable and no updates can be made until the lock is released.
//Here, atomic variables were thus used for synchronization purposes (providing the lock mechanism and prevent race condition), and enforces liveness. 


public class CustomerLocation  { // this is a separate class so don't have to access thread
	
//can protect with Atomic variables or with synchronized	
	private final int ID; //total customers created
	private Color myColor;

	//The Atomic variables below were defined to be used by the Customer thread to track the customer in room and their position
	private AtomicBoolean inRoom;
	private AtomicInteger x;
	private AtomicInteger y;
	
	//CustomerLocation
	//Assign random ID and color to customer
	//Initially outside room at position (0,0)
	CustomerLocation(int ID ) {
		Random rand = new Random();
		float c = rand.nextFloat();
		myColor = new Color(c, rand.nextFloat(), c);	//only set at beginning	by thread
		inRoom = new AtomicBoolean(false);
		this.ID=ID;
		this.x = new AtomicInteger(0);
		this.y = new AtomicInteger(0);
	}

	
	//setter
	public  void  setX(int x) { this.x.set(x);}	
		
	//setter
	public   void  setY(int y) {	this.y.set(y);	}
	
	//setter
	public  void setInRoom(boolean in) {
		this.inRoom.set(in);
}
	//getter
	public  int getX() { return x.get();}	
	
	//getter
	public  int getY() {	return y.get();	}
	
	//getter
		public  int getID() {	return ID;	}

	//getter
		public  boolean inRoom() {
			return inRoom.get();
		}
	//getter
	public synchronized  Color getColor() { return myColor; } //This is synchronized to avoid race condition.
		
}
