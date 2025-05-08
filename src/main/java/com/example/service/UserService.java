package com.example.service;

import com.example.User;
import com.example.UserRepository;
import java.util.List;

public class UserService {
  private final UserRepository userRepository;


  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public void save(User user) {
    userRepository.save(user);
  }
}
