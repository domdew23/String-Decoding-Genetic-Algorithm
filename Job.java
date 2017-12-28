import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.Phaser;

public class Job extends Thread {
	private final Worker[] workers;
	private Person[][] nextGeneration;
	private boolean running;
	private Person[] population;

	public Job(Person[] population) {
		this.workers = new Worker[Settings.THREADS];
		this.nextGeneration = new Person[Settings.THREADS][Settings.POP_SIZE];
		this.running = true;
		this.population = population;
	}

	public void run(){
		Worker worker = new Worker(population, 0, population.length, true);		
		SortTask sort = new SortTask(population, 0, population.length);
		
		Person best = new Person();
		Person oldBest = new Person();
		while (best.fitness != 100){
			worker.evaluate = true;
			
			worker.compute();
			sort.compute();

			best = population[0];
			
			if (!(best.dna.equals(oldBest.dna)) && best.fitness > oldBest.fitness){
				System.out.println("Best: " + best);
			}
			oldBest = best;

			System.arraycopy(population, 0, population, population.length/2, population.length/2);
			
			worker.reinitialize();
			worker.evaluate = false;
			worker.compute();

			worker.reinitialize();
			sort.reinitialize();
		}
	}

	private void printArray(Person[] array){
		for (int i = 0; i < array.length; i++){
			System.out.println(array[i]);
		}
	}
}