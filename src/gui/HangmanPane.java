package gui;

import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class HangmanPane extends Pane{

	private RotateTransition rotate;
	private PathTransition move;
	private Shape hangman;

	public HangmanPane() {
		this.hangman = getHangman();
		
		this.rotate = new RotateTransition();
		this.rotate.setDuration(Duration.millis(5000));
		this.rotate.setCycleCount(500);
		this.rotate.setToAngle(-30);
		this.rotate.setFromAngle(30);
		this.rotate.setAutoReverse(true);
		this.rotate.setNode(this.hangman);
		
		Arc arc = new Arc();
		arc.setLength(60d);
		arc.setCenterY(20.0d);
		arc.setCenterX(270d);
		arc.setRadiusX(126.31d);
		arc.setRadiusY(126.31d);
		arc.setStartAngle(240);
		
		this.move = new PathTransition();
		this.move.setDuration(Duration.millis(5000));
		this.move.setCycleCount(500);
		this.move.setAutoReverse(true);
		this.move.setPath(arc);
		this.move.setNode(this.hangman);

		getChildren().add(new Arc(70.0f, 650.0f, 200.0f, 200.0f, 70.0f, 40.0f));
		getChildren().add(new Polyline(70.0d, 450.0d, 70.0d, 20.0d, 270.0d, 20.0d));

	}
	
	public static Shape getHangman() {
		
		Circle head = new Circle(270.0d, 90.0d, 30.0d);
		head.setFill(null);
		head.setStroke(Color.BLACK);
		Shape temp = Shape.union(head, new Line(248.79d, 111.79d, 214.47d, 146.11d));
		Shape temp2 = Shape.union(temp, new Line(291.21d, 111.79d, 325.53d, 146.11d));
		temp = Shape.union(temp2, new Line(270.0d, 120.0d, 270.0d, 217.08d));
		temp2 = Shape.union(new Line(270.0d, 217.08d, 214.47d, 272.61d), temp);
		temp = Shape.union(temp2, new Line(270.0d, 217.08d, 325.53d, 272.61d));
		temp2 = Shape.union(temp, new Line(270.0d, 20.0d, 270.0d, 60.0d));
		return temp2;
	}

	public void drawParts(int counter) {
		
		Shape newShape = null;
		switch(counter) {
		case 1:
			newShape = new Line(270.0d, 20.0d, 270.0d, 60.0d);
			break;
		case 2:
			Circle head = new Circle(270.0d, 90.0d, 30.0d);
			head.setFill(null);
			head.setStroke(Color.BLACK);
			newShape = Shape.union((Shape)getChildren().remove(2), head);
			break;
		case 3:
			newShape = Shape.union((Shape)getChildren().remove(2), new Line(248.79d, 111.79d, 214.47d, 146.11d));
			break;
		case 4:
			newShape = Shape.union((Shape)getChildren().remove(2), new Line(291.21d, 111.79d, 325.53d, 146.11d));
			break;
		case 5:
			newShape = Shape.union((Shape)getChildren().remove(2), new Line(270.0d, 120.0d, 270.0d, 217.08d));
			break;
		case 6:
			newShape = Shape.union((Shape)getChildren().remove(2), new Line(270.0d, 217.08d, 214.47d, 272.61d));
			break;
		case 7:
			getChildren().remove(2);
			newShape = hangman;
			break;
		}
		
		getChildren().add(newShape);
	}

	public void swingMan() {
		
		this.move.playFrom(Duration.millis(2500));
		this.rotate.playFrom(Duration.millis(2500));
	}
	
	public void pause() {
		
		this.move.stop();
		this.rotate.stop();
	}

}