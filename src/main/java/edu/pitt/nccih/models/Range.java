package edu.pitt.nccih.models;

import javax.persistence.Embeddable;

@Embeddable
public class Range {

    private String startRange;
    private String endRange;
    private Integer startOffset;
    private Integer endOffset;


    public String getStart() {
        return startRange;
    }

    public void setStart(String startRange) {
        this.startRange = startRange;
    }

    public String getEnd() {
        return endRange;
    }

    public void setEnd(String endRange) {
        this.endRange = endRange;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    public Integer getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(Integer endOffset) {
        this.endOffset = endOffset;
    }
}