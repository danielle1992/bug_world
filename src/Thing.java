import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
//extends the circle class
public class Thing extends Circle {

	private double xPos; //xposition of circle
	private double yPos; //y position of circle
	protected double radius; //to determine size of the circle
	protected Stage parent; //to link to properties within BugWorld class

	//Set up the constructor for creating 'Thing' objects
	public Thing(double x, double y, double radius, Stage parent) {
		setTranslateX(x); 
		setTranslateY(y);
		setRadius(radius);
		this.parent = parent;
	}
}


