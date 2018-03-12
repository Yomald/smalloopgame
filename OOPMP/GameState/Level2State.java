package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level2State extends GameState {

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

 public Level2State(GameStateManager gsm) {
  this.gsm = gsm;
  init();
 }

 public void init() {

  tileMap = new TileMap(30);
  tileMap.loadTiles("/Tilesets/castletileset.gif");
  tileMap.loadMap("/Maps/map2.map");
  tileMap.setPosition(0, 0);
  tileMap.setTween(1);
  
  bgMusic = new AudioPlayer("/Music/lvl2.wav");
  bgMusic.play();

  (new LoadState()).save("LEVEL2");

  bg = new Background("/Backgrounds/l2_bg.gif", 0.1);

  player = new Player(tileMap);
  player.setPosition(100, 100);


  populateEnemies();

  explosions = new ArrayList < Explosion > ();

  hud = new HUD(player);
  //debug = new DEBUG(player);
  }

 private void populateEnemies() {

  enemies = new ArrayList < Enemy > ();

  Bat s;
  Point[] bpoints = new Point[] {
   new Point(470, 100),
    new Point(890, 110),
    new Point(1000, 90)
  };
  for (int i = 0; i < bpoints.length; i++) {
   s = new Bat(tileMap);
   s.setPosition(bpoints[i].x, bpoints[i].y);
   enemies.add(s);
  }
  
  SurtMonster sm;
  Point[] spoints = new Point[] {
   new Point(280, 150),
    new Point(630, 150)
  };
  for (int i = 0; i < spoints.length; i++) {
   sm = new SurtMonster(tileMap);
   sm.setPosition(spoints[i].x, spoints[i].y);
   enemies.add(sm);
  }

 }

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
	  bgMusic.close();
	   gsm.setState(GameStateManager.LOADSTATE);
	  }
  // draw hud
  hud.draw(g);

  //debug.draw(g);

 }

 public void keyPressed(int k) {
  if (k == KeyEvent.VK_LEFT) player.setLeft(true);
  if (k == KeyEvent.VK_RIGHT) player.setRight(true);
  if (k == KeyEvent.VK_UP) player.setUp(true);
  if (k == KeyEvent.VK_DOWN) player.setDown(true);
  if (k == KeyEvent.VK_SPACE) player.setJumping(true);
  if (k == KeyEvent.VK_A) player.setAttack();
 }

 public void keyReleased(int k) {
  if (k == KeyEvent.VK_LEFT) player.setLeft(false);
  if (k == KeyEvent.VK_RIGHT) player.setRight(false);
  if (k == KeyEvent.VK_UP) player.setUp(false);
  if (k == KeyEvent.VK_DOWN) player.setDown(false);
  if (k == KeyEvent.VK_SPACE) player.setJumping(false);
 }

}