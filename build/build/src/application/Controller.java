package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {
    
    //FXML
    
    @FXML Button button = new Button(); 
    @FXML TextField fileName = new TextField();
    
    
    @FXML private TableView<Words> table;
    @FXML private TableColumn<Words, String> word; 
    @FXML private TableColumn<Words, Integer> occurrence; 
    
    @FXML ProgressBar progressBar = new ProgressBar();
   
    public ObservableList<Words> listW = FXCollections.observableArrayList();
 
    FileInputStream inputStream = null;
    Scanner sc = null;
    FileChooser fileChooser = new FileChooser();

   
    public void chooseButton(ActionEvent e) {
        
        long start = new Date().getTime();

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(new Stage());

        StringBuilder contentBuilder = new StringBuilder();
       
        BufferedReader br;
        
        String path = file.getPath();
        File f = new File(path);
        long fileSize = f.length();
        double lengthPerPercent = 100.0 / fileSize;
        long readLength = 0;
        
        System.out.format("The size of the file: %d bytes", fileSize);

        if (file != null) {
    
            try {
                
  /*            Read file with Scanner
   *  
                inputStream = new FileInputStream(file);
                sc = new Scanner(inputStream, "UTF-8");
                String fileContent="";
                while (sc.hasNextLine()) {
                    fileContent = fileContent.concat(sc.nextLine() + " ");  
                } */
                
                listW.clear();
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"));
                String currentLine = null;

                while ((currentLine = br.readLine()) != null) {
                    contentBuilder.append(currentLine).append(" ");       
                }

                String fileContent = (contentBuilder.toString()).toLowerCase().replaceAll("\\s{2,}", " ").trim();
                fileContent = fileContent.replaceAll("\\s{2,}", " ").trim();
                String words[] = fileContent.split(" ");
                List<String> listOfWords = Arrays.asList(words);
                
                Map<String, Integer> map = new HashMap<>();

                for (int i = 0; i < words.length; i++) {
                    map.put(words[i], Collections.frequency(listOfWords, words[i]));
                }

                for (String i : map.keySet()) {
                    
                     listW.add(new Words(i, map.get(i)));
                }
   
                occurrence.setSortType(TableColumn.SortType.DESCENDING);
                table.getSortOrder().addAll(occurrence);
                
                long end = new Date().getTime();
                long time = end - start;
                System.out.println("Scanner Time Consumed => " + time);
                
                br.close();

                
            } catch (IOException ioe) {

                ioe.printStackTrace();
            }
                 
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        fileChooser.setInitialDirectory(new File("/Users/Hp/Downloads/")); 
        word.setCellValueFactory(new PropertyValueFactory<Words, String>("word"));
        occurrence.setCellValueFactory(new PropertyValueFactory<Words, Integer>("occurrence"));
        table.setItems(listW);
        }

    public void clear(ActionEvent e) {
        System.out.println("Clear");
        listW.clear();
    }

}
