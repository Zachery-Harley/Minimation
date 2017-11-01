package application;

import javafx.scene.shape.Rectangle;

public class Pixel extends Rectangle {

   private int posX;
   private int posY;
   
   public Pixel(int width, int height, int posX, int posY) {
      super(width * posX, height * posY, width, height);
      
      this.posX = posX;
      this.posY = posY;      
   }
   
   public int getXPos(){
      return this.posX;
   }
   
   public int getYPos(){
      return this.posY;
   }
   
}
