package application;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import application.GUI.AnimationWindow;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Demo {

   public int fps = 0;
   public Animation anim = null;
   public ArrayList<Image> frames;
   public boolean running = false;
   private static Timer timer;
   
   public Demo(int fps, Animation currentAnimation) {
      //Start the demo in a new thread
      this.fps = fps;
      this.anim = currentAnimation;
      timer = new Timer();
      timer.schedule(new Animate(), 0, 200);
      
      int count = generateFrames();
      if(count > 0){
         Settings.printDebug("Start animation");
         startAnimation();
      }
   }
   
   
   
   class Animate extends TimerTask{

      @Override
      public void run() {
         Settings.printDebug("Test");
      }
      
   }
   
   
   ////////////////////\\\\\\\\\\\\\\\\\\\\
   ////////////////////Methods\\\\\\\\\\\\\\\\\\\\
   ////////////////////\\\\\\\\\\\\\\\\\\\\
   
   public void stopAnimation(){
      
   }
   
   public void startAnimation(){
      if(running){
         //Stop the current animation
         timer.cancel();
      }
      
      /*Thread animation = new Thread(new Runnable() {
         
         @Override
         public void run() {
            while(running){
               //Play the animation at the set fps;
               ImageView demoView =  AnimationWindow.getDemoImage();
               
               
               try {
                  Thread.sleep(500);
               } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
         }
      });*/

      
      
   }

   
   /**
    * Convert the frames from the animation to images stores in the
    * global variable 'frames'. The number of converted frames is returned
    * as an int.
    * @return int   The number of converted frames
    * 
    * @see Frame#flatten()
    * @see Layer#toImage()
    * @see #anim
    */
   private int generateFrames(){
      frames = new ArrayList<Image>();
      if(anim != null){
         //Get the frames from the animation and convert them to images
         Vector<Frame> framesRaw = anim.getFrames();
         
         for(Frame f : framesRaw){
            frames.add(f.flatten().toImage());
         }
         
         return frames.size();
      } else {
         Settings.printError("Demo->generateFrames: Animation is null");
         return 0;
      }
   }

}
