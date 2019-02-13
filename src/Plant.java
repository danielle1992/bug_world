import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
//Inherits Thing class
public class Plant extends Thing {

	//Set up the constructor for creating 'Plant' objects. Inherits attributes from 'Thing' class
	public Plant(double x, double y, double radius, Stage parent) {
		super(x,y,radius,parent);
	}

	//Method for increasing the size of the plant (called on each animation frame)
	public void growPlant(Plant p) {
		p.radius++;
	}

	//Method for decrease the size of the plant (called when 'Bug' object passes over 'Plant' object)
	public void plantEaten(Plant p) {
		if(p.radius >= 1){
			p.radius--;
		}
	}
}