package app;

import gui.HangmanPane;

import java.util.LinkedHashSet;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Hangman extends Application {
	private static final int FONT_SIZE = 20;
	private static final String[] WORD_BANK = {
			"bank", "list", "people", "computer", "chess", "friend", "money",
			"job", "software", "genius", "google", "happy", "milk", "water"
	};

	private static String currentWord;
	private static SimpleStringProperty stringPropertyWord;
	private static SimpleStringProperty stringPropertyLetters;
	private static LinkedHashSet<Character> usedLetters;
	private static int counter;
	private static GridPane dialogPane;
	private static StackPane stackPane;
	private static StringBuilder usedLettersString;
	private static int[] codes;
	private static boolean[] flags;
	private static boolean isDone;
	private static HangmanPane hangmanPane;
	private static HBox promptContainer;
	private static Text retryPrompt;
	private static BorderPane promptPane;

	static {
		retryPrompt = new Text("Press enter to play again");
		retryPrompt.setFont(new Font(FONT_SIZE));
		retryPrompt.setFill(Color.RED);
		promptContainer = new HBox();
		promptContainer.setPadding(new Insets(0, 0, 10, 0));
		promptContainer.setAlignment(Pos.CENTER);
		promptContainer.getChildren().add(retryPrompt);
		usedLettersString = new StringBuilder();
		usedLetters = new LinkedHashSet<>();
		currentWord = getRandomWord(); 
		codes = new int[currentWord.length()];
		flags = new boolean[currentWord.length()];
		fillCodesArray();
		counter = 0;
		hangmanPane = new HangmanPane();
		isDone = false;
		promptPane = new BorderPane();
		promptPane.setBottom(promptContainer);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		dialogPane = getDialogPane();

		stringPropertyLetters = new SimpleStringProperty();

		Text lettersText = new Text();
		lettersText.setFont(new Font(FONT_SIZE));
		lettersText.textProperty().bind(stringPropertyLetters);

		dialogPane.add(lettersText, 11, 36);

		stringPropertyWord = new SimpleStringProperty(currentWord.replaceAll("\\w", "*"));

		Text output = setNewWord(dialogPane, currentWord);
		output.textProperty().bind(stringPropertyWord);;

		stackPane = new StackPane();
		stackPane.getChildren().add(dialogPane);
		stackPane.getChildren().add(hangmanPane);


		Scene scene = new Scene(stackPane, 500, 510);
		primaryStage.setTitle("Hangman");
		primaryStage.setScene(scene);
		primaryStage.show();
		

		scene.setOnKeyTyped(e -> {
			if(!isDone) {
				if(e.getCharacter().matches("[\\W^[0-9]]"))
					return;
				else
					update(e.getCharacter().toLowerCase().charAt(0));
			} else {
				if(e.getCharacter().matches("[\\f\\r\\n]")) {
					reset();
					return;
				}
			}
			
		});
	}

	private static void fillCodesArray() {
		for(int i = 0; i < codes.length; i++) {
			codes[i] = currentWord.charAt(i) - '*';
		}
	}

	private static void update(char x) {

		if(currentWord.indexOf(x) < 0) {
			if(!usedLetters.contains(x) && ++counter == 7) {
				stackPane.getChildren().add(promptPane);
				hangmanPane.swingMan();
				isDone = true;
			}
			hangmanPane.drawParts(counter);


		} else {
			StringBuilder stringBuilder = new StringBuilder();
			int code = x - '*';
			for(int i = 0; i < codes.length; i++) {
				if (code == codes[i]) {
					flags[i] = true;
					stringBuilder.append((char)(codes[i] + '*'));
				} else {
					if(flags[i])
						stringBuilder.append((char)(codes[i] + '*'));
					else
						stringBuilder.append('*');
				}
			}
			if(stringBuilder.toString().indexOf('*') < 0) {
				stackPane.getChildren().add(promptPane);
				isDone = true;
			}
			stringPropertyWord.set(stringBuilder.toString());
		}

		if(usedLetters.add(x))
			stringPropertyLetters.set(usedLettersString.append(x).append(" ").toString());
	}

	private static void reset() {
		
		hangmanPane.pause();
		usedLettersString.setLength(0);
		usedLetters.clear();
		currentWord = getRandomWord();
		codes = new int[currentWord.length()];
		flags = new boolean[currentWord.length()];
		fillCodesArray();
		if(counter > 0)
			hangmanPane.getChildren().remove(2);
		isDone = false;
		counter = 0;
		stringPropertyWord.set(currentWord.replaceAll("\\w", "*"));
		stringPropertyLetters.set("");
		stackPane.getChildren().remove(2);
	}
	
	private static Text setNewWord(GridPane dialogPane, String string) {
		Text text = new Text();
		text.setFont(new Font(FONT_SIZE));
		dialogPane.add(text, 11, 35);

		return text;
	}

	private static GridPane getDialogPane() {
		Text txt1 = new Text("Guess the Word: ");
		txt1.setFont(new Font(FONT_SIZE));

		Text txt2 = new Text("Used Letters: ");
		txt2.setFont(new Font(FONT_SIZE));

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));

		grid.add(txt1, 10, 35);
		grid.add(txt2, 10, 36);

		return grid; 
	}

	private static String getRandomWord(){
		return WORD_BANK[(int)(Math.random() * WORD_BANK.length)];
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}