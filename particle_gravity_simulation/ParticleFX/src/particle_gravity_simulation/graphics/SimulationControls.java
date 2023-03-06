package particle_gravity_simulation.graphics;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import particle_gravity_simulation.objects.SimulationWorld;

public class SimulationControls implements Initializable{
	@FXML
	private ChoiceBox<String> isPositive;
	private String[] choices= {"positive","neutral","negative"};
	
	@FXML
	private Slider forceGsl;
	
	@FXML
	private Slider particleMassSl;
	
	public static int gSlider=15;
	public static boolean isMovable=true;
	public static int isPositiveSelected=1;
	public static int Masssl=15;
	public static boolean isTrailOn=true;
	public static boolean isCollisionOn=true;
	
	
	
	public void btIsMovable(ActionEvent event) {
		System.out.println("oke");
       	if(isMovable){
       		isMovable = false;
       	}else {
       		isMovable=true;
       	}
    }
	
	
	public void btIsTrailOn(ActionEvent event) {
       	if(isTrailOn){
       		isTrailOn = false;
       	}else {
       		isTrailOn=true;
       	}
    }
	
	public void btIsCollisionOn(ActionEvent event) {
       	if(isCollisionOn){
       		isCollisionOn = false;
       	}else {
       		isCollisionOn=true;
       	}
    }
	
	public void btClear(ActionEvent event) {
		SimulationWorld.clear();
    }


	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		isPositive.getItems().addAll(choices);
		isPositive.setOnAction(this::getIsPositiveChoice);
		
		forceGsl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gSlider = newValue.intValue();
            }
        });
		
		particleMassSl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            	Masssl = newValue.intValue();
            }
        });
		
	}
	
	

	
	
	public void getIsPositiveChoice(ActionEvent event) {
        String selectedItem = isPositive.getSelectionModel().getSelectedItem();
        if (selectedItem.equals("positive")) {
            isPositiveSelected = 1;
        } else if (selectedItem.equals("neutral")) {
            isPositiveSelected = 0;
        } else {
            isPositiveSelected = -1;
        }
    }
	
}
	
	/*
		// G slider
        gSlider = 15;

		ForceGsl.valueProperty().addListener(new ChangeListener<Number>() {
	            @Override
	            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	                gSlider = newValue.intValue();
	            }
	        });
		
		

		

		
		
		// is positive combobox
		isPositiveSelected = -1;

		ispositive.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedItem = ispositive.getSelectionModel().getSelectedItem();
                if (selectedItem.equals("positive")) {
                    isPositiveSelected = 1;
                } else if (selectedItem.equals("neutral")) {
                    isPositiveSelected = 0;
                } else {
                    isPositiveSelected = -1;
                }
            }
        });
		
		
		
		// mass slider
		Masssl=15;
		masssl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            	Masssl = newValue.intValue();
            }
        });
	
		
		
		//btIsMovable toggle button 
		btIsMovable.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	isMovable = btIsMovable.isSelected();
	        }
	    });
		
		
		
		//btIsTrailOn toggle button
		isTrailOn=true;
		btIsTrailOn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	isTrailOn = !btIsTrailOn.isSelected();
            }
        });
		
		
		//btIsCollisionOn toggle button
		isCollisionOn=true;
		btIsCollisionOn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	isCollisionOn = !btIsCollisionOn.isSelected();
            }
        });
		
		
		// clear button
		clear.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	SimulationWorld.clear();
	        }
	    });
		
	}

	*/

