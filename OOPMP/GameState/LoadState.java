package GameState;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoadState extends GameState {

 // checks last achieved level from a file and changes to appropriate gamestate
 public LoadState(GameStateManager gsm) {
  this.gsm = gsm;
  switchState(load());
 }
 public LoadState() {}
 public void switchState(String level) {
  if (level.equals("LEVEL1")) {
   gsm.setState(GameStateManager.LEVEL1STATE);
  } else if (level.equals("LEVEL2")) {
   gsm.setState(GameStateManager.LEVEL2STATE);
  }
 }
 //load method
 public static String load() {
  String text = "";
  Scanner scanner;
  try {
   scanner = new Scanner(new File("save.txt"));

   text = scanner.next();

   scanner.close();

  } catch (FileNotFoundException e) {
   System.out.println("Cant Find save.txt");
   e.printStackTrace();
  }
  return text;
 }
 // save method
 public void save(String level) {
  try {
   File file = new File("save.txt");
   FileWriter fileWriter = new FileWriter(file);
   fileWriter.write(level);
   fileWriter.flush();
   fileWriter.close();
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
 public void init() {}
 public void update() {}
 public void draw(Graphics2D g) {}
 public void keyPressed(int k) {}
 public void keyReleased(int k) {}

}