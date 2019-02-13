import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BugWorld extends Application {
	//Initialize attribute values for the world
	private int width = 800; //initialize width of world to 400 pixels
	private int height = 600; //initialize width of world to 400 pixels
	private int numObstacles = 20; //initialize the bug (object) count to 20
	private int numPlants = 20; //initialize the plant (object) count to 20
	private int numBugs = 20; //initialize the plant (object) count to 20
	private Image bugImage;
	private Image plantImage;
	private Image obstacleImage;
	private Image bgImage;

	//Set up the arraylists to store the bug/plant/obstacle objects 
	private ArrayList<Thing> things = new ArrayList<Thing>(); //'Master' arraylist to store all objects regardless of type
	private ArrayList<Bug> bugs = new ArrayList<Bug>();
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private ArrayList<Plant> plants = new ArrayList<Plant>();
	
	//Set up application with groups/buttons(game control)
	private Stage primaryStage; 
	private Group master;
	private GridPane worldGrid;
	private Button addBugsBtn;
	private Button addPlantsBtn;
	private Button addObsBtn;
	private Button playPauseBtn;
	private Timeline timeline;

	//Method for running the application
	public void start(Stage stage) throws Exception {

		primaryStage = stage;

		//Set up a grid to hold all the objects in the world
		worldGrid = new GridPane(); 
		worldGrid.setAlignment(Pos.TOP_LEFT);
		worldGrid.setHgap(10);

		//Create a button for adding new bugs and place it on the grid
		addBugsBtn = new Button("Add Bug");
		worldGrid.add(addBugsBtn,1,0);

		//Create a button for adding new plants and place it on the grid
		addPlantsBtn = new Button("Add Plant");
		worldGrid.add(addPlantsBtn,2,0);

		//Create a button for adding new obstacles and place it on the grid
		addObsBtn = new Button("Add Obstacle");
		worldGrid.add(addObsBtn,3,0);

		//Create a button for adding new obstacles and place it on the grid
		playPauseBtn = new Button("Pause/Play");
		worldGrid.add(playPauseBtn,4,0);
		
		//Set up images for the bug/plant/obstacle objects
		bugImage = new Image("beetleImage.png");
		plantImage = new Image("plantImage.png");
		obstacleImage = new Image("obstacleImage.png");
		bgImage = new Image("bgImage.jpg");

		//Add the grid (and all its objects) to the master group
		master = new Group(worldGrid);

		//add obstacles to the grid
		addObstacles();

		//add plants to the grid
		addPlants();

		// add Bugs to the grid
		addBugs();

		//Add the master group to the scene
		final Scene scene = new Scene(master); 
		scene.setFill(new ImagePattern(bgImage));

		//When the Add bugs button is pressed, create a bug object and add to the grid
		addBugsBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent newBugs) {
				numBugs = 1;
				addBugs();
			}
		});

		//When the Add plants button is pressed, create a new plant object and add to the grid
		addPlantsBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent newPlants) {
				numPlants = 1;
				addPlants();
			}
		});
		//When the Add obstacles button is pressed, create a new obstacle object and add to the grid
		addObsBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent newObs) {
				numObstacles = 1;
				addObstacles();
			}
		});

		//If play/pause button is clicked, pause the animation
		playPauseBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent playPause) {
				if(timeline.getStatus() == Animation.Status.RUNNING) {
					timeline.pause();
				}
				else {
					timeline.play();
				}
			}
		});

		//Set up an animation that refreshes every 15 millisecs. Each refresh, the below updateWorld ActionEvent is called
		KeyFrame frame = new KeyFrame(Duration.millis(15), new EventHandler<ActionEvent>() {

			public void handle(ActionEvent updateWorld) {

				//Run through every bug object stored in the bugs arraylist, 
				for (Bug b: bugs) {	
					b.update();  //Call the update method on every bug object (from the bug class)

					//If the bugs energy is below zero, remove from the bugworld
					if(b.getEnergy() < 0) {
						//bugs.remove(b);  //should remove the bug from the bug arraylist - currently throwing an error
						//things.remove(b);  //should remove the bug from the things arraylist - currently throwing an error
						master.getChildren().remove(b); //remove the bug object from the master group
					}

					//check if the bug object is colliding 
					//If it is, call the collision method from the bug class to change direction
					if(isColliding(b)){
						b.collision();
					}
					//Run through every plant object in the plant class
					for(Plant p: plants) {
						// Check if the plant object is colliding with a bug object, 
						//if so, increase the bug objects energy and decrease the bug objects size
						if(isCollidingPlant(p)) {
							b.setEnergy(b.getEnergy()+10); 
							p.setRadius(p.getRadius()-1);
						}
						//If the size of the plant is less than zero, remove the plant object
						if(p.getRadius() < 0) {
							master.getChildren().remove(p);
							//plants.remove(p);  - as per removing bugs, this throws an error
							//things.remove(p);
						}
						//Otherwise increase the plants size (slowly) each frame
						else {
							p.setRadius(p.getRadius()+0.001);	
						}
					}
				}
			}
		});
		//Play the animation
		timeline = new Timeline(frame);
		timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
		timeline.play();

		//set the screen up with the title and all components
		primaryStage.setTitle("Bug World");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Method to add bugs to the bugworld grid
	 */
	public void addBugs() {
		//Add bugs according to the specified numBugs
		for(int i=0; i< numBugs;i++) {
			int radius = 20; //sets the size of the bug
			int energy = (int)(Math.random()*1000); //set the energy of the bug to be a random value between 1-1000

			//Color c = Color.BLUE; //set the color of the bug
			Bug b = new Bug(0,0,radius,energy,primaryStage);
			do { //Give the bug a random X/Y position
				int x = (int)(Math.random() * width+radius);
				int y = (int)(Math.random() * height+radius);
				b.setTranslateX(x);
				b.setTranslateY(y);
			} while(isColliding(b));  //if it is colliding with another object, go back up and re-run loop(give it a new position)
			b.setFill(new ImagePattern(bugImage));
			bugs.add(b);  //add bug to bugs arraylist
			things.add(b); //add bug to things arraylist
			b.moveBug(); //move the bug in a random direction
			master.getChildren().add(b); //add bug to the main group
		}
	}
	/**
	 * Add plants to the bug world
	 */
	public void addPlants() {
		//Add plants according to the specified numPlants
		for(int i=0; i<numPlants;i++) {
			int radius = 20;
			//Color c = Color.GREEN;
			Plant p = new Plant(0,0,radius,primaryStage);
			//Give the bug a random X/Y position
			do { 
				int x = (int)(Math.random() * width+radius);
				int y = (int)(Math.random() * height+radius);
				p.setTranslateX(x);
				p.setTranslateY(y);
			} while(isColliding(p)); //if it is colliding, go back up and re-run loop(give it a new position)
			p.setFill(new ImagePattern(plantImage));
			plants.add(p); //add plant to plants arraylist
			things.add(p); //add plant to things arraylist
			master.getChildren().add(p); //add plant to the main group
		}
	}
	/**
	 * Add obstacles to the bug world
	 */
	public void addObstacles() {
		//Add obstacles according to the specified numObstacles
		for(int i=0; i < numObstacles;i++) {
			int radius = 20;
			//Color c = Color.BLACK;
			Obstacle o = new Obstacle(0,0,radius,primaryStage);
			//Give the plant a random X/Y position
			do { 
				int x = (int)(Math.random() * width+radius);
				int y = (int)(Math.random() * height+radius);
				o.setTranslateX(x);
				o.setTranslateY(y);
			} while(isColliding(o));  //if it is colliding, go back up and re-run loop(give it a new position)
			o.setFill(new ImagePattern(obstacleImage));
			obstacles.add(o); //add obstacle to obstacles arraylist
			things.add(o); //add obstacle to things arraylist
			master.getChildren().add(o); //add obstacle to the main group
		}
	}

	//check if any thing object is colliding with obstacles 
	public boolean isColliding(Thing t) {

		boolean isColliding = false;
		for(Obstacle o: obstacles) {
			//If the boundaries of the obstacle objects touch the boundaries of another object, return true
			if(t.getBoundsInParent().intersects(o.getBoundsInParent())){
				isColliding = true;
			}  
		}
		//check if the thing object collides with any buttons
		if(t.getBoundsInParent().intersects(addBugsBtn.getBoundsInParent())){
			isColliding = true;
		} 
		if(t.getBoundsInParent().intersects(addPlantsBtn.getBoundsInParent())){
			isColliding = true;
		}
		if(t.getBoundsInParent().intersects(addObsBtn.getBoundsInParent())){
			isColliding = true;
		}
		return isColliding;
	}

	//check if a bug is on a plant object
	public boolean isCollidingPlant(Plant p) {
		for(Bug b: bugs) {
			//If the boundaries of the bug objects touch the boundaries of a plant object, return true
			if(p.getBoundsInParent().intersects(b.getBoundsInParent())){
				return true;
			} 
		}return false;
	}

	//to launch the program
	public static void main(String[] args) {
		launch();
	}
}