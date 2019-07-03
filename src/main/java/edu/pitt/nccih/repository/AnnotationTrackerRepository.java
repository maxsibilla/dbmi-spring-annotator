package edu.pitt.nccih.repository;

import edu.pitt.nccih.models.AnnotationTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnotationTrackerRepository extends JpaRepository<AnnotationTracker, Long> {
    AnnotationTracker findById(Long id);

    void delete(Long id);
}
