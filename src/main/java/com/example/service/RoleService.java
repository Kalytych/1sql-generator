package com.example.service;

import com.example.Role;
import com.example.RoleRepository;

import java.util.List;

public class RoleService {
  private final RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  public Role findById(int id) {
    return roleRepository.findById(id);
  }
}
