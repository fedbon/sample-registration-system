package ru.fedbon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fedbon.dto.UserDto;
import ru.fedbon.dto.UserRequestDto;

import java.util.List;


public interface AdminService {

    Page<UserRequestDto> getAllUserRequests(Pageable pageable);

    Page<UserRequestDto> getAllUserRequestsByStatusAndUsername(Pageable pageable, String username);

    List<UserDto> getAllUsers();

    UserDto assignOperatorRoleByUserId(long userId);
}
