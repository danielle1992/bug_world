import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
//Inherits Thing class
public class Obstacle extends Thing {

	//Set up the constructor for creating 'Obstacle' objects. Inherits attributes from 'Thing' class
	public Obstacle(double x, double y, double radius,Stage parent) {
		super(x,y,radius,parent);
	}
	/**No methods as obstacles do not 'do' anything once they are created**/
}
