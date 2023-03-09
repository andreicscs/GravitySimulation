package particle_gravity_simulation.graphics;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MyFrame extends Application {

    //private Sliders sliders;
    public SimulationPanel gamePanel;
    public static double panelHeight;
    public static double panelWidth;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gamePanel = new SimulationPanel();
        //sliders = new Sliders();
        
		gamePanel.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
        primaryStage.setTitle("Particle's gravity Simulation");
        primaryStage.setScene(new Scene(gamePanel, 1366, 768));
        
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        gamePanel.start();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}