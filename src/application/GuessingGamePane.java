package application;

import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.util.Random;
import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;
public class GuessingGamePane extends HBox {
    private ArrayList<String> chars=new ArrayList<String>();
    private String hintstr;
    private Text scoring=new Text("Total score: 0");
    private int totalscore=0;
    private int count=0;
    private Text space;
    private Button hint;
    private Button again;
    private Button bgmusic;
    private Text wordtext;
    private Text meaningtext,hinttext;
    private AudioClip bg, click, win, lose;
    private int guessTime = 5;
    private Text guess;
    private VBox process;
    private Group root;
    private Button Q, W, E, R, T, Y, U, I, O, P;
    private Button A, S, D, F, G, H, J, K, L;
    private Button Z, X, C, V, B, N, M;
    private String leavespace = "";
    private String[] word;
    private Button[] alphabet = { Q, W, E, R, T, Y, U, I, O, P, A, S, D, F, G, H, J, K, L, Z, X, C, V, B, N, M };
    private String[] all = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K",
            "L", "Z", "X", "C", "V", "B", "N", "M" };
    private Stream<String> file = Files.lines(Paths.get("/Users/wei/Desktop/adv cs/projects/WordGuess/GRE vocab.txt"));
    public GuessingGamePane() throws IOException {
        // input a random line from the file
        Random generator = new Random();
        int randomNum = generator.nextInt(677);
        String importline = file.skip(randomNum).findFirst().get();
        word = importline.split(" ", 3); // split the string by space into 3 parts

        // put word and meaning in separate strings
        String wordstr = "\nAnswer: " + word[0];
        String meaningstr = "Meaning: " + word[2];
        wordtext = new Text(wordstr);
        meaningtext = new Text(meaningstr);
        meaningtext.setFill(Color.BLACK);
        hinttext = new Text("");
        hinttext.setFill(null);
             
        // show the blank
        int length = word[0].length();
        int given=generator.nextInt(length);
        for (int i = 0; i < length; i++) {
            if(i==given){
            leavespace = leavespace + word[0].substring(i,i+1).toUpperCase() + "   ";
            chars.add(word[0].substring(i,i+1));
            }
            else{
            leavespace = leavespace + "_" + "   ";
            }
        }
        space = new Text(leavespace);

        // create hint button and set on action
        hint = new Button("Want a hint?");
        hint.setOnAction(this::processHintButtonPress);
        
    
       //create the button to play again
        again = new Button("Play Again");
        again.setOnAction(this::processAgainAction);
        
        // input background music and create music button
        File bgFile = new File("/Users/wei/Desktop/adv cs/projects/WordGuess/bgm.mp3");
        bg = new AudioClip(bgFile.toURI().toString());
        bg.play();
        bgmusic = new Button("♫");
        bgmusic.setOnAction(this::processMusicButtonPress);

        VBox buttons = new VBox(hint, again,bgmusic);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.TOP_RIGHT);
        getChildren().addAll(buttons, space);
        VBox vblank = new VBox();
        
        // input sound effect
        click = new AudioClip(new File("/Users/wei/Desktop/adv cs/projects/WordGuess/click.mp3").toURI().toString());
        win = new AudioClip(new File("/Users/wei/Desktop/adv cs/projects/WordGuess/win.mp3").toURI().toString());
        lose = new AudioClip(new File("/Users/wei/Desktop/adv cs/projects/WordGuess/lose.mp3").toURI().toString());
        
        // guess number, win, and lose
        String guessStr = "Error: " + guessTime;
        guess = new Text(guessStr);
        process = new VBox(guess);
        process.setAlignment(Pos.CENTER);
        process.setSpacing(10);
        
        // keyboard
        wordtext.setFill(null);
        VBox te = new VBox(scoring,wordtext, meaningtext, space);
        te.setSpacing(10);
        te.setAlignment(Pos.CENTER);
        HBox hblank = new HBox();
        hblank.setAlignment(Pos.CENTER);

        // setting up alphabet
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = new Button(all[i]);
            alphabet[i].setOnAction(this::processButtonAction);
        }
        HBox line1 = new HBox(alphabet[0], alphabet[1], alphabet[2], alphabet[3], alphabet[4], alphabet[5], alphabet[6],
                alphabet[7], alphabet[8], alphabet[9]);// first row of our keyboard
        line1.setSpacing(5);
        HBox line2 = new HBox(alphabet[10], alphabet[11], alphabet[12], alphabet[13], alphabet[14], alphabet[15],
                alphabet[16], alphabet[17], alphabet[18]);// second row
        line2.setSpacing(5);
        HBox line3 = new HBox(alphabet[19], alphabet[20], alphabet[21], alphabet[22], alphabet[23], alphabet[24],
                alphabet[25]);// third row
        line3.setSpacing(5);
        line1.setAlignment(Pos.CENTER);
        line2.setAlignment(Pos.CENTER);
        line3.setAlignment(Pos.CENTER);

        VBox line = new VBox(line1, line2, line3);
        line.setSpacing(10);
        VBox all = new VBox(hblank, te, process, line,hinttext);
        all.setSpacing(40);
        all.setAlignment(Pos.CENTER);
        HBox smallsets = new HBox(vblank, all);
        smallsets.setSpacing(80);
        HBox sets = new HBox(smallsets, buttons);
        sets.setSpacing(30);

        getChildren().addAll(sets);
    }

    public void processButtonAction(ActionEvent event) {
        click.play(); // play sound effect
        String guessStr = "Error: " + guessTime;
        guess.setText(guessStr);
        
        // check if you lose
        if(guessTime < 0) {
            guessStr = "Try Again!";
            guess.setText(guessStr);
            lose.play();
        }
        
        for (int i = 0; i < alphabet.length; i++) {
            if (event.getSource().equals(alphabet[i])) { // check the letter on the clicked button
                int num = 0;
                int[] a = new int[31];
                String noww = "";
                boolean present = false;
                for (int j = 0; j < word[0].length(); j++) {
                    if (word[0].substring(j, j + 1).equals(all[i].toLowerCase())) { // check if the letter is in the word
                        num++;
                        a[num] = j; // store the index of the letter
                        present = true;
                    }
                }
                if(!present) {
                    guessTime--;
                    guessStr = "Error: " + guessTime;
                    guess.setText(guessStr);
                }
                for (int j = 0; j < leavespace.length(); j++) {
                    boolean flag = true;
                    for (int k = 1; k <= num; k++) {
                        if (a[k] * 4 == j)
                            flag = false;
                    }
                    if (flag) {
                        noww += leavespace.substring(j, j + 1);
                    } else {
                        noww += all[i];
                    }
                }
                leavespace = noww;
                space.setText(leavespace);
                
                // check if you win or lose
                                if(guessTime >= 0 && leavespace.indexOf("_") == -1) {
                                         guessStr = "You Win!";
                                         guess.setText(guessStr);
                                         win.play();
                                         totalscore+=10;
                                         scoring.setText("Total score: "+totalscore);
                                         break;
                                }else if(guessTime < 0) {
                                         guessStr = "Try Again!";
                                         guess.setText(guessStr);
                                         wordtext.setFill(Color.BLACK);
                                         lose.play();
                                }
                
                break;
            }
                }
        }
    

    public void processHintButtonPress(ActionEvent event) {
        boolean start=true;
        int randomNum=0;
        while(start){
        start=false;
        int wordlength=word[0].length();
        Random generator = new Random();
        randomNum = generator.nextInt(wordlength);
        String temp=word[0].substring(randomNum,randomNum+1);
        hintstr="Hint: "+temp.toUpperCase();
        for(int i=0;i<chars.size();i++){
            if(temp.equals(chars.get(i))){
                start=true;
                break;
            }
        }
        }
        chars.add(word[0].substring(randomNum,randomNum+1));
        hinttext.setText(hintstr);
        hinttext.setFill(Color.BLACK);
        count++;
        totalscore-=2;
        scoring.setText("Total score: "+totalscore);
    }
    
    public void processAgainAction(ActionEvent event){
        try {
            guessTime = 5;
            String guessStr = "Error: " + guessTime;
            guess.setText(guessStr);
            // input a random line from the file
            Random generator = new Random();
            int randomNum = generator.nextInt(677);
            Stream<String> file = Files.lines(Paths.get("/Users/wei/Desktop/adv cs/projects/WordGuess/GRE vocab.txt"));
            String importline = file.skip(randomNum).findFirst().get();
            word = importline.split(" ", 3); // split the string by space into 3 parts

            // put word and meaning in separate strings
            String wordstr = "\nAnswer: " + word[0];
            String meaningstr = "Meaning: " + word[2];
            wordtext.setText(wordstr);
            wordtext.setFill(null);
            meaningtext.setText(meaningstr);
            meaningtext.setFill(Color.BLACK);
            hinttext.setText("");
            hinttext.setFill(null);
            
            
            // show the blank
            int length = word[0].length();
            leavespace = "";
            int given=generator.nextInt(length);
            for (int i = 0; i < length; i++) {
                if(i==given){
                    leavespace = leavespace + word[0].substring(i,i+1).toUpperCase() + "   ";
                    chars.add(word[0].substring(i,i+1));
                }
                else{
                    leavespace = leavespace + "_" + "   ";
                }
            }
            space.setText(leavespace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processMusicButtonPress(ActionEvent event) {
        if (bg.isPlaying()) {
            bg.stop();
        } else {
            bg.play();
        }
    }
}
