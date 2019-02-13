import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
//Inherits Thing class
public class Bug extends Thing {

	//list of attributes unique to Bug object
	private int energy; //energy of bug(increases/decreases as result of methods)
	private float deltaX = 1f; //to calculate the X direction the bug moves in
	private float deltaY = 1f; //to calculate the y direction the bug moves in

	//Set up the constructor for creating 'Bug' objects. Inherits attributes from 'Thing' class
	public Bug(double x, double y, double radius, int energy, Stage parent) {
		super(x,y,radius,parent);
		this.energy = energy;
	}

	//Method to move bug around world, decrease energy each move, if the bug hits a wall, change direction
	public void update() {

		/*//if the center of the circle plus the x position of the circle is less than the radius, 
		or the center of the circle plus the x position of the circle plus the radius is less than the 
		width of the bugworld grid, then change direction*/
		if((getCenterX()+getTranslateX() < getRadius()) || 
				((getCenterX()+getTranslateX() + getRadius()) > (parent.getWidth()-getRadius()))) {
			deltaX = -deltaX;
		}
		if((getCenterY()+getTranslateY() < getRadius()) ||
				((getCenterY()+getTranslateY()+getRadius()) > (parent.getHeight()-getRadius()))) {
			deltaY = -deltaY;
		}
		setTranslateX(getTranslateX() + deltaX); //sets the new X position after update method has been called
		setTranslateY(getTranslateY() + deltaY); //sets the new Y position after update method has been called
		energy--;
	}

	//Method to move in the bug a random direction(up/down/left or right)
	public void moveBug() {
		float randomNo = (float) (Math.random());
		deltaX = randomNo;
		deltaY = randomNo;		
		if (randomNo < 0.25)
			deltaX = deltaX+1; //moves bug down
		else if (randomNo > 0.5)
			deltaX = deltaX-1; //moves bug up
		else if (randomNo < 0.75)
			deltaY = deltaY+1; //moves bug right
		else
			deltaY = deltaY-1; //moves bug left

		setTranslateX(getTranslateX() + deltaX); //sets the new X position after moveBug method has been called
		setTranslateY(getTranslateY() + deltaY); //sets the new Y position after moveBug method has been called
	}

	//Method to change direction of the bug (when collision is detected)
	public void collision() {
		deltaX = -deltaX;
		deltaY = -deltaY;

		setTranslateX(getTranslateX() + deltaX);
		setTranslateY(getTranslateY() + deltaY);
	}

	//Method to increase energy of bug when called (as passing over plant object)
	public void eatPlant() {
		energy = energy+10;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}
}
