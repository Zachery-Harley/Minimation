package application;

public class Tools {
   
   private static int sign(float x){
      if(x < 0){
         return -1;
      }
      else {
         return 1;
      }
   }
   
   public static void line(int x0, int y0, int x1, int y1){
      //Need to make sure that x0 < x1
      if(x0 > x1){
         int tx = x1;
         int ty = y1;
         x1 = x0;
         y1 = y0;
         x0 = tx;
         y0 = ty;
      }
      
      //Calculate the differences in X and Y
      float deltaX = x1 - x0;
      float deltaY = y1 - y0;
      float x,y = 0;
      
      if(Math.abs(deltaX) > Math.abs(deltaY)){
    	  //Greater change in X
    	  for(x = x0; x != x1; x += sign(deltaX)){
    	     y = y1 + (x - x1) * deltaY/deltaX;
    	     Session.currentLayer.setPixel((int)x, (int)y, Session.currentColour);
    	  }
      } else {
       //Greater change in Y
         for(y = y0; y != y1; y += sign(deltaY)){
            x = x1 + (y - y1) * deltaX/deltaY;
            Session.currentLayer.setPixel((int)x, (int)y, Session.currentColour);
         }
      }
      //Refresh the canvas
      PixelCanvas.getInstance().resetLine();
   }
   
}
