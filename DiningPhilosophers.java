package HW6;

import java.util.Random;

class DiningPhilosophers
{
	public static void main( String[] arg )
	{
		new DiningPhilosophers( 10, 60000 );
	}

	private String[] name = { "Seneca", "Aristotle", "Epicurius", "Voltaire", 
					"Kant", "Machiavelli", "Nietzsche", "Socrates", 
					"Frege", "Hume" };

	private Philosopher[] thinker;
	private Chopstick[]   chopstick;

	public DiningPhilosophers( int numPhilosophers, int duration )
	{
		initialize( numPhilosophers );  // construct the philosophers & chopsticks
		startSimulation(); 
		sleep( duration );              // let simulation run for desired time
		try{
			// this method might throw an interrupted excetion since it waits for threads to join
			shutdownPhilosophers();         // *gracefully* shut down the philosophers
		}catch(InterruptedException e){
			return;
		}
		printResults();
	}

	private void initialize( int n )  // handles 2 to 10 philosophers
	{
		if ( n > 10 )
		n = 10;
		else if ( n < 2 )
		n = 2;

		thinker = new Philosopher[n];
		chopstick = new Chopstick[n];

		for ( int i = 0 ; i < n ; i++ )
		chopstick[i] = new Chopstick(i);

		for ( int i = 0 ; i < n ; i++ )
		thinker[i] = new Philosopher( name[i], chopstick[i], chopstick[(i+1)%n] );
	}

	private void startSimulation()
	{
		int n = thinker.length; // the number of philosophers

		System.out.print( "Our " + n + " philosophers (" );
		for ( int i = 0 ; i < (n-1) ; i++ )
		System.out.print( name[i] + ", " );
		System.out.println( "and " + name[n-1] +
				") have gather to think and dine" );

		System.out.println( "-----------------------------------------------");

		for ( int i = 0 ; i < n ; i++ )
		thinker[i].start();
	}

	private void sleep( int duration )
	{
		try
		{
		Thread.currentThread().sleep( duration );
		}
		catch ( InterruptedException e )
		{
		Thread.currentThread().interrupt();
		}
	}

	private void shutdownPhilosophers() throws InterruptedException
	{
		/********* YOUR CODE GOES HERE **********/
		// For each philosopher, interrupt corresponding thread
		int n = thinker.length; // the number of philosophers
		for ( int i = 0 ; i < n ; i++ )
		thinker[i].interrupt();

		// Parent thread should now wait until each
		// philosopher has gracefully exited
		for ( int i = 0 ; i < n ; i++ )
		thinker[i].join();
	}

	private void printResults()
	{
		System.out.println( "-----------------------------------------------");

		int n = thinker.length; // the number of philosophers

		for ( int i = 0 ; i < n ; i++ )
		System.out.println( thinker[i] );

		System.out.flush();
	}
}

