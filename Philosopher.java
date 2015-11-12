package HW6;

import java.util.Random;

class Philosopher extends Thread
{
	static private Random random = new Random();

	private String name;
	private Chopstick leftStick;
	private Chopstick rightStick;

	private int eatingTime   = 0;
	private int thinkingTime = 0;
	private int countEat     = 0;
	private int countThink   = 0;

	public Philosopher( String name, Chopstick leftStick, Chopstick rightStick )
	{
		this.name = name;
		this.leftStick = leftStick;
		this.rightStick = rightStick;
	}
	
	// Helper method for Chopstick class
	public int getIDforLeftChopstick(){
		return leftStick.getID();
	}
	
	// Helper method for Chopstick class
	public int getIDforRightChopstick(){
		return rightStick.getID();
	}

	public String toString()
	{
		return name + " ate " + countEat + " times (" +
		eatingTime + " ms) and pondered " + countThink + " times (" +
		thinkingTime + "ms)";
	}
	
	public void run()
	{
		// Make sure to gracefully terminate
		// thread once it is interrupted.
		try{
			while(!this.interrupted()){
				countThink++;
				thinkingTime += doAction( "think" );
				while(!pickupChopsticks()){ /* trying again and again until it succeeds */ };
				countEat++;
				eatingTime += doAction( "eat" );
				putdownChopsticks();
			}
		}catch(InterruptedException e){
			return;
		}
	}

	private int doAction( String act ) throws InterruptedException
	{
		int time = random.nextInt( 4000 ) + 1000 ;
		System.out.println( name + " is begining to " + act + " for " + time + 
				" milliseconds" );
		sleep( time );

		System.out.println( name + " is done " + act + "ing" );

		return time;
	}
	
	/*
	 
	This method will have a philosopher thread
	try to pick up chopsticks one at a time. After
	picking up the right chopstick, pick up the left stick. 
	If the left stick is in use, return the right stick and
	this method returns false. Otherwise, once both
	sticks are picked up, return true (to allow philosopher 
	to eat). If the right stick was in use to begin with, 
	the method immediately returns false.
	 
	*/
	private boolean pickupChopsticks() throws InterruptedException
	{
		System.out.println( name + " wants right " + rightStick );

		if(rightStick.pickUp(this)){
			// The right stick has been picked up
			// and the left stick is needed before eating

			System.out.println( name + " has right " + rightStick );
			System.out.println( name + " wants left " + leftStick );

			if(leftStick.pickUp(this)){
				// The left stick is available
				// and picked up. This thread is
				// now ready to eat.

				System.out.println( name + " has both left " + leftStick +
				" and right " + rightStick );

				return true;
			}else{
				// The left stick is in use so
				// we put back the right stick
				// and continue executing method

				System.out.println( name + " was unable to get the left " + leftStick );
				System.out.println( name + " politely returned right " + rightStick );

				rightStick.putDown(this);
			}
		}
		// If we haven't return true yet, we unsuccessfully
		// tried to pick up the chopsticks
		return false;
	}

	private void putdownChopsticks()
	{
		// return right chopstick
		rightStick.putDown(this);

		System.out.println( name + " finished using right " + rightStick );
		
		// return left chopstick
		leftStick.putDown(this);

		System.out.println( name + " finished using left " + leftStick );
	}
}

