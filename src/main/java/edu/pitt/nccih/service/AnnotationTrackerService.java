package edu.pitt.nccih.service;

import edu.pitt.nccih.models.AnnotationTracker;

public interface AnnotationTrackerService {
    void save(AnnotationTracker annotationTracker);

    AnnotationTracker findById(Long id);

    void delete(Long id);
}
