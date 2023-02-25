package particle_gravity_simulation.graphics;

import java.awt.*;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import particle_gravity_simulation.objects.SimulationWorld;

public class SimulationPanel extends JComponent implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	//simulation FPS limit
	private final int FPS=60;
	private final int TARGET_TIME=1000000000/FPS; //convert fps in nanoseconds
	private Thread updateThread; //thread secondario per i cicli infiniti
	boolean start = true;//simulazione attiva
	static private int width;//larghezza e grandezza pannello
	static private int height;
	public static Graphics2D g2d; 
	private BufferedImage image;
	SimulationWorld world;
	
	public static int getwidth() {
		return width;
	}
	public static int getheight() {
		return height;
	}
	
	
	
	public void start() {
		width=getWidth();
		height=getHeight();
		world = new SimulationWorld();
		//grafica
		image=new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
		g2d=image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//aggiungo il mouselistener al pannello
		addMouseListener(this); 
		
		//creo il thread dedicato ai calcoli e al disegno a schermo
		updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(start) {
					long startTime = System.nanoTime(); // tempo del sistema in nanosecondi
					
					updateWorld();
					drawBackgound();
					drawgame();
					render();
					
					long time = System.nanoTime()-startTime; // calcolo il tempo passato
					
					//se il tempo passato è meno di quello che dovrebbe essere (fps)
					if(time < TARGET_TIME) {
						//allora mando il thread in sleep quanto necessario per rifare la differenza
						long sleeptime=(TARGET_TIME-time)/1000000; //convert in milliseconds
						sleep(sleeptime);
					}
				}
			}
		});
		//lo avvio
		updateThread.start();
		
	}
	private void updateWorld() {
		world.update();//aggiorno il mondo di simulazione
	}
	private void drawBackgound() {
		g2d.setColor(new Color(10,10,10));//disegno sfondo nero
		g2d.fillRect(0, 0, width, height);//con grandezza del pannello
	}
	private void drawgame() {
		world.draw(g2d);//disegno tutte le particelle
	}
	private void render() {
		//rendero la buffered image sullo schermo
		Graphics g=getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
	}
	
	private void sleep(long time) {
		//il thread potrebbe essere interrotto mentre è in sleep
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//quando il mouseListener rileva un click aggiunge una particella
		//al mondo alle coordinate del click
	    world.addP(e.getX(),e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
