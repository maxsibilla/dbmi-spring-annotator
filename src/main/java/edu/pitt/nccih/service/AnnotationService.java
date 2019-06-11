package edu.pitt.nccih.service;

import edu.pitt.nccih.models.Annotation;

import java.util.List;

public interface AnnotationService {
    Annotation save(Annotation annotation);

    List<Annotation> findByUri(String uri);

    Annotation findById(Long id);

    void delete(Long id);
}