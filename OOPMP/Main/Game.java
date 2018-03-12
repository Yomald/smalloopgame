package Main;

import javax.swing.JFrame;

public class Game {
    // window adding the game loop
 public static void main(String[] args) {

  JFrame window = new JFrame("Platformer");
  window.setContentPane(new GamePanel());
  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  window.setResizable(false);
  window.pack();
  window.setVisible(true);

 }

}