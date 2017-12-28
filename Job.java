import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.Phaser;

public class Job extends Thread {
	private final Worker[] workers;
	private Person[][] nextGeneration;
	private boolean running;
	private Phaser synchPoint;

	public Job(Person[] initPopulation) {
		this.workers = new Worker[Settings.THREADS];
		this.nextGeneration = new Person[Settings.THREADS][Settings.POP_SIZE];
		this.running = true;
		this.synchPoint = new Phaser(1);
		init(initPopulation);
	}

	public void run(){
		startWorkers();
		Person oldBest = new Person();
		Person bestMember = new Person();

		while (running){
			while (synchPoint.getArrivedParties() != Settings.THREADS);
			bestMember = checkBestMember();
			if (!(oldBest.dna.equals(bestMember.dna))){
				System.out.println("Best: " + bestMember);
			}
			oldBest = bestMember;
			if (bestMember.fitness == 100){
				running = false;
				System.exit(0);
			} else {
				reset(createMatingPool(getOldGeneration()));
			}
			synchPoint.arriveAndAwaitAdvance();
		}
	}

	private void startWorkers(){
		for (int i = 0; i < Settings.THREADS; i++){
			workers[i].start();
		}
	}

	private void init(Person[] initPopulation){
		for (int i = 0; i < workers.length; i++){
			workers[i] = new Worker(initPopulation, synchPoint);
		}
	}

	private static Person combine(Person one, Person two){
		Person child;
		String dna = "";
		int rand = 0;
		Random r = new Random();

		for (int i = 0; i < Settings.GOAL_SIZE; i++){
			if (i % 2 == 0){
				dna += one.dna.charAt(i);
			} else {
				dna += two.dna.charAt(i);
			}
		}
		child = new Person(dna);
		child.fitness = 0;
		child.dna = mutate(child.dna);
		return child;
	}

	private static String mutate(String dna){
		char[] tmp_dna = dna.toCharArray();
		for (int i = 0; i < Settings.GOAL_SIZE; i++){
			Random r = new Random();
			if (Math.random() <= Settings.MUTATION_RATE){
				char c = Settings.CHARS.charAt(r.nextInt(Settings.CHARS.length()));
				tmp_dna[i] = c; 
			}
		}
		String mutated = new String(tmp_dna);
		return mutated;
	}

	private Person checkBestMember(){
		Person best = new Person();
		int workerID = 0;
		for (int i = 0; i < workers.length; i++){
			if (workers[i].bestMember.fitness > best.fitness) best = workers[i].bestMember;
		}
		return best;
	}

	private void reset(Person[] newPopulation){
		for (int i = 0; i < workers.length; i++){
			workers[i].setPopulation(newPopulation);
		}
	}

	private Person[] getOldGeneration(){
		Person[] oldPopulation = new Person[Settings.POP_SIZE];
		int offset = 0;
		for (int i = 0; i < Settings.THREADS; i++){
			for (int j = 0; j < workers[i].topMembers.length; j++, offset++){
				oldPopulation[offset] = workers[i].topMembers[j]; 
			}
		}
		return oldPopulation;
	}

	private Person[] createMatingPool(Person[] oldPopulation){
		Random r = new Random();
		//System.out.println("New Population:\n");
		Person[] newPopulation = new Person[Settings.POP_SIZE];
		for (int j = 0; j < Settings.POP_SIZE; j++){
			Person one = oldPopulation[r.nextInt(10)];
			Person two = oldPopulation[r.nextInt(10)];
			newPopulation[j] = combine(one, two);
			//System.out.println(newPopulation[j]);
		}
		return newPopulation;
	}
}