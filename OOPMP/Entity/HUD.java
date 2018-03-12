package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD {

 private Player player;

 private BufferedImage image;
 private Font font;
//initialises sprites
 public HUD(Player p) {
  player = p;
  try {
   image = ImageIO.read(
    getClass().getResourceAsStream("/HUD/hud.gif")
   );
   font = new Font("Arial", Font.PLAIN, 14);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
//draws it to g
 public void draw(Graphics2D g) {

  g.drawImage(image, 0, 10, null);
  g.setFont(font);
  g.setColor(Color.WHITE);
  g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
 }

}