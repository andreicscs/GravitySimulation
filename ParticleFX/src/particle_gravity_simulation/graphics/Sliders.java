package particle_gravity_simulation.graphics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;

public class Sliders extends JPanel implements ChangeListener, ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JSlider ForceGsl;
	private JButton clear;
	private JCheckBox options;
	private JCheckBox ismovable;
	private JComboBox<String> ispositive;
	private JSlider masssl;
	public static int Gslider;
	public static boolean isMovable;
	public static int isPositive;
	public static int Masssl;
	
	Sliders(){
		setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		
		
		//G slider
		Gslider=50;
		ForceGsl=new JSlider(0,100,50);//(minimo, massimo, default)
		ForceGsl.setPreferredSize(new Dimension(200,50));
		ForceGsl.setBorder(BorderFactory.createTitledBorder("G constant"));
		ForceGsl.setPaintLabels(true);
		ForceGsl.setMajorTickSpacing(10);
		ForceGsl.setMinorTickSpacing(2);
		ForceGsl.setFont(new Font("Segoe UI",Font.PLAIN,10));
		ForceGsl.addChangeListener(this); //aggiungo change listener, fa qualcosa quando vede un cambiamento
		ForceGsl.setSnapToTicks(true);
		//aggiungo al pannello
		add(ForceGsl);
		
		
		//particle option CheckBox
		options=new JCheckBox();
		options.setBorder(BorderFactory.createTitledBorder(""));
		options.setBorderPainted(true);
		options.setText("spawn options");
		options.setPreferredSize(new Dimension(100,30));
		options.setFont(new Font("Segoe UI",Font.PLAIN,12));
		options.setFocusable(false);
		options.addActionListener(this);
		add(options);
		
		
		//is movable CheckBox
		ismovable=new JCheckBox();
		ismovable.setText("movable");
		ismovable.setFont(new Font("Segoe UI",Font.PLAIN,15));
		ismovable.setVisible(false);
		ismovable.setFocusable(false);
		ismovable.addActionListener(this);
		add(ismovable);
		
		
		//da sistemare la tendina drop down che viene messa sotto la simulazione
		//is positive ComboBox
		String[] ispositiveOptions= {"negative","neutral","positive"};
 		ispositive=new JComboBox<String>(ispositiveOptions);
		ispositive.setFont(new Font("Segoe UI",Font.PLAIN,15));
		ispositive.setVisible(false);
		ispositive.setFocusable(false);
		ispositive.addActionListener(this);
		ispositive.setSelectedIndex(0);
		//ispositive.alwaysOnTop();
		add(ispositive);
		
		
		//mass JSlider
		Masssl=15;
		masssl=new JSlider(0,100,15);
		masssl.setPreferredSize(new Dimension(200,50));
		masssl.setBorder(BorderFactory.createTitledBorder("Mass"));
		masssl.setPaintLabels(true);
		masssl.setMajorTickSpacing(10);
		masssl.setMinorTickSpacing(2);
		masssl.setFont(new Font("Segoe UI",Font.PLAIN,10));
		masssl.addChangeListener(this);
		masssl.setSnapToTicks(true);
		masssl.setVisible(false);
		add(masssl);
	}
	
	
	
	@Override
	public void stateChanged(ChangeEvent e) {//per sliders..
		if(e.getSource()==ForceGsl) {
			Gslider = ForceGsl.getValue();
		}
		else if(e.getSource()==masssl) {
			Masssl = masssl.getValue();
		}
		
		
	}
	@Override
    public void actionPerformed(ActionEvent e) {//per bottoni, checkbox...
		if(e.getSource()==options) {
			if(options.isSelected()) {
				masssl.setVisible(true);
				ispositive.setVisible(true);
				ismovable.setVisible(true);
			}
			else {
				masssl.setVisible(false);
				ispositive.setVisible(false);
				ismovable.setVisible(false);
			}
		}
		else if(e.getSource()==ispositive) {
			if(ispositive.getSelectedItem()=="positive")
				isPositive=1;
			else if (ispositive.getSelectedItem()=="negative")
				isPositive=-1;
			else if (ispositive.getSelectedItem()=="neutral")
				isPositive=0;
		}
		else if(e.getSource()==ismovable) {
			if(ismovable.isSelected())
				isMovable=true;
			else
				isMovable=false;
		}
		else if(e.getSource()==clear) {
		}
	}
}
