//M. M. Kuttel
//Class to represent the shop, as a grid of gridblocks

 
package socialDistanceShopSampleSolution;

import java.util.concurrent.Semaphore; //Import Semaphore

//We make use of the Multiplex Pattern(See Little Book of Semaphores)
//The Multiplex Pattern allows multiple threads to run in the critical
//section at the same time. However, an upper limit is enforced on
//the number of cuncurrent threads.It is made sure that no more than n threads
//can run in the critical section at once. 
//For this situation, we are basically limiting the number of customers
//in the shop at once.
//By using Semaphores, we enforces the synchronization constraint by
//keeping track of the number of people inside the shop and barring arrivals when the shop is at it's maximum capacity


public class ShopGrid {
	//Defining Semaphores
	private Semaphore multiplex;
	private Semaphore door;
	private GridBlock [][] Blocks;
	private final int x;
	private final int y;
	public final int checkout_y;
	private final static int minX =5;//minimum x dimension
	private final static int minY =5;//minimum y dimension
	
	ShopGrid() throws InterruptedException {
		this.x=20;
		this.y=20;
		this.checkout_y=y-3;
		Blocks = new GridBlock[x][y];
		int [] [] dfltExit= {{10,10}};
		this.initGrid(dfltExit);

		//Initialize Sempahore initial permit count to 1(multiplex is default to 1) and setting the "how" boolean to true.
		//True ensures that waiting threads are granted a permit in the order in which they requested access (ReferenceL GeeksforGeeks.com)
		multiplex = new Semaphore(1,true);
		door = new Semaphore(1,true); //Only 1 thread(customer) at shop door. This allows customer to get into the shop
	}
	
	ShopGrid(int x, int y, int [][] exitBlocks,int maxPeople) throws InterruptedException {
		if (x<minX) x=minX; //minimum x
		if (y<minY) y=minY; //minimum x
		this.x=x;
		this.y=y;
		this.checkout_y=y-3;
		Blocks = new GridBlock[x][y];
		this.initGrid(exitBlocks);

		multiplex = new Semaphore(maxPeople,true); //Maximum number of threads/customers in the shop at once (max threads that can acquire lock and run in critical section at once).
		door = new Semaphore(1,true); //Restrict thread to one per door. Basically we are only allowing one customer at the shop entrance at any time.
	}
	
	private  void initGrid(int [][] exitBlocks) throws InterruptedException {
		for (int i=0;i<x;i++) {
			for (int j=0;j<y;j++) {
				boolean exit=false;
				boolean checkout=false;
				for (int e=0;e<exitBlocks.length;e++)
						if ((i==exitBlocks[e][0])&&(j==exitBlocks[e][1])) 
							exit=true;
				if (j==(y-3)) {
					checkout=true; 
				}//checkout is hardcoded two rows before  the end of the shop
				Blocks[i][j]=new GridBlock(i,j,exit,checkout);
			}
		}
	}
	
	//get max X for grid
	public  int getMaxX() {
		return x;
	}
	
	//get max y  for grid
	public int getMaxY() {
		return y;
	}

	public GridBlock whereEntrance() { //hard coded entrance
		return Blocks[getMaxX()/2][0];
	}

	//is a position a valid grid position?
	public  boolean inGrid(int i, int j) {
		if ((i>=x) || (j>=y) ||(i<0) || (j<0)) 
			return false;
		return true;
	}
	
	//called by customer when entering shop
	//Updated to acquire Sempahores
	public GridBlock enterShop() throws InterruptedException  {
		//If the Semaphore count > 0, the thread acquires a permit and decrements count of semaphore. 
		//Otherwise thread is blocked until a permit can be acquired.
		multiplex.acquire(); //Customer gets a spot in shop and max decrements. When hits 0, max number of customers are in shop
		door.acquire();//Only one customer at the door. 1 decrements to 0.
		GridBlock entrance = whereEntrance();
		return entrance;
	}
		
	//called when customer wants to move to a location in the shop
	public GridBlock move(GridBlock currentBlock,int step_x, int step_y) throws InterruptedException {  
		//try to move in 
		
		int c_x= currentBlock.getX();
		int c_y= currentBlock.getY();
		
		int new_x = c_x+step_x; //new block x coordinates
		int new_y = c_y+step_y; // new block y  coordinates
		
		//restrict i an j to grid
		if (!inGrid(new_x,new_y)) {
			//Invalid move to outside shop - ignore
			return currentBlock;
			
		}

		//Updated code
		if ((new_x==currentBlock.getX())&&(new_y==currentBlock.getY()) || (new_x == whereEntrance().getX() && new_y == whereEntrance().getY())) //not actually moving or customer tries to go back to shop door  position
			return currentBlock;
		 
		GridBlock newBlock = Blocks[new_x][new_y];
		
		if (newBlock.get())  {  //get successful because block not occupied 
				currentBlock.release(); //must release current block

				//Added code
				if(c_x == whereEntrance().getX() && c_y == whereEntrance().getY()){ 
					door.release(); //Once a customer moves into the shop, the permit is released at the door allowing other customer/thread waiting to acquire the permit at the door
				}
			}
		else {
			newBlock=currentBlock;
			///Block occupied - giving up
		}
		return newBlock;
	} 
	
	//called by customer to exit the shop
	//Updated Code
	public void leaveShop(GridBlock currentBlock) throws InterruptedException   {
		currentBlock.release();
		multiplex.release(); //Once a customer leaves the shop, the permit is released and the Semaphore Count is incremented thus allowing another thread/customer to acquire a permit and get into the shop
	}

}


	

	

