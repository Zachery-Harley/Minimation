package application;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PixelCanvas extends Pane {

   ////////////////
   // Constants. //
   ////////////////

   private final static double zoomScaleMin = 0.5;
   private final static double zoomScaleMax = 10;

   //////////////////////
   // Class variables. //
   //////////////////////

   private static Pixel canvasPixels[][];
   private static PixelCanvas pixelCanvas;
   private static int width, height;
   

   ////////////////////
   // Class methods. // 
   ////////////////////

   //these are the static methods that are part of the class

   /////////////////////////
   // Instance variables. //
   /////////////////////////

   private double zoomScale = 1.0;
   private boolean dragging = false;
   private Pixel lastPixel; //The last places pixel when dragging

   ///////////////////
   // Constructors. //
   ///////////////////

   public PixelCanvas(int width, int height) {
      this.height = height;
      this.width = width;
      
      //Set the event listeners
      this.setOnScroll(scrollZoom);
      this.addEventFilter(MouseEvent.DRAG_DETECTED, canvasPixelDrag);
      this.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseRelease);
      pixelCanvas = this;
      
      buildCanvas();
   }

   ////////////////////////////
   // Read/Write properties. //
   ////////////////////////////

   /**
    * Zoom in by increasing the scale of the grid pane
    */
   public void zoomIn(){
      zoomScale += 0.2;
      if(zoomScale  > zoomScaleMax){
         zoomScale = zoomScaleMax;
      }
      //Set the scale
      this.setScaleX(zoomScale);
      this.setScaleY(zoomScale);
   }
   
   /**
    * Zoom out by decreasing the scene of the grid pane
    */
   public void zoomOut(){
      zoomScale -= 0.2;
      if(zoomScale  < zoomScaleMin){
         zoomScale = zoomScaleMin;
      }
      //Set the scale
      this.setScaleX(zoomScale);
      this.setScaleY(zoomScale);
   }

   ///////////////////////////
   // Read-only properties. //
   ///////////////////////////

   /**
    * Get the instance of the pixel canvas
    * @return
    */
   public static PixelCanvas getInstance(){
      return pixelCanvas;
   }

   //////////////
   // Methods. //
   //////////////
   
   /**
    * Set the color value of a pixel on the canvas, this does not effect any
    * layers and should only be called from layers classes. 
    * @param x - The x position of the pixel
    * @param y - The y position of the pixel
    * @param color - The new color of the pixel
    */
   public void setPixel(int x, int y, Color color){
      canvasPixels[x][y].setFill(color);
   }
   
   /**
    * Build the canvas creating and storing the pixels for the canvas.
    */
   private void buildCanvas(){
      canvasPixels = new Pixel[width][height];
      //Build each pixel
      for(int x = 0; x < width; x++){
         for(int y = 0; y < height; y++){
            Pixel pix = new Pixel(10, 10, x ,y);
            pix.setStroke(Color.DARKGRAY);
            canvasPixels[x][y] = pix;
            
            //Add the event handlers
            pix.addEventHandler(MouseEvent.MOUSE_CLICKED, this.canvasPixelClick);
            pix.setOnMouseDragEntered(this.canvasPixelClick);
            
            getChildren().add(pix);
         }
      }
   }
   
   /**
    * The controller that handles the zoom method
    * ctrl + scroll
    */
   private EventHandler<ScrollEvent> scrollZoom = new EventHandler<ScrollEvent>() {
      @Override
      public void handle(ScrollEvent event) {
         if(event.isControlDown()){
            if(event.getDeltaY() < 0){
               //Zoom out
               zoomOut();
            } else {
               zoomIn();
            }
         }
      }
   };
   
   /**
    * Event for when the mouse is released refresh the current layer, this is a
    * big chunk of ugly code that could be improved upon later.
    * 
    * This will disable the dragging state.
    * TODO Make nicer
    */
   private EventHandler<MouseEvent> mouseRelease = new EventHandler<MouseEvent>() {
      @Override
		public void handle(MouseEvent event) {			
			//Run the created task
			Thread refresh = new Thread(new Runnable() {
            @Override
            public void run() {
               //Small delay to ensure the changes have taken effect before redrawing
               try {
                  Thread.sleep(200);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                     if(Session.currentLayer != null){
                        Session.currentLayer.getPreviewManager().refresh();
                     }
                  }
               });
               
            }
         });
			//Start the thread
			refresh.start();
			dragging = false;
      }
	};
   
   public void resetLine(){
      refreshCanvas();
   }
   
   private EventHandler<MouseEvent> canvasPixelDrag = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
         Main.getActiveScene().startFullDrag();
         dragging = true;
         lastPixel = null;
      }
   };
   
   /**
    * When the pixel panes are clicked this event is triggered
    */
   public EventHandler<MouseEvent> canvasPixelClick = new EventHandler<MouseEvent>() {
      
      @Override
      public void handle(MouseEvent event) {
         long startTime = System.nanoTime();
         Pixel source = (Pixel)event.getSource();
         
         //Get the mouse button
         MouseButton click = event.getButton();
         if(click == MouseButton.PRIMARY){
            //Set the color of the pixel in the current layer
            int col = source.getXPos();
            int row = source.getYPos();
            
            //Set the pixel layer
            if(Session.currentLayer != null){
               Session.currentLayer.setPixel(col, row, Session.currentColour);
               refreshPixel(source);
               
               
               //Check if dragging and fill in any missing pixels
               if(dragging){
                  if(lastPixel != null){
                     //Get the distance of the last pixel to this pixel, if > 1 then fill in the missing pixels
                     int disX = Math.abs(lastPixel.getXPos() - source.getXPos());
                     int disY = Math.abs(lastPixel.getYPos() - source.getYPos());
                     if(disX > 1 || disY > 1){
                        //Fill in the missing pixels
                        Tools.line(lastPixel.getXPos(), lastPixel.getYPos(), source.getXPos(), source.getYPos());
                     }
                     
                  }
                  lastPixel = source;
               }
            } else {
               System.out.println("No Active Layer!");
               //TODO Error handling
            }
         }
         long endTime = System.nanoTime();
         double duration = (endTime - startTime);
         Settings.printDebug("Time:" + duration/1000000);
      }
   };
   
   /**
    * Given a single pixel, this will update the value of that single pixel with the flattened
    * value of the pixel in that location.
    * 
    * @param pixel - The pane pixel to update
    * @see Frame#flatten()
    */
   private void refreshPixel(Pixel pixel){
      int col = pixel.getXPos();
      int row = pixel.getYPos();
      
      //get the color for that pixel
      Color newColor = Session.currentFrame.flattenPixel(col, row);
      Color buttonColor = null;
      buttonColor = (Color)pixel.getFill();
      
      if(!newColor.equals(buttonColor)){
         pixel.setFill(newColor);
      }
   }
   
   /**
    * Set the color for each pixel on the canvas, this is
    * used to update the canvas after any MAJOR change as
    * it will consume more processing time.
    * 
    * Also updates the frame preview to reflect the changes
    * made to the canvas.
    * @see Frame#refreshPreview()
    */
   public void refreshCanvas(){
      //Get the flattened view currently
      Layer flattened = Session.currentFrame.flatten();
      
      //Loop through each child in the grid pane and set the pixel accordingly,
      //this is done not always in the correct order however by iterating through
      //child nodes is faster.
      for(Node node : getChildren()){
         Pixel pix = (Pixel)node;
         //get the x and y of that node
         int x = pix.getXPos();
         int y = pix.getYPos();
         //Get the color that the node needs to be
         Color nodeColor = flattened.getPixel(x, y);
         if(nodeColor == null){
            nodeColor = Color.TRANSPARENT;
         }
         
         pix.setFill(nodeColor);
      }
      
      //Redraw the frame preview to reflect these changes
      Session.currentFrame.refreshPreview();
   }
   
}
