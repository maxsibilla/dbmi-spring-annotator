package edu.pitt.nccih.auth.repository;

import edu.pitt.nccih.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}