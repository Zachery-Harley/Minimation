package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Util {

   public static File userGetFile(String title, String extensionDesc, String extenstion, boolean save){
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(title);
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extensionDesc, extenstion);
      fileChooser.getExtensionFilters().add(extFilter);

      Stage stage = new Stage();
      File file = null;
      if(save){
         file = fileChooser.showSaveDialog(stage);
      } else {
         file = fileChooser.showOpenDialog(stage);
      }
      if (file != null) {
          System.out.println(file.toString());
      }
      return file;
   }
   
   
   public static String stringDialog(String header, String message){
      
      TextInputDialog dialog = new TextInputDialog(null);
      dialog.setTitle(header);
      dialog.setHeaderText(message);
      //Show the dialog
      Optional<String> results = dialog.showAndWait();
      String entered = null;
      
      if(results.isPresent()){
         entered = results.get();
      }
         
      return entered;
      
   }
   
   
}
