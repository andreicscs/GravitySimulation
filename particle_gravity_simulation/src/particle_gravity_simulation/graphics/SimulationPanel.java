package particle_gravity_simulation.graphics;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javafx.animation.AnimationTimer;
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
import particle_gravity_simulation.objects.WorldObject;

public class SimulationPanel extends StackPane {
    private boolean isMenuOpen;
    //getWidth() , getHeight(), non sono statici..
    static public double width;
    static public double height;
    
    private Canvas canvas;
    public static final Semaphore graphicsSemaphore = new Semaphore(1,true);
    private static GraphicsContext g2d;
    static public SimulationWorld world;
    
    private boolean wasMousePrimaryDown=false;
    
    private final int FPS = 60;
    private final long TARGET_TIME = 1000000000L / FPS;
    private boolean start = true;
    private AnimationTimer animationTimer1;
    Thread thread1;
    private AnimationTimer animationTimer2;
    
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
	            	while (start) {
	                	width=getWidth();
	                	height=getHeight();

	                    long startTime = System.currentTimeMillis();
	                    updateWorld();
	                    
	                    long lastTime = System.currentTimeMillis();
	                    long elapsedTime = lastTime - startTime;
	                    //se l'update e il rendering ci ha messo troppo poco tempo, aspetta il tempo rimanente per avere 60fps (FPS)
	                    if (elapsedTime < TARGET_TIME) {
	                        long sleepTime = (TARGET_TIME - elapsedTime) / 1000000; // convert in milliseconds
	                        sleep(sleepTime);
	                    }
	                }
	            }
           });
        
        
       
        //Drawing Loop
        animationTimer1 = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if (start) {


                    long startTime = System.currentTimeMillis();
                    drawBackground();
                    drawGame();
                    
                    long lastTime = System.currentTimeMillis();
                    long elapsedTime = lastTime - startTime;
                    //se l'update e il rendering ci ha messo troppo poco tempo, aspetta il tempo rimanente per avere 60fps (FPS)
                    if (elapsedTime < TARGET_TIME) {
                        long sleepTime = (TARGET_TIME - elapsedTime) / 1000000; // convert in milliseconds
                        sleep(sleepTime);
                    }
                }
            }
        };
        
           
           
        //carico l'fxml file, che contiene le impostazioni
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("AA.fxml"));
        Parent controls = loader.load();
        
        //stile bottone apertura/chiusura menu
        Button hamburgerButton = new Button("ⓧ");
        hamburgerButton.setStyle("-fx-background-color : rgba(100, 100, 100,0.2);\r\n"
                + "	-fx-background-radius: 20px; -fx-text-fill:white; -fx-font-size: 20pt; -fx-cursor: hand;");
        //hamburgerButton.setMaxWidth(60);
        //hamburgerButton.setMaxHeight(30);

        getChildren().addAll(controls, hamburgerButton);
        setAlignment(Pos.TOP_RIGHT);
        
        //all'inizio è aperto
        isMenuOpen = true;
        hamburgerButton.setOnAction(event -> {
            if (isMenuOpen) {
                // Chiudi il menu
                TranslateTransition transition = new TranslateTransition(Duration.millis(200), controls);
                transition.setToX(400);
                transition.play();

                hamburgerButton.setText("≡");
                isMenuOpen = false;
            } else {
                // Apri il menu
                TranslateTransition transition = new TranslateTransition(Duration.millis(200), controls);
                transition.setToX(0);
                transition.play();

                hamburgerButton.setText("ⓧ");
                isMenuOpen = true;
            }
        });
    }

    
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
    
    public void start() {
        world = new SimulationWorld();
        g2d = canvas.getGraphicsContext2D();
        animationTimer1.start();
        thread1.start();
    }

    private void updateWorld() {
        world.update();
    }

    private void drawBackground() {
			try {
				graphicsSemaphore.acquire();
				g2d.setFill(Color.rgb(50, 50, 50));
		        g2d.fillRect(0, 0, width, height);
			} catch (InterruptedException e) {
				System.out.println(e);
				e.printStackTrace();
			}finally {
				graphicsSemaphore.release();
		    }
    }
    
    private void drawGame() {
        world.draw();
    }
    
    static public void draw(WorldObject toDraw) {
		try {
			graphicsSemaphore.acquire();
	    	toDraw.draw(g2d);
			if(SimulationControls.isTrailOn) {
				toDraw.drawTrail(g2d);
			}
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}finally {
			graphicsSemaphore.release();
        }
	}
	
	static public void drawCircle(double x,double y,int r) {
		try {
			graphicsSemaphore.acquire();
			
			//bisogna dare un colore per forza altrimenti la preview della prima particella non viene disegnata
			//disegno il cerchio con il colore dato dal simulation controls
			if(SimulationControls.isPositiveSelected>0)
				g2d.setFill(Color.RED);
			else if(SimulationControls.isPositiveSelected<0)
				g2d.setFill(Color.BLUE);
			else
				g2d.setFill(Color.GRAY);
			
		    g2d.fillOval(x, y, r, r);
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}finally {
			graphicsSemaphore.release();
	    }
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
	            	
	            	if (wasMousePrimaryDown) {
	            		//disegno cerchio temporaneo al posto della particella
	                	drawCircle(x1,y1,10);
	            	}
	    			//se la distanza >5 (rilevante) calcolo la traiettoria
	    			if(dist>5) {
	    				
		                if (wasMousePrimaryDown) {
		                	world.trajectoryPreview(x1, y1, x2, y2, g2d);
		                }else {
		                	this.stop();
		                }
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
		    	world.calcInitForce(x1, y1, x2, y2);
		    }
    	}
	}
	
}



