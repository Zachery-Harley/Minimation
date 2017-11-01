package application;

import java.util.Vector;

import application.GUI.AnimationWindow;

/**
 * Represent a single animation
 * @author zache
 *
 */
public class Animation {

   //////////////////////////
   /// Class    Variables ///
   //////////////////////////
   //The width and height of the frames in pixels
   private int frameWidth = 20;
   private int frameHeight = 20;
   
   private String name; //The name of the animation
   
   private Vector<Frame> frames = new Vector<Frame>(1); //Create a list of frames
   
   //////////////////////////
   /// Class Methods      ///
   //////////////////////////
   
   //////////////////////////
   /// Instance Variables ///
   //////////////////////////
   
   //////////////////////////
   /// Constructors       ///
   //////////////////////////
   
   /**
    * Create a new animation and give it a start pane
    * 
    * @param x    The width of the frame in pixels
    * @param y    The height of the frame in pixels
    */
   public Animation(int x, int y, String name) {
      //Set the height and the width
      this.frameHeight = y;
      this.frameWidth = x;
      
      this.name = name;
      AnimationWindow.getInstance().makeCanvasButtons(frameWidth , frameHeight);
      Session.currentAnimation = this;
      //Create a new frame
      newFrame();
   }
   
   //////////////////////////
   /// Method             ///
   //////////////////////////
   
   /**
    * Create a new frame and add it to the vector
    * 
    * @return Frame - The new frame
    */
   public Frame newFrame(){
      //Create a new frame and add it to the vector
      Frame newFrame = new Frame();
      frames.addElement(newFrame);
      Session.currentFrame = newFrame;
      
      //Redraw the frame preview section
      drawFramePreviews();
      //Redraw canvas
      PixelCanvas.getInstance().refreshCanvas();
      return newFrame;
   }
   
   /**
    * Get the current frame and return it
    * 
    * @return Frame - The active frame
    */
   public Frame getCurrentFrame(){
      try {
         return Session.currentFrame;
      } catch (Exception e){
         System.out.println("Null Frame!");
         //TODO error handling
         return null;
      }
   }
   
   /**
    * Sets the given frame to the active frame. This will also refresh the
    * GUI to reflect this change. NOTE: If the given frame is not part of
    * this sequence then it will not be set active.
    * @param frame    The frame to be set as the active frame
    */
   public void setActiveFrame(Frame frame){
      //Set the given frame as the active frame
      if(frames.contains(frame)){
         //The frame exists, set the frame to be the current active 
         Frame oldA = Session.currentFrame;
         Session.currentFrame = frame;
         oldA.refreshPreview();
         frame.refreshPreview();
         //Set the active layer to the fist layers if possible
         if(frame.getLayers().size() > 0){
            Session.currentLayer = frame.getLayers().get(0);
         } else {
            Session.currentLayer = null;
         }
         
         
         //Refresh the UI to reflect the changes
         PixelCanvas.getInstance().refreshCanvas();
         frame.drawLayerPreviews();
      } else {
         //The given frame does not exist in this animation
         Settings.printError("Animation->setActiveFrame: The given frame does not exsist for the current animation");
      }
   }
   
   /**
    * Get the frames from the animation. Frames are given in a vector.
    * @return Vector<Frame>   Frames in a vector
    */
   public Vector<Frame> getFrames(){
      return frames;
   }
   
   /**
    * Get the frame width of the animation
    * 
    * @return int - The frame width in pixels
    */
   public int getFrameWidth(){
      return frameWidth;
   }
   
   /**
    * Get the frame height of the animation
    * 
    * @return int - The frame height in pixels
    */
   public int getFrameHeight(){
      return frameHeight;
   }
   
   /**
    * Get the name of this animation sequence
    * @return String - The name of the animation
    */
   public String getName(){
      return this.name;
   }
  
   
   /////////////////////
   ////// Drawing //////
   /////////////////////
   
   /**
    * Draw the frame previews for the current animation, this will first
    * clear the old frames from the preview area.
    */
   public void drawFramePreviews(){
      //Clear the current frame previews
      AnimationWindow.getFramePane().getChildren().clear();
      
      //Redraw each of the frame previews
      for(Frame cur : frames){
         cur.drawPreview();
      }
   }
   
   /**
    * Refresh the frame previews in the preview area of the
    * animation window. This does not redraw the frames so be
    * sure that there are no new or reordered layers as this will
    * not reflect those changes. For those use drawFramePreviews()
    * 
    * @see #drawFramePreviews()
    */
   public void refreshFramePreviews(){
      //Refresh each frame preview
      for(Frame cur : frames){
         cur.refreshPreview();
      }
   }
  
   
}
