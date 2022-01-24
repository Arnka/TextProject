package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Words {
    private final SimpleStringProperty word;
    private final SimpleIntegerProperty occurrence;
    
    public Words(String word, Integer occurrence) {
        super();
        this.word = new SimpleStringProperty(word);
        this.occurrence = new SimpleIntegerProperty(occurrence);
    }

    public String getWord() {
        return word.get();
    }

    public Integer getOccurrence() {
        return occurrence.get();
    }
     
}
