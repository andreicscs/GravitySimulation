package particle_gravity_simulation.graphics;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import particle_gravity_simulation.objects.SimulationWorld;

public class SimulationPanel extends StackPane {
    private boolean isMenuOpen;
    static public double width;
    static public double height;
    
    private Canvas canvas;
    private static GraphicsContext g2d;
    static public SimulationWorld world;
    
    private boolean wasMousePrimaryDown=false;
    
    private boolean start = true;
    private AnimationTimer animationTimer1;
    Thread thread1;
    private AnimationTimer animationTimer2;
    
    public static final Semaphore synchronizationSemaphoreCalculating = new Semaphore(1);
    public static final Semaphore synchronizationSemaphoreDrawing = new Semaphore(0);
    final double FIXED_DT = 1.0 / 60.0; // 60 updates per second
    final double PHYSICS_DT = FIXED_DT*100;
    
    public SimulationPanel() throws IOException {
        canvas = new Canvas();
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        
        //Calculating Loop
        thread1 = new Thread(new Runnable() {
	            @Override
				public void run() {
	            	long lastTime = System.nanoTime();
	                double accumulator = 0.0;
	                
	            	while (start) {
	            		width=getWidth();
	                	height=getHeight();
	                	
	                	try {
	                		synchronizationSemaphoreCalculating.acquire();
	                		
	                		long now = System.nanoTime();
	                		double frameTime  = (now - lastTime) / 1e9; // seconds
	                		lastTime = now;
	                        accumulator += frameTime;

	                        // run multiple fixed updates if needed to mantain fixed simulation speed
	                        while (accumulator >= FIXED_DT) {
	                            world.update(PHYSICS_DT);
	                            accumulator -= FIXED_DT;
	                        }
	                	} catch (InterruptedException e) {
	                		System.out.println(e);
	                		e.printStackTrace();
	                	}finally {
	                		synchronizationSemaphoreDrawing.release();
	                    }
	                }
	            }
           });
        
        
       
        //Drawing Loop
        animationTimer1 = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if (start) {
                    try {
                    	synchronizationSemaphoreDrawing.acquire();
                    	drawBackground();
                        drawSimulation(g2d);
                        
                	} catch (InterruptedException e) {
                		System.out.println(e);
                		e.printStackTrace();
                	}finally {
                		synchronizationSemaphoreCalculating.release();
                    }
                }
            }
        };
        
           
           
        //carico l'fxml file, che contiene le impostazioni
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("AA.fxml"));
        Parent controls = loader.load();
        
        Button hamburgerButton = new Button("✕");
        hamburgerButton.setStyle(
            "-fx-background-color: rgba(100, 100, 100,0.2);" +
            "-fx-background-radius: 20px;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 20pt;" +
            "-fx-cursor: hand;" +
            "-fx-alignment: center;"
        );
        hamburgerButton.setPrefSize(70, 70);

        
        getChildren().addAll(controls, hamburgerButton);
        setAlignment(Pos.TOP_RIGHT);
        
        isMenuOpen = true;
        TranslateTransition menuTransition = new TranslateTransition(Duration.millis(250), controls);
        menuTransition.setInterpolator(Interpolator.EASE_BOTH);

        RotateTransition buttonRotate = new RotateTransition(Duration.millis(250), hamburgerButton);

        
        hamburgerButton.setOnAction(event -> {
            hamburgerButton.setDisable(true); // prevent double clicks

            if (isMenuOpen) {
                // close menu
                menuTransition.setToX(400); 
                menuTransition.setOnFinished(e -> {
                    hamburgerButton.setText("|||"); // change text after animation
                    hamburgerButton.setDisable(false);
                });
                menuTransition.play();
                
                buttonRotate.setByAngle(90);
                buttonRotate.play();
                isMenuOpen = false;
            } else {
                // open menu
                menuTransition.setToX(0); 
                menuTransition.setOnFinished(e -> {
                    hamburgerButton.setText("✕"); // revert text after animation
                    hamburgerButton.setDisable(false);
                });
                menuTransition.play();
                
                buttonRotate.setByAngle(90);
                buttonRotate.play();
                isMenuOpen = true;
            }
        });
    }
    
    public void start() {
        world = new SimulationWorld();
        g2d = canvas.getGraphicsContext2D();
        g2d.setGlobalAlpha(0.8);
        animationTimer1.start();
        thread1.start();
    }

    private void drawBackground() {
		g2d.setFill(Color.rgb(50, 50, 50));
        g2d.fillRect(0, 0, width, height);

    }
    
    private void drawSimulation(GraphicsContext g2d) {
        world.draw(g2d);
    }
    
	
	static public void drawCircle(double x,double y, double r) {
			//bisogna dare un colore per forza altrimenti la preview della prima particella non viene disegnata
			//disegno il cerchio con il colore dato dal simulation controls
			if(SimulationControls.isPositiveSelected>0)
				g2d.setFill(Color.web("#FF3B30"));
			else if(SimulationControls.isPositiveSelected<0)
				g2d.setFill(Color.web("#007AFF"));
			else
				g2d.setFill(Color.web("#8E8E93"));
			
		    g2d.fillOval(x, y, r, r);

	}
    
    
	//handle mouse actions//
	
    //pos mouse premuto
    private double x1=0;
    private double y1=0;
    
    //pos mouse mosso in seguito essere premuto
    private double x2=0;
    private double y2=0;
    
    //calcolo distanza fra i due punti
    double risy;
	double risx;
	double dist;
	
    private void handleMousePressed(MouseEvent event){
    	if (event.getButton()==MouseButton.PRIMARY){
    		wasMousePrimaryDown=true;
    		
            //punto iniziale (mouse down per la prima volta)
            x1=event.getX();
            y1=event.getY();
            x2=event.getX();
            y2=event.getY();
            
            //calcolo distanza fra i due punti
            risy= Math.abs(y1-y2);
			risx= Math.abs(x1-x2);
			dist= Math.sqrt((Math.pow(risx, 2)+Math.pow(risy, 2)));
			
			//animation timer per calcolo e disegno traiettoria..
			//da implementare un thread da dedicare al calcolo della traiettoria
            animationTimer2 = new AnimationTimer() {
	            @Override
	            public void handle(long currentTime) {
	            	
	            	
	    			//se la distanza >5 (rilevante) calcolo la traiettoria
	    			if(dist>5) {
	    				
		                if (wasMousePrimaryDown) {
		                	world.trajectoryPreview(x1, y1, x2, y2, g2d, PHYSICS_DT);
		                }else {
		                	this.stop();
		                }
	    			}
	    			if (wasMousePrimaryDown) {
	            		//disegno cerchio temporaneo al posto della particella
	                	drawCircle(x1,y1,10);
	            	}
	            }
	        };
	        animationTimer2.start();
    	}
    }
    
    private void handleMouseDragged(MouseEvent event) {
    	if (wasMousePrimaryDown) {
    		//posizione corrente del mouse (mentre rimane premuto)
	    	x2=event.getX();
	        y2=event.getY();
	        
	        //calcolo distanza fra i due punti
            risy= Math.abs(y1-y2);
			risx= Math.abs(x1-x2);
			dist= Math.sqrt((Math.pow(risx, 2)+Math.pow(risy, 2)));
    	}
    }
	
	private void handleMouseReleased(MouseEvent e) {
    	if (wasMousePrimaryDown&& e.getButton()==MouseButton.PRIMARY ) {
    		wasMousePrimaryDown=false;
    		
	    	//punto finale(mouse released)
		    x2=e.getX();
		    y2=e.getY();

		    //spawno la particella prendendo ismovable dal controlpanel 
		    world.addP(x1, y1,SimulationControls.isMovable);
		   
			//se la distanza è maggiore di 5, (se la distanza è abbastanza rilevante) allora dò la spinta iniziale
		    if(dist>5) {
		    	world.calcInitForce(x1, y1, x2, y2, PHYSICS_DT);
		    }
    	}
	}
	
}



