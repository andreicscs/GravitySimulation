package particle_gravity_simulation.graphics;

import java.io.IOException;

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



public class SimulationPanel extends StackPane {
	private boolean isMenuOpen;
    private final int FPS = 60;
    private final long TARGET_TIME = 1000000000L / FPS;
    private AnimationTimer animationTimer;
    private boolean start = true;
    static public double width;
    static public double height;
    private Canvas canvas;
    private GraphicsContext g2d;
    private SimulationWorld world;

    public SimulationPanel() throws IOException {
        canvas = new Canvas();
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
       
        canvas.setOnMousePressed(this::handleMouseClicked);
        canvas.setOnMouseReleased(this::handleMouseClicked);
        

        
        animationTimer = new AnimationTimer() {
	        @Override
	        public void handle(long currentTime) {
	            if (start) {
	            	width = getWidth();
	                height = getHeight();
	                
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
        
        
        
        
        
        //SimulationControls controls = new SimulationControls();
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("AA.fxml"));
        Parent controls=loader.load();


        Button hamburgerButton = new Button("✖");
        hamburgerButton.setStyle("-fx-background-color : rgba(100, 100, 100,0);\r\n"
        		+ "	-fx-background-radius: 40px; -fx-text-fill:white; -fx-font-size: 20pt ;");

        hamburgerButton.setMinWidth(30);
        hamburgerButton.setMinHeight(30);
        
        getChildren().addAll(controls, hamburgerButton);
        setAlignment(Pos.TOP_RIGHT);

        isMenuOpen=true;

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
                
                hamburgerButton.setText("✖");
                isMenuOpen = true;
            }
        });
         
         
        
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
        g2d.setFill(Color.rgb(50, 50, 50));
        g2d.fillRect(0, 0, width, height);
    }

    private void drawGame() {
        world.draw(g2d);
    }
    
    
    
    
    int x1=1;
	int y1=1;
	int x2=1;
	int y2=1;
    private void handleMouseClicked(MouseEvent e) {
    	
    	//spawnare particelle solo con il tasto primario
    	if(e.getButton()==MouseButton.PRIMARY) {    		
    		if (e.isPrimaryButtonDown()) {		
    			//punto iniziale (mouse down)
    			x1=(int)e.getX();
    			y1=(int)e.getY();

    			//spawno una stationary particle temporaneamente, per non farla muovere mentre gli si da la froza iniziale
        		world.addP((int)e.getX(), (int)e.getY(),false); 
        		
            }
    		if (!e.isPrimaryButtonDown()) {
    			//punto finale(mouse released)
            	x2=(int)e.getX();
            	y2=(int)e.getY();
            	
            	//rimuovo la particella temporanea
            	world.removeLastP();
            	//ne spawno un'altra prendendo ismovable dal controlpanel
            	world.addP(x1, y1,SimulationControls.isMovable);
            	//e gli do la spinta iniziale 
            	world.calcInitForce(x1, y1, x2, y2);
            	
            }
    	}
    }
}



