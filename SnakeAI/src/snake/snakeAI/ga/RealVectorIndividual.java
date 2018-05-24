package snake.snakeAI.ga;


import java.util.Random;

public abstract class RealVectorIndividual <P extends Problem, I extends RealVectorIndividual> extends Individual<P, I>{
    protected double[] genome;
    private Random rand;
    //o genoma vai ser um array de double com os pesos que serao colocados nas sinapses da RN
    public RealVectorIndividual(P problem, int size, Random random) {
        super(problem);
        this.rand = random;
        genome = new double[size];
        for (int g = 0; g < genome.length; g++) {
            genome[g] = random.nextDouble()*2-1;
        }
    }

    public RealVectorIndividual(RealVectorIndividual<P, I> original) {
        super(original);
        this.genome = new double[original.genome.length];
        genome = original.genome.clone();
    }
    
    @Override
    public int getNumGenes() {
        return genome.length;
    }
    
    public double getGene(int index) {
        return genome[index];
    }
    
    public void setGene(int index, double newValue) {
        genome[index] = newValue;
    }

    @Override
    public void swapGenes(RealVectorIndividual other, int index) {
        double aux = genome[index];
        genome[index] = other.genome[index];
        other.genome[index] = aux;
    }
}
