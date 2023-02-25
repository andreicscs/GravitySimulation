package particle_gravity_simulation.graphics;



import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

//finestra grafica
public class MyFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	Sliders sliders;
	SimulationPanel gamePanel;
	
	public MyFrame(){

		gamePanel = new SimulationPanel();
		sliders = new Sliders();
		
		//impostazioni della finestra grafica
		this.setTitle("Particle's gravity Simulation");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1366,768);
		this.setVisible(true);
		setResizable(false);
		
		//aggiungo alla finestra grafica i due pannelli
		add(gamePanel);
		add(sliders,BorderLayout.PAGE_START);
		
		//cosa fare quando si apre la finestra grafica
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowOpened(WindowEvent e){
				gamePanel.start();
			}
		});
	}
}