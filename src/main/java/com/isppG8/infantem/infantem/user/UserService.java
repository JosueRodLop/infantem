package com.isppG8.infantem.infantem.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Email address is already registered.");
    }
    
    if (userRepository.findByNameUser(user.getNameUser()).isPresent()) {
        throw new IllegalArgumentException("Username is already registered.");
    }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setSurname(userDetails.getSurname());
            user.setNameUser(userDetails.getNameUser());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        });
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Optional<User> login(String nameUser, String password) {
        Optional<User> user = userRepository.findByNameUser(nameUser);
        if(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }
    
}
