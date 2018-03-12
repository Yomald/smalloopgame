package Entity;

import TileMap.*;
import Audio.AudioPlayer;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {

 // player stuff
 private int health;
 private int maxHealth;
 private boolean dead;
 private boolean flinching;
 private long flinchTimer;


 // attack
 private boolean attack;
 private int attackDamage;
 private int attackRange;


 // animations
 private ArrayList < BufferedImage[] > sprites;
 private final int[] numFrames = {
  2,
  8,
  1,
  2,
  6
 };

 // animation actions
 private static final int IDLE = 0;
 private static final int WALKING = 1;
 private static final int JUMPING = 2;
 private static final int FALLING = 3;
 private static final int ATTACK = 4;

 private HashMap < String, AudioPlayer > sfx;

 public Player(TileMap tm) {

  super(tm);

  width = 30;
  height = 30;
  cwidth = 20;
  cheight = 20;

  moveSpeed = 1;
  maxSpeed = 2.4;
  stopSpeed = 0.4;
  fallSpeed = 0.15;
  maxFallSpeed = 4.0;
  jumpStart = -4.8;
  stopJumpSpeed = 0.3;

  facingRight = true;

  health = maxHealth = 5;

  attackDamage = 8;
  attackRange = 40;

  // load sprites
  try {

   BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));

   sprites = new ArrayList < BufferedImage[] > ();
   for (int i = 0; i < 5; i++) {

    BufferedImage[] bi =
     new BufferedImage[numFrames[i]];

    for (int j = 0; j < numFrames[i]; j++) {

     if (i != ATTACK) {
      bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
     } else {
      bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
     }

    }
    sprites.add(bi);
   }

  } catch (Exception e) {
   e.printStackTrace();
  }

  animation = new Animation();
  currentAction = IDLE;
  animation.setFrames(sprites.get(IDLE));
  animation.setDelay(50000);

  sfx = new HashMap < String, AudioPlayer > ();
  sfx.put("jump", new AudioPlayer("/SFX/jumping2.wav"));
  sfx.put("attack", new AudioPlayer("/SFX/attack.wav"));

 }

 public int getHealth() {
  return health;
 }
 public int getMaxHealth() {
  return maxHealth;
 }

 public void setAttack() {
  attack = true;
 }

 public void checkAttack(ArrayList < Enemy > enemies) {

  // loop through enemies
  for (int i = 0; i < enemies.size(); i++) {

   Enemy e = enemies.get(i);

   // attack attack
   if (attack) {
    if (facingRight) {
     if (
      e.getx() > x &&
      e.getx() < x + attackRange &&
      e.gety() > y - height / 2 &&
      e.gety() < y + height / 2
     ) {
      e.hit(attackDamage);
     }
    } else {
     if (
      e.getx() < x &&
      e.getx() > x - attackRange &&
      e.gety() > y - height / 2 &&
      e.gety() < y + height / 2
     ) {
      e.hit(attackDamage);
     }
    }
   }

   // check enemy collision
   if (intersects(e)) {
    hit(e.getDamage());
   }

  }

 }

 public void hit(int damage) {
  if (flinching) return;
  health -= damage;
  if (health < 0) health = 0;
  if (health == 0) dead = true;
  flinching = true;
  flinchTimer = System.nanoTime();
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
  } else {
   if (dx > 0) {
    dx -= stopSpeed;
    if (dx < 0) {
     dx = 0;
    }
   } else if (dx < 0) {
    dx += stopSpeed;
    if (dx > 0) {
     dx = 0;
    }
   }
  }

  // cannot move while attacking, except in air
  if (
   (currentAction == ATTACK) &&
   !(jumping || falling)) {
   dx = 0;
  }

  // jumping
  if (jumping && !falling) {
   sfx.get("jump").play();
   dy = jumpStart;
   falling = true;
  }

  // falling
  if (falling) {

   dy += fallSpeed;

   if (dy > 0) jumping = false;
   if (dy < 0 && !jumping) dy += stopJumpSpeed;

   if (dy > maxFallSpeed) dy = maxFallSpeed;
  }

 }

 public void update() {

  // update position
  getNextPosition();
  checkTileMapCollision();
  setPosition(xtemp, ytemp);

  // check attack has stopped
  if (currentAction == ATTACK) {
   if (animation.hasPlayedOnce()) attack = false;
  }

  // check done flinching
  if (flinching) {
   long elapsed =
    (System.nanoTime() - flinchTimer) / 1000000;
   if (elapsed > 1000) {
    flinching = false;
   }
  }

  // set animation
  if (attack) {
   if (currentAction != ATTACK) {
    sfx.get("attack").play();
    currentAction = ATTACK;
    animation.setFrames(sprites.get(ATTACK));
    animation.setDelay(50);
    width = 60;
   }
  }
  /*
  			else if(currentAction != FALLING) {
  				currentAction = FALLING;
  				animation.setFrames(sprites.get(FALLING));
  				animation.setDelay(100);
  				width = 30;
  			}*/
  else if (dy < 0) {
   if (currentAction != JUMPING) {
    currentAction = JUMPING;
    animation.setFrames(sprites.get(JUMPING));
    animation.setDelay(-1);
    width = 30;
   }
  } else if (left || right) {
   if (currentAction != WALKING) {
    currentAction = WALKING;
    animation.setFrames(sprites.get(WALKING));
    animation.setDelay(40);
    width = 30;
   }
  } else if (currentAction != IDLE) {
   currentAction = IDLE;
   animation.setFrames(sprites.get(IDLE));
   animation.setDelay(400);
   width = 30;
  }

  animation.update();

  // set direction
  if (currentAction != ATTACK) {
   if (right) facingRight = true;
   if (left) facingRight = false;
  }
 }

 public void draw(Graphics2D g) {

  setMapPosition();

  // draw player
  if (flinching) {
   long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
   if (elapsed / 100 % 2 == 0) {
    return;
   }
  }
  super.draw(g);
 }

}