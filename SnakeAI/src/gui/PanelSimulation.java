package gui;

import snake.Environment;
import snake.EnvironmentListener;
import snake.snakeAI.nn.SnakeAIAgent;
import snake.snakeAI.nn.SnakeAIAgent2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PanelSimulation extends JPanel implements EnvironmentListener {

    public static final int PANEL_SIZE = 250;
    public static final int CELL_SIZE = 20;
    public static final int GRID_TO_PANEL_GAP = 20;
    MainFrame mainFrame;
    private Environment environment;
    private Image image;
    JPanel environmentPanel = new JPanel();
    final JButton buttonSimulate = new JButton("Simulate");

    public PanelSimulation(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        environmentPanel.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setLayout(new BorderLayout());

        add(environmentPanel, java.awt.BorderLayout.NORTH);
        add(buttonSimulate, java.awt.BorderLayout.SOUTH);
        buttonSimulate.addActionListener(new SimulationPanel_jButtonSimulate_actionAdapter(this));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Snake Automatus"),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setJButtonSimulateEnabled(boolean enabled) {
        buttonSimulate.setEnabled(enabled);
    }

    public void jButtonSimulate_actionPerformed(ActionEvent e)  {

        environment = mainFrame.getProblem().getEnvironment();
        environment.addEnvironmentListener(this);

        buildImage(environment);

        final PanelSimulation simulationPanel = this;

        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground()  {
               int type = environment.getType();
                try {
                    int environmentSimulations = mainFrame.getProblem().getNumEvironmentSimulations();
                    if (type < 2 ){
                        for (int i = 0; i < environmentSimulations; i++) {
                            environment.initialize(i);
                            environmentUpdated();
                            environment.simulate();
                        }
                    }else if(type == 2){
                        for (int i = 0; i < environmentSimulations; i++) {
                            environment.initialize(i);
                            if(mainFrame.getBestInRun() != null ){
                                SnakeAIAgent aiAgent = (SnakeAIAgent) environment.getAgents().get(0);
                                aiAgent.setWeights(mainFrame.getBestInRun().getGenome());
                            }
                            environmentUpdated();
                            environment.simulateAI();
                        }
                    }else if(type == 3){
                        for (int i = 0; i < environmentSimulations; i++) {
                            environment.initialize(i);
                            if(mainFrame.getBestInRun() != null ){
                                SnakeAIAgent2 aiAgent2 = (SnakeAIAgent2) environment.getAgents().get(0);
                                aiAgent2.setWeights(mainFrame.getBestInRun().getGenome());
                            }
                            environmentUpdated();
                            environment.simulateAI();
                        }
                    }else if(type == 4){
                            for (int i = 0; i < environmentSimulations; i++) {
                                environment.initialize(i);
                                if(mainFrame.getBestInRun() != null ){
                                    SnakeAIAgent aiAgent = (SnakeAIAgent) environment.getAgents().get(0);
                                    SnakeAIAgent aiAgent2 = (SnakeAIAgent) environment.getAgents().get(1);
                                    aiAgent.setWeights(mainFrame.getBestInRun().getGenome());
                                    aiAgent2.setWeights(mainFrame.getBestInRun().getGenome());
                                }
                                environmentUpdated();
                                environment.simulateAI();
                            }
                    }else if(type == 5){
                        for (int i = 0; i < environmentSimulations; i++) {
                            environment.initialize(i);
                            if(mainFrame.getBestInRun() != null ){
                                SnakeAIAgent aiAgent = (SnakeAIAgent) environment.getAgents().get(0);
                                SnakeAIAgent2 aiAgent2 = (SnakeAIAgent2) environment.getAgents().get(1);

                                double[] weights1 = new double[9*10+(10+1)*4];
                                double[] weights2 = new double[13*15+(15+1)*8];
                                double[] genome = mainFrame.getBestInRun().getGenome();
                                System.arraycopy(genome, 0, weights1,0,weights1.length);
                                System.arraycopy(genome, weights1.length, weights2,0,weights2.length);
                                aiAgent.setWeights(weights1);
                                aiAgent2.setWeights(weights2);
                            }
                            environmentUpdated();
                            environment.simulateAI();
                        }
                    }



                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return null;
            }

            @Override
            public void done() {
                environment.removeEnvironmentListener(simulationPanel);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignore) {
                }

            }
        };
        worker.execute();
    }

    public void buildImage(Environment environment) {
        image = new BufferedImage(
                environment.getSize() * CELL_SIZE + 1,
                environment.getSize() * CELL_SIZE + 1,
                BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void environmentUpdated() {
        int n = environment.getSize();
        Graphics g = image.getGraphics();

        //Fill the cells color
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                g.setColor(environment.getCellColor(y, x));
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        //Draw the grid lines
        g.setColor(Color.BLACK);
        for (int i = 0; i <= n; i++) {
            g.drawLine(0, i * CELL_SIZE, n * CELL_SIZE, i * CELL_SIZE);
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, n * CELL_SIZE);
        }

        g = environmentPanel.getGraphics();
        g.drawImage(image, GRID_TO_PANEL_GAP, GRID_TO_PANEL_GAP, null);

        try {
            Thread.sleep(50);
        } catch (InterruptedException ignore) {
        }
    }
}

//--------------------
class SimulationPanel_jButtonSimulate_actionAdapter implements ActionListener {

    final private PanelSimulation adaptee;

    SimulationPanel_jButtonSimulate_actionAdapter(PanelSimulation adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.jButtonSimulate_actionPerformed(e);
    }
}
