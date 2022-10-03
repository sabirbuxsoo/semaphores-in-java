package socialDistanceShopSampleSolution;

//class to keep track of people inside and outside and left shop
public class PeopleCounter {
	private int peopleOutSide; //counter for people arrived but not yet in the building
	private int peopleInside; //people inside the shop
	private int peopleLeft; //people left the shop
	private int maxPeople; //maximum for lockdown rules
	
	PeopleCounter(int max) {
		peopleOutSide = 0;
		peopleInside = 0;
		peopleLeft = 0;
		maxPeople = max; //0 changed to max. (Correction) This gives the maximum number of people in the shop at a time.
	}
		
	//All methods below been modified, with the exception of resetScore() (which already has it), to include the 'synchronized' keyword.
	//We make use of the synchronized methods to lock an object for any shared resource.
	//When a thread invokes a synchronized method, it automatically acquires the lock for that object and releases it when the thread completes its task.
	//The reason synchronized is added is to prevent race condition from happening.



	//getter
	//Corrected and added synchronized to prevent race condition
	synchronized public int getWaiting() {
		return peopleOutSide;
	}

	//getter
	//Corrected and added synchronized to prevent race condition
	synchronized public int getInside() {
		return peopleInside;
	}
	
	//getter
	//Corrected and added synchronized to prevent race condition
	synchronized public int getTotal() {
		return (peopleOutSide+peopleInside+peopleLeft);
	}

	//getter
	//Corrected and added synchronized to prevent race condition
	synchronized public int getLeft() {
		return peopleLeft;
	}
	
	//getter
	//Corrected and added synchronized to prevent race condition
	synchronized public int getMax() {
		return maxPeople;
	}
	
	//getter
	//Corrected and added synchronized to prevent race condition
	synchronized public void personArrived() {
		peopleOutSide++;
	}
	
	//update counters for a person entering the shop
	//Corrected and added synchronized to prevent race condition
	synchronized public void personEntered() {
		peopleOutSide--;
		peopleInside++;
	}

	//update counters for a person exiting the shop
	//Corrected and added synchronized to prevent race condition
	synchronized public void personLeft() {
		peopleInside--;
		peopleLeft++;
		
	}

	//reset - not really used
	synchronized public void resetScore() { //No Update here, as it already contains the synchronized method
		peopleInside = 0;
		peopleOutSide = 0;
		peopleLeft = 0;
	}
}
