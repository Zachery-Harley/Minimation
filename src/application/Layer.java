package application;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Layer {
   
   //////////////////////////
   /// Instance Variables ///
   //////////////////////////
   
   
   private Color[][] pixel; //The colors that will be each pixel
   private String name; //The name of the layer
   private LayerPreview previewManager;
   private int[] resolution = new int[2]; //0 - Width, 1 - Height
   
   
   //////////////////////////
   /// Constructors       ///
   //////////////////////////
   
   /**
    * Create a new layer and set all pixels to null
    * 
    * @param makeCurrent - True when this layer is to be the new current layer
    */
   public Layer() {

      //Get the frame width and height from the animation
      resolution[1] = Session.currentAnimation.getFrameHeight();
      resolution[0] = Session.currentAnimation.getFrameWidth();
      
      //Initiate the array of pixels
      pixel = new Color[resolution[0]][resolution[1]];
      
      previewManager = new LayerPreview(this);
   }
   
   /**
    * Create a new layer and set all pixels to null
    * 
    * @param makeCurrent - True when this layer is to be the new current layer
    */
   public Layer(String name) {
      this();
      this.name = name;
      
   }
   
      
   //////////////////////////
   /// Read and write     ///
   //////////////////////////
   
   /**
    * Sets the name of the layer
    * 
    * @param name    The name of the layer
    */
   public void setName(String name){
      this.name = name;
   }
   
   /**
    * Gets the name of the layer
    * 
    * @return String - The name of the layer
    */
   public String getName(){
      return this.name;
   }
   
   /**
    * Set the color of a pixel using a hexadecimal color code
    * 
    * @param x - The x position of the pixel
    * @param y - The Y position of the pixel
    * @param pixelColor - The hex value of the pixel
    */
   public void setPixel(int x, int y, String pixelColor){
      //Convert the string to a color;
      Color newColor = Color.web(pixelColor);
      //Set the pixel
      setPixel(x, y, newColor);
   }
   
   /**
    * Set the color of a pixel
    * 
    * @param x - The x position of the pixel
    * @param y - The Y position of the pixel
    * @param pixelColor - The color
    */
   public void setPixel(int x, int y, Color pixelColor){
      //Set the pixel value
      pixel[x][y] = pixelColor;
   }
   
   /**
    * Set all pixels to be a single given value
    * 
    * @param pixelValue The value of the pixel as a hex
    */
   public void setAllPixels(String pixelColor){
      Color newColor = Color.web(pixelColor);
      //Set the pixels
      setAllPixels(newColor);
   }
   
   /**
    * Set all pixels to be a single given value
    * 
    * @param pixelValue The value of the pixel as a hex in long
    */
   public void setAllPixels(Color pixelValue){
    //Loop through each pixel and set it
      for(int row = 0; row < resolution[1]; row++){
         //For each column
         for(int col = 0; col < resolution[0]; col++){
            //Set the color
            setPixel(col, row, pixelValue);
         }
      }
   }
   
   /**
    * Get the pixel from the current layer at the given poisiton.
    * Null if pixel is blank
    * 
    * @param x    The x value of the pixel
    * @param y    The y value of the pixel
    * @return Color -  The color of the pixel, null if empty.
    */
   public Color getPixel(int x, int y){
      return pixel[x][y];
   }
   
   public LayerPreview getPreviewManager(){
      return this.previewManager;
   }
   
   
   /////////////////////
   //// Methods ////////
   /////////////////////

   /**
    * Combine this layer with the given layer. The given layer will be
    * drawn over this layer.
    * NOTE: This layer will not be effected, the combined layer instead
    * will be returned.
    * @param newTop    The layer to draw over this layer
    * @return Layer - The layer that contains the combined pixels
    */
   public Layer combine(Layer newTop){
      Layer combined = new Layer();
      //Loop through the entire array setting values the newTop value if they are not null
      for(int col = 0; col < resolution[0]; col++){
         for(int row = 0; row < resolution[1]; row++){
            //Get the new value and if not null set it as the pixel, otherwise use the old
            Color topPixel = newTop.getPixel(col, row);
            Color basePixel = this.getPixel(col, row);
            
            //set the combined pixel
            if(topPixel != null){
               combined.setPixel(col, row, topPixel);
            } else {
               combined.setPixel(col, row, basePixel);
            }
         }
      }
      
      return combined;
   }
   
   /**
    * Convert this layer to a canvas and return it.
    * @return Canvas - This layer as a canvas
    */
   public Canvas toCanvas(){
      Canvas output = new Canvas(resolution[0], resolution[1]);
      GraphicsContext gc = output.getGraphicsContext2D();
      PixelWriter pw = gc.getPixelWriter();
      
      //Loop through and set the pixel values
      for(int row = 0; row < resolution[1]; row++){
         for(int col = 0; col < resolution[0]; col++){
            Color pixel = this.getPixel(col, row);
            if(pixel != null){
               pw.setColor(col, row, pixel);
            }
         }
      }
      
      return output;
   }
   
   /**
    * Convert this layer to an image and return it. 
    * Then image background if transparent will be left so.
    * @return Image - The image of the canvas
    */
   public Image toImage(){
      SnapshotParameters params = new SnapshotParameters();
      params.setFill(Color.TRANSPARENT);
      Image snapshot = toCanvas().snapshot(params, null);
      return snapshot;
   }
   
   /**
    * Set this layer as the active layer if the active value is true
    * 
    * @param active - boolean, true to make this layer active
    * @see LayerPreview#drawActive();
    */
   public void setActive(boolean active){
      if(active) Session.currentLayer = this;
      previewManager.drawActive(active);
   }
   
   
}
