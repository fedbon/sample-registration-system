package ru.fedbon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fedbon.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(String name);
}