package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class Bat extends Enemy {

 private BufferedImage[] sprites;
//initialise bat enemy
 public Bat(TileMap tm) {

  super(tm);

  moveSpeed = 0.3;
  maxSpeed = 1;
  fallSpeed = 0.2;
  maxFallSpeed = 10.0;

  width = 30;
  height = 30;
  cwidth = 20;
  cheight = 20;

  health = maxHealth = 2;
  damage = 1;

  // load sprites
  try {

   BufferedImage spritesheet = ImageIO.read(
    getClass().getResourceAsStream(
     "/Sprites/Enemies/bat.gif"
    )
   );

   sprites = new BufferedImage[3];
   for (int i = 0; i < sprites.length; i++) {
    sprites[i] = spritesheet.getSubimage(
     i * width,
     0,
     width,
     height
    );
   }

  } catch (Exception e) {
   System.out.println("Error! cant find the image bat.gif");
   System.exit(0);
  }

  animation = new Animation();
  animation.setFrames(sprites);
  animation.setDelay(300);

  left = true;
  facingRight = false;

 }

 private void getNextPosition() {

  // movement
  if (left) {
   dx -= moveSpeed;
   if (dx < -maxSpeed) {
    dx = -maxSpeed;
   }
  } else if (right) {
   dx += moveSpeed;
   if (dx > maxSpeed) {
    dx = maxSpeed;
   }
  }

 }
 public void update() {

  // update position
  getNextPosition();
  checkTileMapCollision();
  setPosition(xtemp, ytemp);

  // check flinching
  if (flinching) {
   long elapsed =
    (System.nanoTime() - flinchTimer) / 1000000;
   if (elapsed > 400) {
    flinching = false;
   }
  }

  // if it hits a wall, go other direction
  if (right && dx == 0) {
   right = false;
   left = true;
   facingRight = false;
  } else if (left && dx == 0) {
   right = true;
   left = false;
   facingRight = true;
  }

  // update animation
  animation.update();

 }

 public void draw(Graphics2D g) {

  //if(notOnScreen()) return;

  setMapPosition();

  super.draw(g);

 }

}