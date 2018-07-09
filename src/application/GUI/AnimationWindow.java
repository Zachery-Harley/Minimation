package application.GUI;

import java.io.IOException;
import java.util.ArrayList;

import application.Animation;
import application.Demo;
import application.PixelCanvas;
import application.Layer;
import application.Main;
import application.Session;
import application.Settings;
import application.Pallet;
import application.Util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AnimationWindow {

   ////////////////
   // Constants. //
   ////////////////

	//TODO Speerate pallet to own object fully
   public final static CornerRadii palletCorner = new CornerRadii(5);
   //TODO remove placeholder
   public Layer placeholder;

   //////////////////////
   // Class variables. //
   //////////////////////

   
   private static AnimationWindow controllerInstance; //The controller for the animation window
   private static boolean loaded = false; //True when the controller has been loaded
   //TODO fix what ever DEMO is
   private static Demo demo = null;
   
   ////////////////////
   // Class methods. // 
   ////////////////////

   /**
    * Set the main stage scene to be this window. This will only set the scene not enable show on the stage.
    * The stage is that from the main class.
    * 
    * @see Main.mainStage
    */
   public static void show(){
      Scene newScene = new Scene(controllerInstance.base_pane, 600, 400);
      Main.mainStage.setScene(newScene);
   }
   
   /**
    * Set the value of the color picker to be the new color,
    * 
    * @param newColor    The new color that is to be set and the current color on the picker
    * @see Session#setCurrentColor(Color)
    */
   public static void setColorPicker(Color newColor){
      //Set the color picker
      controllerInstance.sel_colorPicker.setValue(newColor);
      setColorText(newColor);
   }
   
   /**
    * Set the value of the color text box to the correct WEB color
    * 
    * @param newColor    The new color to show
    */
   public static void setColorText(Color newColor){
      //Get the string of the color
      String sColor = newColor.toString();
      String newValue = "#";
      //Set the value
      newValue += sColor.substring(2, 8);
      controllerInstance.txt_color.setText(newValue);
   }
   
   public static StackPane getCanvasBase(){
      return controllerInstance.pane_canvas_base;
   }

   /////////////////////////
   // Instance variables. //
   /////////////////////////

   @FXML
   private Text lbl_pallet_name;
   @FXML
   private Button btn_new_layer;
   @FXML
   private ColorPicker sel_colorPicker;
   @FXML
   private Button btn_up_layer;
   @FXML
   private Button btn_del_layer;
   @FXML
   private Button btn_dup_layer;
   @FXML
   private Button btn_sequence_del;
   @FXML
   private Button btn_new_frame;
   @FXML
   private GridPane grid_pallet;
   @FXML
   private Button btn_pallet_load;
   @FXML
   private BorderPane base_pane;
   @FXML
   private AnchorPane pane_frames;
   @FXML
   private TextField txt_demo_fps;
   @FXML
   private Button btn_dup_frame;
   @FXML
   private TextField txt_color;
   @FXML
   private Button btn_demo_play;
   @FXML
   private AnchorPane pane_layers;
   @FXML
   private Button btn_pallet_save;
   @FXML
   private Button btn_del_frame;
   @FXML
   private Button btn_demo_stop;
   @FXML
   private Button btn_down_layer;
   @FXML
   private Button btn_sequence_new;
   @FXML
   private ImageView img_demo_view;
   @FXML
   private ChoiceBox<String> sel_sequence;
   @FXML
   private StackPane pane_canvas_base;
   
   ArrayList<Button> palletButtons;
   PixelCanvas canvasHolder;
   
   //Animation Window
   ArrayList<Animation> animationSet = new ArrayList<Animation>();

   ///////////////////
   // Constructors. //
   ///////////////////

   public AnimationWindow() {
      //If loaded then set the instance, otherwise load it
      if(!loaded){
         try{
            
            //Set loaded to be true
            loaded = true;
            
            //Load the animation window
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Main.fxml"));
            loader.load();
            makePalletButton();
            initPalletButtons();
         }
         catch(IOException e){
            //TODO error handling
            e.printStackTrace();
         }
      }
      else {
         
         if(controllerInstance == null){
            //Set the controller
            controllerInstance = this;
         }
      }
      
   }

   ////////////////////////////
   // Read/Write properties. //
   ////////////////////////////

   //Methods to get and set instance variables

   ///////////////////////////
   // Read-only properties. //
   ///////////////////////////

   /**
    * Get the pane on which the frames are drawn.
    * 
    * @return AnchorPane - The frame anchor pane
    */
   public static AnchorPane getFramePane(){
      return controllerInstance.pane_frames;
   }
   
   /**
    * Get the pane on which the layers are drawn
    * 
    * @return AnchorPane - The layer anchor pane
    */
   public static AnchorPane getLayerPane(){
      return controllerInstance.pane_layers;
   }
   
   /**
    * Get the image view where the demo is displayed
    * 
    * @return ImageView   The image view
    */
   public static ImageView getDemoImage(){
      return controllerInstance.img_demo_view;
   }

   //////////////
   // Methods. //
   //////////////

   
   @FXML
   void change_active_seq(ActionEvent event) {
      System.out.println("Sequence Changed");
   }

   @FXML
   void click_seq_new(ActionEvent event) {
      System.out.println("New Sequence");
      
      //Get the name for the sequence 
      String seqName = Util.stringDialog("New Sequence", "Enter a name for the sequence");
      
      //if there is a name then create the new sequence and add it to the list
      if(seqName != null){
         Animation newAnimation = new Animation(20, 20, seqName);
         sel_sequence.getItems().add(seqName);
         sel_sequence.setValue(seqName);
         animationSet.add(newAnimation);
         //Set the new sequance as the current sequence
         Session.currentAnimation = newAnimation;
         //Draw the new canvas
         canvasHolder.refreshCanvas();
      }
   }

   @FXML
   void click_seq_del(ActionEvent event) {
      System.out.println("Deleted Sequence");
   }

   @FXML
   void click_frame_new(ActionEvent event) {
      System.out.println("New Frame");
      
      Session.currentAnimation.newFrame();
   }

   @FXML
   void click_frame_del(ActionEvent event) {
      System.out.println("Deleted  Frame");
   }

   @FXML
   void click_frame_dup(ActionEvent event) {
      System.out.println("Duplicated frame");
   }

   @FXML
   void change_color_text(ActionEvent event) {
      System.out.println("Update color from HEX text");
   }

   @FXML
   /**
    * Save the current pallet to the pallets specified location
    * 
    * @param event
    * @see Pallet#save()
    */
   void click_save_pallet(ActionEvent event) {
      System.out.println("Save Pallet");
      
      //Check if the current pallet is null
      if(Session.activePallet == null){
         Session.activePallet = new Pallet();
         Session.activePallet.save(palletButtons);
         
      } else {
         //Save the pallet
         Session.activePallet.save(palletButtons);
      }
      
      //Set the name of the pallet
      if(Session.activePallet.getName() != null){
         lbl_pallet_name.setText(Session.activePallet.getName());
      } else {
         //Set it to untitled
         lbl_pallet_name.setText("Untitled");
      }
   }
   
   @FXML
   /**
    * Create a new pallet, this is done by setting the current pallet to null
    * 
    * @param event
    */
   void click_new_pallet(ActionEvent event){
      System.out.println("New Pallet");
      Session.activePallet = null;
      lbl_pallet_name.setText("Untitled");
   }

   @FXML
   void click_pallet_open(ActionEvent event) {
      System.out.println("Load Pallet");
      
      Pallet.load(palletButtons);
      //Set the name of the pallet
      if(Session.activePallet.getName() != null){
         lbl_pallet_name.setText(Session.activePallet.getName());
      } else {
         //Set it to untitled
         lbl_pallet_name.setText("Untitled");
      }
   }

   @FXML
   void click_demo_start(ActionEvent event) {
      System.out.println("Start the demo");
      //Get the value from the FPS
      String fpsString = txt_demo_fps.getText();
      if(fpsString.equals("")){
         fpsString = "10";
      }
      int fps = Integer.parseInt(fpsString);
      if(fps > 0){
         demo = new Demo(fps, Session.currentAnimation);
      }
   }

   @FXML
   void click_demo_stop(ActionEvent event) {
      System.out.println("Stop the demo");
   }

   @FXML
   void click_layer_new(ActionEvent event) {
      Settings.printDebug("New Layer");
      Session.currentFrame.newLayer();
      
      //Refresh the previews
      Session.currentFrame.drawLayerPreviews();
   }

   @FXML
   void click_layer_rem(ActionEvent event) {
      //Remove the current layer
      Session.currentFrame.remLayer(Session.currentLayer);
      
      System.out.println("Remove a layer");
   }

   @FXML
   void click_layer_dup(ActionEvent event) {
      System.out.println("Duplicate a layer");
      Session.currentFrame.newLayer(Session.currentLayer);
      
      //Refresh the previews
      Session.currentFrame.drawLayerPreviews();
   }

   @FXML
   void click_layer_up(ActionEvent event) {
      System.out.println("Move layer up");
      
      Session.currentFrame.shiftLayer(Session.currentLayer, true);
   }

   @FXML
   void click_layer_down(ActionEvent event) {
      System.out.println("Move layer down");
      
      Session.currentFrame.shiftLayer(Session.currentLayer, false);
   }
   
   /**
    * When the pallet buttons are clicked this event is triggered
    */
   private EventHandler<MouseEvent> colourPalletClick = new EventHandler<MouseEvent>() {
      
      @Override
      public void handle(MouseEvent event) {
         Button source = (Button)event.getSource();
         
         //Get the mouse button
         MouseButton click = event.getButton();
         if(click == MouseButton.PRIMARY){
            //Set teh color of the painter from the color of the button
            Background buttonBG = source.getBackground();
            Color buttonColor = (Color)buttonBG.getFills().get(0).getFill();
            Session.setCurrentColor(buttonColor);
         } else {
            //Set the color of the button
            BackgroundFill base = new BackgroundFill(Session.currentColour, palletCorner, Insets.EMPTY);
            Background newBG = new Background(base);
            source.setBackground(newBG);
         }
      }
   };


   
   @FXML
   /**
    * Called when the color picker color is changed and will set the current color in the painter class
    * @param event
    * 
    * @see Painter
    */
   void colour_changed(ActionEvent event) {
      System.out.println("Colour Changed");
      
      //Set the current color
      Color c = sel_colorPicker.getValue();
      Session.currentColour = c;

      setColorText(c);
   }
   

   
   /**
    * Initialize the buttons to have the correct listeners and all to have a white background and black border
    * This only effects the pallet buttons
    * 
    * @see #makePalletButton()
    */
   private void initPalletButtons(){
      ArrayList<Button> pallet = controllerInstance.palletButtons;
      for(Button tile : pallet){
         //Set the button style and controller
         tile.addEventHandler(MouseEvent.MOUSE_CLICKED, colourPalletClick);
         //Set the button background
         BackgroundFill base = new BackgroundFill(Color.WHITE, palletCorner, Insets.EMPTY);
         Background newBG = new Background(base);
         tile.setBackground(newBG);
      }
   }
   
   /**
    * Create the buttons that will be used as the pallet.
    * Buttons are set to the default style pallet-button
    * However for the buttons to work, initPalletButtons must be called.
    * 
    * @see #initPalletButtons()
    */
   private void makePalletButton(){
      //Initialize the button array
      controllerInstance.palletButtons = new ArrayList<Button>();
      
      for(int row = 0; row < 5; row++){
         for(int col = 0; col < 7; col++){
            //make the new button and then add it to the array list
            Button newButton = new Button();
            newButton.getStyleClass().add("pallet-button");
            controllerInstance.grid_pallet.add(newButton, col, row);
            controllerInstance.palletButtons.add(newButton);
         }
      }
   }
   
   /**
    * Return the controller instance of the UI.
    * @return AnimationWindow - The controller instance
    */
   public static AnimationWindow getInstance(){
      return controllerInstance;
   }
   
   /**
    * Draw on the screen the buttons that will act as the canvas.
    * The draw buttons will be added to an array list 
    * @param x    The number of pixels in the X
    * @param y    The number of pixels in the Y
    * @param minAxis    The size of the smallest axis that the canvas has to fit inside of.
    */
   public void makeCanvasButtons(int x, int y){
      
      PixelCanvas canvasContainer = new PixelCanvas(x, y);
      Group canvasGroup = new Group(); //group is used so the scroll pane knows the size of the pane
      
      /*
      //Set up a temporary array list to store the buttons while they are made
      ArrayList<Pane> canvas = new ArrayList<Pane>();
      
      //Setup the gridpane
      PixelCanvas canvasContainer = new PixelCanvas();
      Group canvasGroup = new Group(); //group is used so the scroll pane knows the size of the gridpane
      //calculate the size of the buttons and then generate them
      int maxSize = (x < y) ? y : x;
      double size = minAxis / maxSize;
      for(int row = 0; row < y; row++){
         for(int col = 0; col < x; col++){
            //Make the button
            Pane pixel = new Pane();
            
            //Add the event handlers
            pixel.addEventHandler(MouseEvent.MOUSE_CLICKED, canvasContainer.canvasPixelClick);
            pixel.setOnMouseDragEntered(canvasContainer.canvasPixelClick);
            
            pixel.getStyleClass().add("canvas-button");
            pixel.setMinSize(size, size);
            pixel.setMaxSize(size, size);
            
            //add to the grid pane
            canvasContainer.add(pixel, col, row);
            canvas.add(pixel);
         }
      }
      */
      controllerInstance.canvasHolder = canvasContainer;
      
      //center the grid pane
      StackPane.setAlignment(canvasContainer, Pos.CENTER);
      StackPane.setAlignment(canvasGroup, Pos.CENTER);
      canvasGroup.getChildren().add(canvasContainer);
      //Draw the container
      controllerInstance.pane_canvas_base.getChildren().add(canvasGroup);
      
   }
   
   
}
