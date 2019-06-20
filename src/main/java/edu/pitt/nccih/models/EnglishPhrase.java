package edu.pitt.nccih.models;

import java.io.Serializable;

public class EnglishPhrase implements Serializable {
    private String definition;

    private String difficulty;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
