package application;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import application.GUI.AnimationWindow;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Pallet {
   ////////////////
   // Constants. //
   ////////////////

   //Store your constants here if you have any

   //////////////////////
   // Class variables. //
   //////////////////////

   public static boolean load(ArrayList<Button> palletButtons){
      
      try{ 
         //Get the file to load
         File palletFile = Util.userGetFile("Load Pallet", "Pallet Files [*.plt]", "*.plt", false);
         if(palletFile == null){
            return false;
         }
         
         //Parse the file
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(palletFile);
      
         doc.getDocumentElement().normalize();
         
         //Get the color list
         NodeList nList = doc.getElementsByTagName("color");
         
         for(int i = 0; i < nList.getLength(); i++){
            
            Node nNode = nList.item(i);
            if(nNode.getNodeType() == Node.ELEMENT_NODE){
               //Get the color element and parse the data from it
               Element eElement = (Element) nNode;
               
               //Set the button background
               Button btn = palletButtons.get(i);
               
               Color loadedColor = Color.web(eElement.getAttribute("value"));
               
               BackgroundFill base = new BackgroundFill(loadedColor, AnimationWindow.palletCorner, Insets.EMPTY);
               Background newBG = new Background(base);
               btn.setBackground(newBG);
            }
            
         }
         
         //Set this as the current pallet
         Pallet newCur = new Pallet();
         newCur.file = palletFile;
         Session.activePallet = newCur;
         newCur.name = getNameFromFile(palletFile);
         
         return true;
         
         
      } catch (Exception e){
         return false;
         //TODO error catch
      }
   }
   
   /**
    * Convert the file path into the name of the the file
    * This will truncate the file if it exceeds 15 characters in length
    * 
    * @param path   The file to convert to a name
    * @return String - The name of the file
    */
   private static String getNameFromFile(File path){
      String output = path.getName();
      int pos = output.lastIndexOf(".");
      if (pos > 0) {
         output = output.substring(0, pos);
      }
      
      //Check length
      if(output.length() > 15){
         output = output.substring(0, 12);
         output += "...";
      }
      return output;
   }

   ////////////////////
   // Class methods. // 
   ////////////////////

   //these are the static methods that are part of the class

   /////////////////////////
   // Instance variables. //
   /////////////////////////

   private File file; //The source of the pallet
   private String name; //The name of the file 
   
   ///////////////////
   // Constructors. //
   ///////////////////

   /**
    * Create a pallet in which to store an array of colors that is stored in the button array
    * list.
    * 
    * @param palletName    The name of the pallet
    */
   public Pallet() {
      //Prompt the user into creating a new pallet
   }

   ////////////////////////////
   // Read/Write properties. //
   ////////////////////////////

   

   ///////////////////////////
   // Read-only properties. //
   ///////////////////////////

   /**
    * Get the name of the pallet
    * 
    * @return String - The name
    */
   public String getName(){
      return this.name;
   }

   //////////////
   // Methods. //
   //////////////

   /**
    * Save the given pallet button collection to an XML file. This will overwrite
    * the file if it already exists without question. If no file exists the user
    * will be prompt on where to store the file
    * @param palletData    The ArrayList<Button> of buttons in the pallet
    * @return boolean - True if the file saved, false otherwise
    */
   public boolean save(ArrayList<Button> palletData){
      try {
         
         //Create an XML file
         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
         
         //Create the document
         Document doc = docBuilder.newDocument();
         Element root = doc.createElement("pallet");
         doc.appendChild(root);
         
         //Add the color data
         for(Button tile : palletData){
            //Get the color value
            Color col = (Color) tile.getBackground().getFills().get(0).getFill();
            
            //Add the color to the xml
            Element color = doc.createElement("color");
            Attr value = doc.createAttribute("value");
            value.setValue(col.toString());
            color.setAttributeNode(value);
            
            root.appendChild(color);
         }
         
         //Write the content to an XML file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(root);
         
         //Check if there is already a file to save to
         if(file == null){
            //Write it after getting the path from the user
            file = Util.userGetFile("Save Pallet", "Pallet Files [*.plt]", "*.plt", true);
            //Check for the null output
            if(file == null){
               return false;
            }
         }
         
         //Save the file
         StreamResult writer = new StreamResult(file);
         transformer.transform(source, writer);
         name = getNameFromFile(file);
         
         return true;
         
      } catch (Exception e){
         //TODO error handling
         return false;
      }
   }
   
}
