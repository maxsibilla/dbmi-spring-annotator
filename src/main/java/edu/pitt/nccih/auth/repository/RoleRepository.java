package edu.pitt.nccih.auth.repository;

import edu.pitt.nccih.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
}