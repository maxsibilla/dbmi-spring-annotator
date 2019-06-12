package edu.pitt.nccih.repository;

import edu.pitt.nccih.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByUri(String uri);

    void delete(Long id);
}
