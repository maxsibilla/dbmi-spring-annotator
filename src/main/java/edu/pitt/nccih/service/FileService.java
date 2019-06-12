package edu.pitt.nccih.service;

import edu.pitt.nccih.models.File;

public interface FileService {
    void save(File file);

    File findByUri(String uri);

    void delete(Long id);
}
