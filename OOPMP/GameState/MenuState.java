package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;
//Menu for the game
public class MenuState extends GameState {

 private Background bg;

 private int currentChoice = 0;
 private String[] options = {
  "Start",
  "Load",
  "Quit"
 };


 private Font font;

 private AudioPlayer bgMusic;
 // constructor for menu state, loads background, font that will be used for display and music
 public MenuState(GameStateManager gsm) {

  this.gsm = gsm;

  try {

   bg = new Background("/Backgrounds/menubg.gif", 1);


   font = new Font("Calibri", Font.PLAIN, 25);
   bgMusic = new AudioPlayer("/Music/menu.wav");
   bgMusic.play();

  } catch (Exception e) {
   e.printStackTrace();
  }

 }

 public void init() {}

 public void update() {}
//draw call
 public void draw(Graphics2D g) {

  // draw bg
  bg.draw(g);


  // draw menu options
  g.setFont(font);
  for (int i = 0; i < options.length; i++) {
   if (i == currentChoice) {
    g.setColor(Color.WHITE);
   } else {
    g.setColor(new Color(117, 191, 161));
   }
   g.drawString(options[i], 155, 140 + i * 25);
  }

 }
// check what is selected and change gamestate based on that
 private void select() {
	 bgMusic.close();
  if (currentChoice == 0) {	
   gsm.setState(GameStateManager.LEVEL1STATE);
  } else if (currentChoice == 1) {
   gsm.setState(GameStateManager.LOADSTATE);
  } else if (currentChoice == 2) {
   System.exit(0);
  }
 }
//swing keylisteners
 public void keyPressed(int k) {
  if (k == KeyEvent.VK_ENTER) {
   select();
  }
  if (k == KeyEvent.VK_UP) {
   currentChoice--;
   if (currentChoice == -1) {
    currentChoice = options.length - 1;
   }
  }
  if (k == KeyEvent.VK_DOWN) {
   currentChoice++;
   if (currentChoice == options.length) {
    currentChoice = 0;
   }
  }
 }
 public void keyReleased(int k) {}

}