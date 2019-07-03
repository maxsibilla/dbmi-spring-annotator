package edu.pitt.nccih.service;

import edu.pitt.nccih.models.AnnotationTracker;
import edu.pitt.nccih.repository.AnnotationTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AnnotationTrackerImpl implements AnnotationTrackerService {
    @Autowired
    AnnotationTrackerRepository annotationTrackerRepository;

    @Override
    public void save(AnnotationTracker annotationTracker) {
        annotationTrackerRepository.save(annotationTracker);
    }

    @Override
    public AnnotationTracker findById(Long id) {
        return annotationTrackerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        annotationTrackerRepository.delete(id);
    }
}
