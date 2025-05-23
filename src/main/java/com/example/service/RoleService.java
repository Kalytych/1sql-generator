package com.example.service;

import com.example.Role;
import com.example.RoleRepository;

import java.util.List;

/**
 * Сервіс для роботи з ролями користувачів.
 */
public class RoleService {
  private final RoleRepository roleRepository;

  /**
   * Конструктор сервісу ролей.
   *
   * @param roleRepository репозиторій для доступу до ролей
   */
  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  /**
   * Отримати список усіх ролей.
   *
   * @return список ролей
   */
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  /**
   * Знайти роль за її унікальним ідентифікатором.
   *
   * @param id унікальний ідентифікатор ролі
   * @return об'єкт ролі або null, якщо роль не знайдена
   */
  public Role findById(int id) {
    return roleRepository.findById(id);
  }
}
