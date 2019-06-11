package edu.pitt.nccih.auth.service;

import edu.pitt.nccih.auth.model.Role;
import edu.pitt.nccih.auth.model.User;
import edu.pitt.nccih.auth.repository.RoleRepository;
import edu.pitt.nccih.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
         if(user.getRoles()==null || user.getRoles().isEmpty()){
            Set<Role> roles = new HashSet<>();
//            Optional<Role> role = roleRepository.findById(1L);
            roles.add(roleRepository.findOne(1L));
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}