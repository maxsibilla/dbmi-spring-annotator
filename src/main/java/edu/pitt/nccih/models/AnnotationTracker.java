package edu.pitt.nccih.models;

import edu.pitt.nccih.auth.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name="annotationTracker")
public class AnnotationTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "annotation_id")
    private Annotation annotation;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime pageLoadTime;

    private LocalDateTime annotationClickTime;

    private ArrayList<String> wordDifficultyFilter;

    private String englishPhraseFilter;

    private String sciencePhraseFilter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getPageLoadTime() {
        return pageLoadTime;
    }

    public void setPageLoadTime(LocalDateTime pageLoadTime) {
        this.pageLoadTime = pageLoadTime;
    }

    public LocalDateTime getAnnotationClickTime() {
        return annotationClickTime;
    }

    public void setAnnotationClickTime(LocalDateTime annotationClickTime) {
        this.annotationClickTime = annotationClickTime;
    }

    public ArrayList<String> getWordDifficultyFilter() {
        return wordDifficultyFilter;
    }

    public void setWordDifficultyFilter(ArrayList<String> wordDifficultyFilter) {
        this.wordDifficultyFilter = wordDifficultyFilter;
    }

    public String getEnglishPhraseFilter() {
        return englishPhraseFilter;
    }

    public void setEnglishPhraseFilter(String englishPhraseFilter) {
        this.englishPhraseFilter = englishPhraseFilter;
    }

    public String getSciencePhraseFilter() {
        return sciencePhraseFilter;
    }

    public void setSciencePhraseFilter(String sciencePhraseFilter) {
        this.sciencePhraseFilter = sciencePhraseFilter;
    }
}
