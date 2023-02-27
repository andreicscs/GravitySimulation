package particle_gravity_simulation.graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyFrame extends Application {

    //private Sliders sliders;
    public SimulationPanel gamePanel;
    public static double panelHeight;
    public static double panelWidth;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gamePanel = new SimulationPanel();
        //sliders = new Sliders();

        primaryStage.setTitle("Particle's gravity Simulation");
        primaryStage.setScene(new Scene(gamePanel, 1366, 768));
        primaryStage.setResizable(false);
        primaryStage.show();

        gamePanel.start();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}