package application;

import application.GUI.AnimationWindow;
import javafx.scene.paint.Color;

public class Session {

   ////////////////
   // Constants. //
   ////////////////

   //Store your constants here if you have any

   //////////////////////
   // Class variables. //
   //////////////////////

   public static Color currentColour = Color.web("0xFFFFFFFF"); //The current color being used
   public static Pallet activePallet = null;
   
   //Current data
   public static Layer currentLayer = null;
   public static Frame currentFrame = null;
   public static Animation currentAnimation = null;
   

   ////////////////////
   // Class methods. // 
   ////////////////////

   /**
    * Set the current color and update any of the display data while doing so
    * 
    * @param buttonColor - The new current color
    * @see AnimationWindow#setColorPicker(Color)
    */
   public static void setCurrentColor(Color newColor) {
      currentColour = newColor;
      
      //Set the color picker to show the current color
      AnimationWindow.setColorPicker(currentColour);
   }

   /////////////////////////
   // Instance variables. //
   /////////////////////////

   //Variables that are local to the class

   ///////////////////
   // Constructors. //
   ///////////////////

   //Constructors

   ////////////////////////////
   // Read/Write properties. //
   ////////////////////////////

   //Methods to get and set instance variables

   ///////////////////////////
   // Read-only properties. //
   ///////////////////////////

   //Methods to access read only properties

   //////////////
   // Methods. //
   //////////////

   //Normal methods
   
}
