import java.util.Random;
import java.util.Arrays;
public class main {
	public static void main(String[] args){
		Person[] population = initPopulation();
		Job job = new Job(population);
		job.start();

		Person best = new Person();
		Person oldBest = new Person();

		while (best.fitness != 100){
			best = job.population[0];
			if (!(best.dna.equals(oldBest.dna)) && best.fitness > oldBest.fitness){
				System.out.println("Best: " + best);
			}
			oldBest = best;
		}
		System.exit(0);
	}

	private static Person[] initPopulation(){
		Person[] population = new Person[Settings.POP_SIZE];
		Random r = new Random();
		String dna;
		int index = 0;
		for (int i = 0; i < Settings.POP_SIZE; i++){
			dna = "";
			for (int j = 0; j < Settings.GOAL_SIZE; j++){
				index = r.nextInt(Settings.CHARS.length());
				dna += Settings.CHARS.charAt(index);
			}
			Person p = new Person(dna);
			population[i] = p;
		}
		return population;		
	}
}