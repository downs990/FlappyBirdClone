package flappybird;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class FlappyBird extends Application {
    static final int SCREEN_WIDTH = 420;
    static final int SCREEN_HEIGHT = 600;
    Label score_label;
    Button replay_btn;
    int score = 0;

    static int bird_y = 200;
    int pipes_x = 500;
    int deaths = 0;
    Timeline animation;

    ArrayList<ImageView> topPipes = new ArrayList<>();
    ArrayList<ImageView> bottomPipes = new ArrayList<>();

    ImageView bird;
    Rotate birdRotation = new Rotate(); // Rotate transformation for the bird

    boolean isRising = false; // Track if the bird is rising

    @Override
    public void start(Stage primaryStage) {
        // Background setup
        Image image = new Image("bg2.png");
        ImageView background = new ImageView(image);
        background.setFitHeight(SCREEN_HEIGHT +10);
        background.setFitWidth(SCREEN_WIDTH + 500);

        Pane root = new Pane();
        root.setOnMouseClicked(event -> {
            // Bird jumps when the screen is clicked
            bird_y -= 50;
            bird.setLayoutY(bird_y);

            // Rotate bird counterclockwise (-20°) and set rising flag
            isRising = true;
            birdRotation.setAngle(-20);

            // Start a timer to reset rotation to falling after the jump
            Timeline resetRotation = new Timeline(new KeyFrame(Duration.millis(180), e -> isRising = false));
            resetRotation.setCycleCount(1);
            resetRotation.play();
        });
 
        score_label = new Label("0");
        score_label.setScaleX(3);
        score_label.setScaleY(3);
        score_label.setLayoutX(210);
        score_label.setLayoutY(100);
        score_label.setTextFill(Color.BLACK);
        score_label.setTextFill(Color.WHITE); // Set text color to white
        score_label.setFont(new Font("Arial", 20)); // Set font and size

        // Apply CSS for outline
        score_label.setStyle("-fx-effect: dropshadow(gaussian, black, 1.5, 1.0, 0, 0); " +
               "-fx-text-fill: white;");

        replay_btn = new Button("Replay");
        replay_btn.setLayoutX(180);
        replay_btn.setLayoutY(300);
        replay_btn.setVisible(false);
        replay_btn.setOnAction(event -> {
            // Reset game state
            score = 0;
            score_label.setText("" + score);
            pipes_x = 500;
            bird_y = 200;
            bird.setLayoutY(bird_y);
            birdRotation.setAngle(0); // Reset rotation
            bird.setVisible(true);
            replay_btn.setVisible(false);
            animation.play();
        });

        root.getChildren().add(background);

        // Add pipes
        for (int i = 0; i < 200; i++) {
            Image top = new Image("top_pipe.png");
            ImageView top_pipe = new ImageView(top);
            top_pipe.setFitHeight(250);
            top_pipe.setFitWidth(150);
            top_pipe.setLayoutX(400);
            top_pipe.setLayoutY(0);
            topPipes.add(top_pipe);
            root.getChildren().add(top_pipe);

            Image bottom = new Image("bottom_pipe.png");
            ImageView bottom_pipe = new ImageView(bottom);
            bottom_pipe.setFitHeight(290);
            bottom_pipe.setFitWidth(150);
            bottom_pipe.setLayoutX(200);
            bottom_pipe.setLayoutY(270);
            bottomPipes.add(bottom_pipe);
            root.getChildren().add(bottom_pipe);
        }

        Image imageBird = new Image("bird.png");
        bird = new ImageView(imageBird);
        bird.setFitHeight(45);
        bird.setFitWidth(45);
        bird.setLayoutX(150);
        bird.setLayoutY(bird_y);

        bird.getTransforms().add(birdRotation); // Add rotation to the bird
        root.getChildren().add(bird);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        root.getChildren().add(score_label);
        root.getChildren().add(replay_btn);

        primaryStage.setTitle("Flappy Bird - Courtney");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> updateGame());
        animation = new Timeline(keyFrame);
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    public static void main(String[] args) {
        launch(args);
    }


    
    
    public void collisionDetection(int index) throws InterruptedException {

        double bird_x_axis = bird.getLayoutX();
        double bird_y_axis = bird.getLayoutY();
        double top_pipe_x_axis = topPipes.get(index).getLayoutX();
        double top_pipe_y_axis = topPipes.get(index).getLayoutY();
        
        double bottom_pipe_x_axis = bottomPipes.get(index).getLayoutX();
        double bottom_pipe_y_axis = bottomPipes.get(index).getLayoutY();

        
        //sets the score
        if((top_pipe_x_axis < bird_x_axis) && (top_pipe_x_axis > ( bird.getLayoutX() - 2) )){
        
            score++;
                        
            //sets the score to the screen
            score_label.setText(""+Integer.toString(score));
        }
        
        
        //if you hit a pipe at the top or the bottom
        if (((bird_y_axis <= topPipes.get(index).getFitHeight()-50 ) && ((bird_x_axis < top_pipe_x_axis+100) && (bird_x_axis >= top_pipe_x_axis)))||
               ((bird_y_axis > bottomPipes.get(index).getFitHeight() ) && (bird_x_axis >= bottom_pipe_x_axis && bird_x_axis < bottom_pipe_x_axis+100))) {

          //what happens when bird hits pipe ?            
            deaths++;
            System.out.println("You died " + deaths + " times.");

          
            //birdie go bye bye and game stops (pauses)
            replay_btn.setVisible(true);
            bird.setVisible(false);
            animation.pause();

        
        }

    }
     


    public void updateGame() {
        // Bird falls if not rising also falls if not on ground
        if (!isRising && bird_y < 500) {
            bird_y += 2;
            bird.setLayoutY(bird_y);

            // Rotate bird clockwise (20°) as it falls
            birdRotation.setAngle(20);
        }

        pipes_x--;

        for (int i = 0; i < topPipes.size(); i++) {

            try {
                collisionDetection(i);
        
            } catch (InterruptedException ex) {
                Logger.getLogger(FlappyBird.class.getName()).log(Level.SEVERE, null, ex);
            }

            topPipes.get(i).setLayoutX(pipes_x + (i * 250));
            bottomPipes.get(i).setLayoutX(pipes_x + (i * 250));
        }
    }
}
