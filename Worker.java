import java.util.concurrent.Phaser;
import java.util.Arrays;

public class Worker extends Thread {
	public Person[] population;
	public Person[] topMembers;
	public Person bestMember;
	private Phaser synchPoint;
	private boolean running;
	private ThreadLocal<Person[]> p;

	public Worker(Person[] initPopulation, Phaser synchPoint){
		this.population = initPopulation;
		this.topMembers = new Person[population.length/Settings.THREADS];
		this.running = true;
		this.bestMember = new Person();
		this.synchPoint = synchPoint;
	}

	public void run(){
		synchPoint.register();
		while (running){
			evaluateFitness();
			setBest();
			//System.out.println(Thread.currentThread().getName() + " | " + bestMember);
			createMatingPool();
			synchPoint.arriveAndAwaitAdvance();
		}
	}

	private void evaluateFitness(){
		int fitness;
		int count;
		for (int i = 0; i < Settings.POP_SIZE; i++){
			count = 0;
			fitness = 0;
			for (int j = 0; j < Settings.GOAL_SIZE; j++){
				if (population[i].dna.charAt(j) == Settings.GOAL.charAt(j)){
					count++;
				}
			}
			double f = ((double)count/(double)Settings.GOAL_SIZE);
			fitness = (int) Math.round(f * 100);
			population[i].set_fitness(fitness);
		}
		Arrays.sort(population);
	}

	private void setBest(){
		if (population[0].fitness > bestMember.fitness){
			bestMember = population[0];
		}
		//System.out.println(Thread.currentThread().getName() + "\n");
		for (int i = 0; i < population.length; i++){
			//System.out.println(population[i]);
		}
		//System.out.println();
	}

	private void createMatingPool(){
		/* grabs top members of this workers population and get them ready to be added
		for the new population for the next generation */
		//System.out.println("Top Members:\n");
		for (int i = 0; i < population.length/Settings.THREADS; i++){
			topMembers[i] = population[i]; 
			//System.out.println(topMembers[i]);
		}
		//System.out.println();
	}

	public void setPopulation(Person[] population){
		this.population = population;
	}
}