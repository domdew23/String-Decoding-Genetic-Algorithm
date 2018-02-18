import java.util.Random;
import java.util.Arrays;
public class main {
	public static void main(String[] args){

		Job job = new Job(initPopulation());
		long start = System.currentTimeMillis();
		job.start();
		try { job.join(); } catch (InterruptedException e){}
		
		long end = System.currentTimeMillis();
		System.out.println("Time taken: {" + ((end - start)) + "} miliseconds");
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