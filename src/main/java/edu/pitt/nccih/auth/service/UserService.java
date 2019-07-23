package edu.pitt.nccih.auth.service;

import edu.pitt.nccih.auth.model.User;

public interface UserService {
    void save(User user);

    void update(User user);

    User findByUsername(String username);
}