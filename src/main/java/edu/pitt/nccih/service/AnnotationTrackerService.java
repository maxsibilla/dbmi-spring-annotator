package edu.pitt.nccih.service;

import edu.pitt.nccih.models.AnnotationTracker;

import java.util.List;

public interface AnnotationTrackerService {
    void save(AnnotationTracker annotationTracker);

    AnnotationTracker findById(Long id);

    List<AnnotationTracker> findAll();

    void delete(Long id);
}
