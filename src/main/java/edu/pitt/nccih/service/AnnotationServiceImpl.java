package edu.pitt.nccih.service;

import edu.pitt.nccih.models.Annotation;
import edu.pitt.nccih.repository.AnnotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationServiceImpl implements AnnotationService {
    @Autowired
    private AnnotationRepository annotationRepository;

    @Override
    public Annotation save(Annotation annotation) {
        return annotationRepository.save(annotation);
    }

    @Override
    public List<Annotation> findByUri(String uri) {
        return annotationRepository.findByUri(uri);
    }

    @Override
    public Annotation findById(Long id) {
        return annotationRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        annotationRepository.delete(id);
    }
}
