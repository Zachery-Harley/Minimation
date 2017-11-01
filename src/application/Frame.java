package application;

import java.util.Vector;

import application.GUI.AnimationWindow;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Frame represents a frame in the animation and contains
 * layers that are combined to build up the frame.
 * 
 * Frames can be created and duplicated from here and also
 * the active frame is set from this class.
 * 
 * @see Layer
 * @see Animation#newFrame()
 * @author zachery harley
 *
 */
public class Frame extends BorderPane{

   //////////////////////////
   /// Instance Variables ///
   //////////////////////////
   
   //The colors that will be each pixel
   private Vector<Layer> layers = new Vector<Layer>(2);
   
   ImageView previewImage;
   BorderPane borderPane;
   Frame self;//The frameClick event cannot call 'this'
   
   //////////////////////////
   /// Constructors       ///
   //////////////////////////
   
   /**
    * Create a new frame with two layers, a background with a white background
    * and a second transparent layer.
    */
   public Frame() {
      //Create 2 new layers a background layer and a layer to draw on
      newLayer("Background","0xFFFFFFFF");
      newLayer();
      drawPreview();
      drawLayerPreviews();
      self = this;
   }
   
 
   
   //////////////////////////
   /// Methods            ///
   //////////////////////////
   
   /**
    * Get the collection of layers from the frame. They layers are returned
    * as a vector
    * @return Vector<Layer>   The layers from the frame.
    */
   public Vector<Layer> getLayers(){
      return this.layers;
   }
   
   /**
    * Create a new layer and set all pixels to the backgroundColor
    * adding it to this frames layers.
    * 
    * @param layerName    The name of the new layer
    * @param backgroundColor    The background color to assign the layer
    * @return Layer - The new layer
    */
   public Layer newLayer(String layerName, String backgroundColor){
      //Create the new layer
      Layer newLayer = newLayer(layerName);
      
      //Set the base color of the layer
      newLayer.setAllPixels(backgroundColor);
      return newLayer;
   }
   
   /**
    * Create a new layer and set the value of each pixle to be that of the
    * given layer. This will duplicate the given layer
    * @param dupLayer
    * @return Layer - The layer created
    */
   public Layer newLayer(Layer dupLayer){
      //Create the new layer
      Layer newLayer = newLayer();
      
      //Remove the layer from the layers list
      layers.remove(newLayer);
      String layerName = dupLayer.getName();
      
      //Get the position of the dupLayer
      int position = layers.indexOf(dupLayer) + 1;
      
      //Set the colors to be that of dupLayer
      newLayer = newLayer.combine(dupLayer);
      newLayer.setName(layerName += "(DUP)");
      layers.add(position, newLayer);
      
      return newLayer;
   }
   
   /**
    * Create a new layer and set name to next default value,
    * this will then be added to this frames layers.
    * @return Layer - The layer created
    */
   public Layer newLayer(){
      //Create the new layer name.
      String name = "Layer " + layers.size();
      return newLayer(name);
   }
   
   /**
    * Create a new layer adding it to this frames layers.
    * 
    * @param layerName    The name of the new layer
    * @return Layer - The new layer
    */
   public Layer newLayer(String layerName){
      //Create the new layer
      Layer newLayer = new Layer(layerName);
      layers.addElement(newLayer);
      //Set the new layer active
      setActiveLayer(newLayer);
      //Return the new layer
      return newLayer;
   }
   
   /**
    * Remove a layer from the frame and refresh the UI to reflect this
    * @param layer - The layer to be deleted
    */
   public void remLayer(Layer layer){
      //If active layer is the removed layer set it to null
      if(Session.currentLayer == layer){
         Session.currentLayer = null;
      }
      layers.remove(layer);
      //Refresh the canvas
      PixelCanvas.getInstance().refreshCanvas();
      //Redraw the previews
      drawLayerPreviews();
   }
   
   /**
    * Shift the given layer in the list of layers, each shift will only
    * allow the shift of one position and a shift cannot move out of bounds
    * of the vector.
    * @param layer - The layer to shift
    * @param up - True if the layer is to be shifted up, note that layers are
    * drawn from the bottom up.
    */
   public void shiftLayer(Layer layer, boolean up){
      //Get the layers current position
      int position = layers.indexOf(layer);
      //if the position is -1 then return -1 as the layer was not found
      if(position == -1){
         Settings.printError("Frame->shiftLayer: layer is null");
         return;
      }
      //Set the new position
      if(up){
         position++;
      } else {
         position--;
      }
      //Check boundaries
      layers.remove(layer);
      if(position > layers.size()){
         position = layers.size();
      }
      if(position < 0){
         position = 0;
      }
      
      //Set the new position
      layers.add(position, layer);
      //Redraw the ui
      PixelCanvas.getInstance().refreshCanvas();
      drawLayerPreviews();
   }
   
   /**
    * Get the colors for each pixel after stacking the layers, this will take the topmost color
    * of each pixel on the stack of layers.
    * 
    * @return Layer - The combined layers
    */
   public Layer flatten(){
      int layerCount = layers.size();
      Layer flattened = new Layer();
      //Loop through each layer where 0 is the bottom and n is the top
      for(int i = 0; i < layerCount; i++){
         flattened = flattened.combine(layers.get(i));
      }
      
      return flattened;
   }
   
   public Color flattenPixel(int col, int row) {
      int layerCount = layers.size();
      Color output = null;
      for(int i = 0; i < layerCount; i++){
         Color fetched = layers.get(i).getPixel(col, row);
         if(fetched != null){
            output = fetched;
         }
      }
      return output;
   }
   
   /**
    * Draw the preview of the frame on the frame display
    */
   public void drawPreview(){
      //Create a new canvas
      Image preview = flatten().toImage();
      previewImage = new ImageView(preview);
      borderPane = new BorderPane(previewImage);
     
      previewImage.setImage(preview);
      previewImage.setPreserveRatio(true);
      previewImage.setFitWidth(100);

      //Set x offset and the y offset for the preview
      int frameIndex = AnimationWindow.getFramePane().getChildren().size();
      borderPane.setLayoutX(110 * frameIndex);
      borderPane.setLayoutY(10);
      
      if(Session.currentFrame == this){
         //Draw a border on this frame
         borderPane.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
      } else {
         borderPane.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
      }
            
      //get the pane to draw the preview on
      AnimationWindow.getFramePane().getChildren().add(borderPane);
      //Add the click event
      borderPane.addEventFilter(MouseEvent.MOUSE_CLICKED, frameClick);
   }
   
   /**
    * Refresh the preview of the frame, this will not modify the order in
    * which frames are viewed. Before this is called, drawPreview MUST be
    * called.
    * 
    * @see #drawPreview()
    */
   public void refreshPreview(){
      //Refresh the image
      Image preview = flatten().toImage();
      previewImage.setImage(preview);
      
      //Check the border status
      if(Session.currentFrame == this){
         //Draw a border on this frame
         borderPane.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
      } else {
         borderPane.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
      }
   }
   
   /**
    * Clear the current layers from the layer pane and the
    * redraw each layer from this frame in the layer pane.
    * The top layer will be on the top of the list and the
    * bottom layer will be bottom of the list.
    */
   public void drawLayerPreviews(){
      AnimationWindow.getLayerPane().getChildren().clear();
      for(int c = layers.size()-1; c >=0; c--){
         layers.get(c).getPreviewManager().draw();
      }
   }
   
   /**
    * Set the active frame in this frame.
    * 
    * @param newActive - Layer, the layer to be made active
    */
   public void setActiveLayer(Layer newActive){
      //Loop through each layer and set the matching layer active
      for(Layer layer : layers){
         if(layer == newActive){
            layer.setActive(true);
         } else {
            layer.setActive(false);
         }
      }
   }
   
   /////////////////
   //// Events /////
   /////////////////
   
   public EventHandler<MouseEvent> frameClick = new EventHandler<MouseEvent>() {
      
      @Override
      public void handle(MouseEvent event) {
         Session.currentAnimation.setActiveFrame(self);
      }
   };


   
   
}
