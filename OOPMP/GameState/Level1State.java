package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
//State for lvl 1
public class Level1State extends GameState {

 private TileMap tileMap;
 private Background bg;

 private Player player;

 private ArrayList < Enemy > enemies;
 private ArrayList < Explosion > explosions;

 int xpos;
 Font font = new Font("Bradley Gratis", Font.PLAIN, 25);

 private HUD hud;
 //private DEBUG debug;

 private AudioPlayer bgMusic;

 public Level1State(GameStateManager gsm) {
  this.gsm = gsm;
  init();
 }
 // initialise tiles, sprites, music, place enemies and player. also save LEVEL1 into the save file
 public void init() {

  tileMap = new TileMap(30);
  tileMap.loadTiles("/Tilesets/castletileset.gif");
  tileMap.loadMap("/Maps/map1.map");
  tileMap.setPosition(0, 0);
  tileMap.setTween(1);

  bg = new Background("/Backgrounds/l1_bg.gif", 0.1);

  player = new Player(tileMap);
  player.setPosition(100, 100);

  (new LoadState()).save("LEVEL1");

  populateEnemies();

  explosions = new ArrayList < Explosion > ();

  hud = new HUD(player);
  //debug = new DEBUG(player);
  
  bgMusic = new AudioPlayer("/Music/lvl1.wav");
  bgMusic.play();


 }
//creates enemy objects puts them in an array list with gievn coordinates
 private void populateEnemies() {

  enemies = new ArrayList < Enemy > ();

  Bat s;
  Point[] points = new Point[] {
   new Point(200, 100),
    new Point(400, 150),
    new Point(200, 150),
    new Point(400, 100),
    new Point(200, 75)
  };
  for (int i = 0; i < points.length; i++) {
   s = new Bat(tileMap);
   s.setPosition(points[i].x, points[i].y);
   enemies.add(s);
  }

 }
//updates positions, background position, enemy position, and check if enemy is dead
 public void update() {

  // update player
  player.update();
  tileMap.setPosition(
   GamePanel.WIDTH / 2 - player.getx(),
   GamePanel.HEIGHT / 2 - player.gety()
  );

  // set background
  bg.setPosition(tileMap.getx(), tileMap.gety());

  // attack enemies
  player.checkAttack(enemies);

  // update all enemies
  for (int i = 0; i < enemies.size(); i++) {
   Enemy e = enemies.get(i);
   e.update();
   if (e.isDead()) {
    enemies.remove(i);
    i--;
    explosions.add(new Explosion(e.getx(), e.gety()));
   }
  }

  // update explosions
  for (int i = 0; i < explosions.size(); i++) {
   explosions.get(i).update();
   if (explosions.get(i).shouldRemove()) {
    explosions.remove(i);
    i--;
   }
  }

 }

 public void draw(Graphics2D g) {

  // draw bg
  bg.draw(g);

  // draw tilemap
  tileMap.draw(g);

  // draw player
  player.draw(g);

  // draw enemies
  for (int i = 0; i < enemies.size(); i++) {
   enemies.get(i).draw(g);
  }

  // draw explosions
  for (int i = 0; i < explosions.size(); i++) {
   explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
   explosions.get(i).draw(g);
  }
  if (player.gety() <= 270 && player.gety() >= 250) {
   gsm.setState(GameStateManager.LOADSTATE);

    bgMusic.close();
  }
  if (player.getx() <= 1700 && player.getx() >= 1620) {
   gsm.setState(GameStateManager.LEVEL2STATE);

    bgMusic.close();
  }
  // draw hud
  hud.draw(g);

  //debug.draw(g);

 }
// keylisteners for player interaction
 public void keyPressed(int k) {
  if (k == KeyEvent.VK_LEFT) player.setLeft(true);
  if (k == KeyEvent.VK_RIGHT) player.setRight(true);
  if (k == KeyEvent.VK_UP) player.setUp(true);
  if (k == KeyEvent.VK_DOWN) player.setDown(true);
  if (k == KeyEvent.VK_SPACE) player.setJumping(true);
  try {
   if (k == KeyEvent.VK_A) player.setAttack();
  } catch (Exception e) {}
 }

 public void keyReleased(int k) {
  if (k == KeyEvent.VK_LEFT) player.setLeft(false);
  if (k == KeyEvent.VK_RIGHT) player.setRight(false);
  if (k == KeyEvent.VK_UP) player.setUp(false);
  if (k == KeyEvent.VK_DOWN) player.setDown(false);
  if (k == KeyEvent.VK_SPACE) player.setJumping(false);
 }

}