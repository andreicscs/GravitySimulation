package particle_gravity_simulation.graphics;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;

public class SimulationControls implements Initializable{
	@FXML
	private ChoiceBox<String> isPositive;
	ObservableList<String> choices = FXCollections.observableArrayList("Positive","Neutral","Negative");
	
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
		SimulationPanel.world.clear();
    }


	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		isPositive.setValue("Positive");
		isPositive.setItems(choices);
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
        if (selectedItem.equals("Positive")) {
            isPositiveSelected = 1;
        } else if (selectedItem.equals("Neutral")) {
            isPositiveSelected = 0;
        } else {
            isPositiveSelected = -1;
        }
    }
	
}
