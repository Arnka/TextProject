package application;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    @FXML Button cancelButton = new Button();
    
    
    @FXML private TableView<Words> table;
    @FXML private TableColumn<Words, String> word; 
    @FXML private TableColumn<Words, Integer> occurrence; 
    
    @FXML ProgressBar progressBar = new ProgressBar();

    public ObservableList<Words> listW = FXCollections.observableArrayList();
 
    FileInputStream inputStream = null;
    Scanner sc = null;
    FileChooser fileChooser = new FileChooser();
    
    File file;

    StringBuilder contentBuilder = new StringBuilder();
   
    BufferedReader br;
    
    String fileContent;
    
    String[] words;
    
    public Map<String, Integer> wordCount(String[] words) {
        
        Map<String, Integer> map = new HashMap<String, Integer>();
        int count = 0;
        for (String w : words) {

          if (map.containsKey(w)) {
            count = map.get(w);
            map.put(w, count + 1);
          } else {
              map.put(w, 1);
          }
        }
        return map;
      };

   
    public void chooseButton(ActionEvent e) {
        
        listW.clear();
        
        cancelButton.setDisable(false);

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                      for(int i=1; i<=100;i++){
                           updateProgress(i, 100);
                            Thread.sleep(10);
                        }

                        return null;
                    }
                };
      
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
                    @Override
                    public void handle(WorkerStateEvent event)
                    {
                        listW.clear();
                        System.out.println("Finished"); 
                        readFile();
                        cancelButton.setDisable(true);
                    }
                });
       
                cancelButton.setOnAction((ActionEvent event) -> {
                    listW.clear();
                    cancelButton.setDisable(true);
                    progressBar.progressProperty().unbind();
                    progressBar.setProgress(0);
                    fileName.setText(" ");
                    task.cancel();
                    System.out.println("Cancelled");
                  });
      
             
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();
                
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(task.progressProperty());
                
            } 
                 
        }
        

    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //fileChooser.setInitialDirectory(new File("/Users/Hp/Desktop/")); 
        word.setCellValueFactory(new PropertyValueFactory<Words, String>("word"));
        occurrence.setCellValueFactory(new PropertyValueFactory<Words, Integer>("occurrence"));
        table.setItems(listW);

        }


    public void readFile() {
        
        try {
            
        listW.clear();  
        inputStream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(inputStream);
        br = new BufferedReader(new InputStreamReader(in,"Cp1252"));
        
        String currentLine=null;;
        
        String path = file.getPath();
        fileName.setText(path);

        while ((currentLine = br.readLine()) != null) {
            contentBuilder.append(currentLine).append(" ");
        }
        
        br.close();

        fileContent = (contentBuilder.toString()).replaceAll("\\s{2,}", " ").trim();

        words = fileContent.split(" ");
        contentBuilder.setLength(0);

        
        Map<String, Integer> map = wordCount(words);

        for (String i : map.keySet()) {
            
             listW.add(new Words(i, map.get(i)));
        }


        occurrence.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().addAll(occurrence);
        }
        catch (IOException ioe) {

            ioe.printStackTrace();
        }

    }
    
    public void clear(ActionEvent e) {
        System.out.println("Clear");
        listW.clear();
        cancelButton.setDisable(true);
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        fileName.setText(" ");
    }
}
