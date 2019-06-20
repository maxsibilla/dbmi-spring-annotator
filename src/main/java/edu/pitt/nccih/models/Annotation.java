package edu.pitt.nccih.models;

import edu.pitt.nccih.auth.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "annotations")
public class Annotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    private LocalDateTime updated;

    private String wordType;

    private String wordDifficulty;

    @Column(length = 200000)
    private String text;

    @Column(length = 200000)
    private String figure;

    @Column(length = 200000)
    private String video;

    @Column(length = 200000)
    private String quote;

    @Column(length = 2000)
    private String uri;

    @Transient
    private Range[] ranges;

    @Embedded
    private Range range;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    private String[] tags = new String[]{};

    public Annotation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getWordDifficulty() {
        return wordDifficulty;
    }

    public void setWordDifficulty(String wordDifficulty) {
        this.wordDifficulty = wordDifficulty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Range[] getRanges() {
        if (ranges == null || ranges.length == 0) {
            ranges = new Range[]{range};
        }
        return ranges;
    }

    public void setRanges(Range[] ranges) {
        this.ranges = ranges;
        if (this.ranges.length > 0) {
            this.range = ranges[0];
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String[] getTags() {
        if (tags == null) {
            tags = new String[]{};
        }
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

}