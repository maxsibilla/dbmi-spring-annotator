package edu.pitt.nccih.service;

import edu.pitt.nccih.models.File;
import edu.pitt.nccih.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public void save(File file) {
        fileRepository.save(file);
    }

    @Override
    public File findByUri(String uri) {
        return fileRepository.findByUri(uri);
    }

    @Override
    public void delete(Long id) {
        fileRepository.delete(id);
    }
}
