package particle_gravity_simulation.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import particle_gravity_simulation.objects.SimulationWorld;

public class SimulationPanel extends StackPane {
    
    private final int FPS = 60;
    private final long TARGET_TIME = 1000000000L / FPS;
    private AnimationTimer animationTimer;
    private boolean start = true;
    static public double width;
    static public double height;
    private Canvas canvas;
    private GraphicsContext g2d;
    private SimulationWorld world;

    public SimulationPanel() {
        canvas = new Canvas();
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        canvas.setOnMouseClicked(this::handleMouseClicked);
        animationTimer = new AnimationTimer() {


            @Override
            public void handle(long currentTime) {
                if (start) {
                    long startTime=System.currentTimeMillis();
                    	updateWorld(); 
                    	drawBackground();
                    	
                    	drawGame();              
                    	
                    	
                        
                    	long lastTime = System.currentTimeMillis();
                    	
                        long elapsedTime = lastTime-startTime;
                    if (elapsedTime < TARGET_TIME) {
						long sleeptime=(TARGET_TIME-elapsedTime)/1000000; //convert in milliseconds
						sleep(sleeptime);
                    }
                }
            }
        };
    }

    
    
    private void sleep(long time) {
		//il thread potrebbe essere interrotto mentre è in sleep
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
	}
    
    
    public void start() {
        width = getWidth();
        height = getHeight();
        world = new SimulationWorld();
        g2d = canvas.getGraphicsContext2D();
        g2d.setLineWidth(1.0);
        g2d.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        g2d.setLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        animationTimer.start();
    }

    private void updateWorld() {
        world.update();
    }

    private void drawBackground() {
        g2d.setFill(Color.rgb(10, 10, 10));
        g2d.fillRect(0, 0, width, height);
    }

    private void drawGame() {
        world.draw(g2d);
    }


    private void handleMouseClicked(MouseEvent e) {
        world.addP((int)e.getX(), (int)e.getY());
    }

}