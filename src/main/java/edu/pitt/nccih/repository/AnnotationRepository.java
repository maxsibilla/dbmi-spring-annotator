package edu.pitt.nccih.repository;

import edu.pitt.nccih.models.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    List<Annotation> findByUri(String uri);

    Annotation findById(Long id);

    void delete(Long id);
}