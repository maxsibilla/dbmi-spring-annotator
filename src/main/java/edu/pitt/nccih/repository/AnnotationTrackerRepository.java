package edu.pitt.nccih.repository;

import edu.pitt.nccih.models.AnnotationTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnotationTrackerRepository extends JpaRepository<AnnotationTracker, Long> {
    AnnotationTracker findById(Long id);

    List<AnnotationTracker> findAll();

    void delete(Long id);
}
