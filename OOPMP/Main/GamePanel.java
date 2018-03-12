package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import GameState.GameStateManager;

public class GamePanel extends JPanel implements Runnable, KeyListener {

 // dimensions
 public static final int WIDTH = 350;
 public static final int HEIGHT = 197;
 public static final int SCALE = 4;

 // game thread
 private Thread thread;
 private boolean running;
 private int FPS = 60;
 private long targetTime = 1000 / FPS;

 // image
 private BufferedImage image;
 private Graphics2D g;

 // game state manager
 private GameStateManager gsm;
//change gamepanel
 public GamePanel() {
  setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
  setFocusable(true);
  requestFocus();
 }

 public void addNotify() {
  super.addNotify();
  if (thread == null) {
   thread = new Thread(this);
   addKeyListener(this);
   thread.start();
  }
 }
//initialise images
 private void init() {

  image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
  g = (Graphics2D) image.getGraphics();

  running = true;

  gsm = new GameStateManager();

 }
//initialises the game images and runs the game loop
 public void run() {

  init();


  long start;
  long elapsed;
  long wait;

  // game loop
  while (running) {

   start = System.nanoTime();

   update();
   draw();
   drawToScreen();

   elapsed = System.nanoTime() - start;

   wait = targetTime - elapsed / 1000000;
   if (wait < 0) wait = 5;

   try {
    Thread.sleep(wait);
   } catch (Exception e) {
    e.printStackTrace();
   }

  }

 }

 private void update() {
  gsm.update();
 }
 //draws graphic object g
 private void draw() {
  gsm.draw(g);
 }
 // draws g2 to screen
 private void drawToScreen() {
  Graphics g2 = getGraphics();
  g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
  g2.dispose();
 }
 //swing key listeners
 public void keyTyped(KeyEvent key) {}
 public void keyPressed(KeyEvent key) {
  gsm.keyPressed(key.getKeyCode());
 }
 public void keyReleased(KeyEvent key) throws NullPointerException {
  gsm.keyReleased(key.getKeyCode());
 }

}