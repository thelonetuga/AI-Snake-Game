package snake.snakeAI.nn;

import snake.*;

import java.awt.Color;

public class SnakeAIAgent extends SnakeAgent {
   
    final private int inputLayerSize;
    final private int hiddenLayerSize;
    final private int outputLayerSize;

    /**
     * Network inputs array.
     */
    final private int[] inputs;
    /**
     * Hiddden layer weights.
     */
    final private double[][] w1;
    /**
     * Output layer weights.
     */
    final private double[][] w2;
    /**
     * Hidden layer activation values.
     */
    final private double[] hiddenLayerOutput;
    /**
     * Output layer activation values.
     */
    final private double[] output;

    public SnakeAIAgent(
            Cell cell,
            int inputLayerSize,
            int hiddenLayerSize,
            int outputLayerSize, Environment environment) {
        super(cell, Color.BLUE, environment);
        this.inputLayerSize = inputLayerSize;
        this.hiddenLayerSize = hiddenLayerSize;
        this.outputLayerSize = outputLayerSize;
        inputs = new int[inputLayerSize];
        inputs[inputs.length - 1] = -1; //bias entry
        w1 = new double[inputLayerSize][hiddenLayerSize]; // the bias entry for the hidden layer neurons is already counted in inputLayerSize variable
        w2 = new double[hiddenLayerSize + 1][outputLayerSize]; // + 1 due to the bias entry for the output neurons
        hiddenLayerOutput = new double[hiddenLayerSize + 1];
        hiddenLayerOutput[hiddenLayerSize] = -1; // the bias entry for the output neurons
        output = new double[outputLayerSize];
    }


    /**
     * Initializes the network's weights
     * 
     * @param weights vector of weights comming from the individual.
     */
    public void setWeights(double[] weights) {
        int w = 0;
        //percorrer os pesos w1
        for (int i = 0; i < inputLayerSize; i++) {
            for (int j = 0; j < hiddenLayerSize; j++) {
                w1[i][j] = weights[w++];
            }
        }
        //percorrer os pesos w2
        for (int i = 0; i < hiddenLayerSize+1; i++) {
            for (int j = 0; j < outputLayerSize; j++) {
                w2[i][j] = weights[w++];
            }
        }

    }
    
    /**
     * Computes the output of the network for the inputs saved in the class
     * vector "inputs".
     *
     */
    private void forwardPropagation() {
        float soma;
        for (int i = 0; i < hiddenLayerSize; i++) {
            soma=0;
            for (int j = 0; j < inputLayerSize; j++) {
                soma+=inputs[j]*w1[j][i];
            }
            hiddenLayerOutput[i]=sigmoide(soma);
        }

        for (int i = 0; i < outputLayerSize; i++) {
            soma=0;
            for (int j = 0; j < hiddenLayerSize+1; j++) {
                soma+=hiddenLayerOutput[j]*w2[j][i];
            }
            output[i]=sigmoide(soma);
        }
    }

    public double sigmoide(float soma){
        return 1/(1+Math.pow(Math.E, -soma));
    }

    @Override
    protected Action decide(Perception perception)  {
        Cell north = perception.getN();
        Cell east = perception.getE();
        Cell south = perception.getS();
        Cell west = perception.getW();
        Food food = perception.getF();


        int lineCell = cell.getLine();
        int columnCell = cell.getColumn();
        int lineFood = food.getCell().getLine();
        int columnFood = food.getCell().getColumn();


        if (north != null && !north.hasAgent() && !north.hasTail() && lineCell > lineFood) {
            inputs[0] = 1;
        } else {
            inputs[0] = 0;
        }
        if (south != null && !south.hasAgent() && !south.hasTail() && lineCell < lineFood) {
            inputs[1] = 1;
        } else {
            inputs[1] = 0;
        }
        if (west != null && !west.hasAgent() && !west.hasTail() && columnCell > columnFood) {
            inputs[2] = 1;
        } else {
            inputs[2] = 0;
        }
        if (east != null && !east.hasAgent() && !east.hasTail() && columnCell < columnFood) {
            inputs[3] = 1;
        } else {
            inputs[3] = 0;
        }


        if (south != null && !south.hasTail() && !south.hasAgent()) {
                inputs[4] = 0;
        } else {
                inputs[4] = 1;
                if (east != null && !east.hasTail() && !east.hasAgent()) {
                    inputs[5] = 0;
                } else {
                    inputs[5] = 1;
                    if (west != null && !west.hasTail() && !west.hasAgent()) {
                        inputs[6] = 0;
                    } else {
                        inputs[6] = 1;
                        if (north != null && !north.hasTail() && !north.hasAgent()) {
                            inputs[7] = 0;
                        } else {
                            inputs[7] = 1;
                        }
                    }

                }
        }

        forwardPropagation();
        double max = output[0];
        int pos = 0;
        for (int i = 0; i < output.length ; i++) {
            if (output[i] > max){
                max = output[i];
                pos = i;
            }
        }

        if ( pos == 0 ) {
            return Action.NORTH;
        } else if (pos == 1) {
            return Action.SOUTH;
        } else if (pos == 2 ) {
            return Action.EAST;
        } else if (pos == 3 ) {
            return Action.WEST;
        }

        return null;
    }
}
