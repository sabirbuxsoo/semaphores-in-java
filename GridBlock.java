package socialDistanceShopSampleSolution;

import java.util.concurrent.Semaphore; //Importing Sempahore


// GridBlock class to represent a block in the shop.

public class GridBlock {
	private boolean isOccupied;
	private final boolean isExit; 
	private final boolean isCheckoutCounter;
	private int [] coords; // the coordinate of the block.
	private int ID;


	//Here, Semaphores were used to enforce Mutual Exclusion. 
	//Mutual Exclusion guarantees that only one thread accesses the shared variable at a time.
	//In this case, we make use of Mutual Exclusion to make sure that only one customer is in one grid block at any time, to prevent two customers from being on the same block, so that social distancing rule is followed. This is done by initializing Semaphore(1)
	private Semaphore mutual_exclusion; //Added the Mutual Exclusion sempahore
	
	public static int classCounter=0;
	
	GridBlock(boolean exitBlock, boolean checkoutBlock) throws InterruptedException {
		isExit=exitBlock;
		isCheckoutCounter=checkoutBlock;
		isOccupied= false;
		ID=classCounter;
		classCounter++;
		mutual_exclusion = new Semaphore(1); //Initializing Semaphore to 1,. 
	}
	
	GridBlock(int x, int y, boolean exitBlock, boolean refreshBlock) throws InterruptedException {
		this(exitBlock,refreshBlock);
		coords = new int [] {x,y};
	}

	//NOTE: synchronized is added for several methods to make the methods thread-safe.
	
	//getter
	public  int getX() {return coords[0];}  
	
	//getter
	 public  int getY() {return coords[1];}
	
	//for customer to move to a block
	//Modified to add a check if not occupied 
	 public boolean get() throws InterruptedException {
		mutual_exclusion.acquire(); //Here we acquire the mutual exclusion and block. One customer acquires a permit and we decrement Semaphore Count.
		if(isOccupied == false){ //Check if block not occupied
			isOccupied=true; //Set occupied to true
			mutual_exclusion.release(); //Customer left block, permit is released and Semaphore count incremented. Anotehr customer/thread can now acquire.
			return true;
		}else{ //If block occupied
			mutual_exclusion.release(); //Release the mutual exclusion and unblock. Ensures liveness
			return false;
		}
		
	}
		
	//for customer to leave a block
	//Added synchronized
	//Added mutual exlcusion acquire and release when customer is not on the current block anymore
	synchronized public  void release() throws InterruptedException{
		mutual_exclusion.acquire();//Here we acquire the mutual exclusion and block. 
		isOccupied =false;
		mutual_exclusion.release();//Release the mutual exclusion and unblock
	}
	
	//getter
	//Added synchronized 
	synchronized public boolean occupied() {
		return isOccupied;
	}
	
	//getter
	//Added synchronized 
	synchronized public  boolean isExit() {
		return isExit;	
	}

	//getter
	//Added synchronized 
	synchronized public  boolean isCheckoutCounter() {
		return isCheckoutCounter;
	}
	
	//getter
	//Added synchronized 
	synchronized public int getID() {return this.ID;}
}
