package edu.pitt.nccih.service;

import edu.pitt.nccih.models.AnnotationTracker;
import edu.pitt.nccih.repository.AnnotationTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationTrackerImpl implements AnnotationTrackerService {
    @Autowired
    AnnotationTrackerRepository annotationTrackerRepository;

    @Override
    public void save(AnnotationTracker annotationTracker) {
        annotationTrackerRepository.save(annotationTracker);
    }

    @Override
    public List<AnnotationTracker> findAll() {
        return annotationTrackerRepository.findAll();
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
