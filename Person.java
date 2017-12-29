public class Person implements Comparable<Person> {

	String dna;
	int fitness;
	
	public Person(String dna){
		this.dna = dna;
	}

	public Person(){
		this.dna = "";
		this.fitness = 0;
	}

	public int compareTo(Person other){
		if (other.fitness == fitness) return 0;
		if (other.fitness > fitness) return 1;
		return -1;
	}

	public String toString(){
		return dna + " | " + fitness;
	}

}
