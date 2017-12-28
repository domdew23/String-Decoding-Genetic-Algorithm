class Person implements Comparable<Person> {

	String dna;
	int fitness;
	
	Person(String dna){
		this.dna = dna;
	}

	Person (){
		this.dna = "";
		this.fitness = 0;
	}

	public void set_fitness(int fitness){
		this.fitness = fitness;
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
