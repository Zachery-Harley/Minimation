package application;

import application.GUI.AnimationWindow;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class LayerPreview extends AnchorPane{
   
   private Layer layer;
   
   public LayerPreview(Layer layer){
      this.layer = layer;
      
      //Add the events
      this.addEventFilter(MouseEvent.MOUSE_CLICKED, layerClick);
   }
   
   /**
    * Draw the layer on the layer pane, this will need to be refreshed
    * should a layer be edited, this can be done with the refresh method.
    * 
    * @see #refresh()
    */
   public void draw(){
      //Set this size
      this.setMaxSize(200, 75);
      this.setMinSize(200, 75);
      
      this.getStyleClass().add("layer-preview-container");
      AnchorPane layerContainer = AnimationWindow.getLayerPane();
      int layerCount = layerContainer.getChildren().size();
      this.setLayoutX(5);
      this.setLayoutY(layerCount * 85);
      
      //Checkbox and label
      String layerName = layer.getName();
      if(layerName.length() > 12){
         layerName = layer.getName().substring(0, 12);
      }
      CheckBox visability = new CheckBox(layerName);
      visability.setLayoutX(5);
      visability.setLayoutY(27.5);
      visability.getStyleClass().add("layer-preview-check");
      visability.setSelected(true);
      this.getChildren().add(visability);
      
      //Image of the layer
      ImageView preview = new ImageView(layer.toImage());
      preview.setPreserveRatio(true);
      preview.setFitWidth(55);
      //Set a wrapper as image view does not support borders
      BorderPane imageViewWrapper = new BorderPane(preview);
      imageViewWrapper.setLayoutY(8);
      imageViewWrapper.setLayoutX(130);
      
      imageViewWrapper.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
      this.getChildren().add(imageViewWrapper);
      
      layerContainer.getChildren().add(this);
   }
   
   /**
    * Refresh the current layer preview so that it displays an up to date
    * preview on the preview box
    * 
    * This will also refresh the preview of the frame in the frame preview
    * area.
    * 
    * @see Frame#refreshPreview()
    */
   public void refresh(){
	   //Remove the old preview image
	   int layerCount = getChildren().size();
	   getChildren().remove(layerCount-1);
	   
	   //Redraw and add the new image
	   //Image of the layer
	   ImageView preview = new ImageView(layer.toImage());
	   preview.setPreserveRatio(true);
	   preview.setFitWidth(55);
	   //Set a wrapper as image view does not support borders
	   BorderPane imageViewWrapper = new BorderPane(preview);
	   imageViewWrapper.setLayoutY(8);
	   imageViewWrapper.setLayoutX(130);
	   //Add the new preview to the preview box
	   imageViewWrapper.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
	   this.getChildren().add(imageViewWrapper);
	   
	   //Update the frame preview also
	   Session.currentFrame.refreshPreview();
   }
   
   //////////////////////////
   ///  Events  /////////////
   //////////////////////////
   
   /**
    * Layer click event, this will cause the active layer to change
    */
   private EventHandler<MouseEvent> layerClick = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
         //Set this layer to the active layer
         Session.currentFrame.setActiveLayer(layer);
      }
   }; 
   
   //////////////////////////
   ///  Methods /////////////
   //////////////////////////
   
   /**
    * Draw this layer as the given active state. True will draw this layer as the active
    * layer, false as an ordinary layer.
    * @param active - The active state of this layer, true for active, false otherwise
    */
   public void drawActive(boolean active){
      this.getStyleClass().clear();
      if(active){
         //Active
         this.getStyleClass().add("layer-preview-container_active");
      } else {
         //Not active
         this.getStyleClass().add("layer-preview-container");
      }
   }

}
