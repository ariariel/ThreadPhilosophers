package HW6;

class Chopstick
{
	private final int id;
	private Philosopher heldBy = null;

	public Chopstick( int id )
	{
		this.id = id;
	}

	// Helper method for Chopstick
	public synchronized boolean inUse(){
		if(heldBy == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	// Helper method used by Philosopher
	public int getID(){
		return id;
	}
	public String toString()
	{
		return "chopstick (" + id + ")";
	}

	synchronized public boolean pickUp(Philosopher p) throws InterruptedException
	{
		/********* YOUR CODE GOES HERE **********/
		// Guarding loop to make sure
		// philosopher does not pick up
		// a chopstick thats being used
		while(inUse()){
			// Tell philosopher to go to the wait set
			// of this chopstick's lock.
			wait();
		}
		//Critical Section
		heldBy = p;
		return true;
	}

	synchronized public void putDown(Philosopher p)
	{
		/********* YOUR CODE GOES HERE **********/
		// If current philosopher tries to put down
		// a chopstick that wasn't held to begin with
		// or that was held by another philosopher,
		// throw run-time exception.
		if (heldBy == null || (this.id != p.getIDforLeftChopstick() && this.id != p.getIDforRightChopstick())){
			throw new RuntimeException( "Exception: " + p + " attempted to put " + "down a chopstick he wasn't holding." );
		}else{
			// Otherwise, clear the 'heldBy' flag
			// and notify the philosopher sitting
			// next to you (and the chopstick).
			// This should be the only philosopher
			// that could be waiting for this chopstick.
			heldBy = null;
			notify();
		}
	}
}

