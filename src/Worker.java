import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class Worker extends RecursiveAction {
	private Person[] population;
	private int lo,hi;
	public boolean evaluate;

	public Worker(Person[] population, int lo, int hi, boolean evaluate){
		this.population = population;
		this.lo = lo;
		this.hi = hi;
		this.evaluate = evaluate;
	}

	public void compute(){
		if ((hi - lo) <= Settings.THRESHOLD){
			if (evaluate){
				evaluateFitness();
			} else {
				createMatingPool();	
			}
		} else {
			int mid = (hi + lo) / 2;
			Worker left = new Worker(population, lo, mid, evaluate);
			Worker right = new Worker(population, mid, hi, evaluate);
			invokeAll(left, right);
		}
	}

	private void evaluateFitness(){
		int fitness;
		int count;
		for (int i = lo; i < hi; i++){
			count = 0;
			fitness = 0;
			for (int j = 0; j < Settings.GOAL_SIZE; j++){
				if (population[i].dna.charAt(j) == Settings.GOAL.charAt(j)){
					count++;
				}
			}
			double f = ((double)count/(double)Settings.GOAL_SIZE);
			fitness = (int) Math.round(f * 100);
			population[i].fitness = fitness;
		}
	}

	private void createMatingPool(){
		for (int i = lo; i < hi; i++){
			Person one = population[ThreadLocalRandom.current().nextInt(lo, hi)];
			Person two = population[ThreadLocalRandom.current().nextInt(lo, hi)];
			
			population[i] = combine(one, two);
		}
	}

	private Person combine(Person one, Person two){
		Person child;
		String dna = "";

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
		char[] tmpDna = dna.toCharArray();
		for (int i = 0; i < Settings.GOAL_SIZE; i++){
			if (ThreadLocalRandom.current().nextDouble(0,1) <= Settings.MUTATION_RATE){
				char c = Settings.CHARS.charAt(ThreadLocalRandom.current().nextInt(0, Settings.CHARS.length()));
				tmpDna[i] = c; 
			}
		}
		String mutated = new String(tmpDna);
		return mutated;
	}
}