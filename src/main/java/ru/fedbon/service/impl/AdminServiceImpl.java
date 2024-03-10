package ru.fedbon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fedbon.constants.ErrorMessage;
import ru.fedbon.dto.UserDto;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.exception.NotFoundException;
import ru.fedbon.exception.RoleAlreadyExistsException;
import ru.fedbon.mapper.UserMapper;
import ru.fedbon.mapper.UserRequestMapper;
import ru.fedbon.model.UserRequestStatus;
import ru.fedbon.model.UserRole;
import ru.fedbon.repository.UserRepository;
import ru.fedbon.repository.UserRequestRepository;
import ru.fedbon.repository.UserRoleRepository;
import ru.fedbon.service.AdminService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRequestRepository userRequestRepository;

    private final UserRequestMapper userRequestMapper;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<UserRequestDto> getAllUserRequests(Pageable pageable) {
        String[] statusNames = {
                UserRequestStatus.PENDING.name(),
                UserRequestStatus.APPROVED.name(),
                UserRequestStatus.REJECTED.name()
        };
        try {
            return userRequestRepository.findAllByRequestStatusIn(pageable, statusNames)
                    .map(userRequestMapper::mapToDto);
        } catch (Exception e) {
            throw new NotFoundException(ErrorMessage.PENDING_USER_REQUEST_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserRequestDto> getAllUserRequestsByStatusAndUsername(Pageable pageable, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND + username));

        String[] statusNames = {
                UserRequestStatus.PENDING.name(),
                UserRequestStatus.APPROVED.name(),
                UserRequestStatus.REJECTED.name()
        };
        try {
            return userRequestRepository.findAllByRequestStatusInAndUser(pageable, statusNames, user)
                    .map(userRequestMapper::mapToDto);
        } catch (Exception e) {
            throw new NotFoundException(ErrorMessage.USER_REQUEST_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto assignOperatorRoleByUserId(long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND + userId));
        UserRole operatorRole = userRoleRepository.findByName("OPERATOR");
        if (operatorRole == null) {
            operatorRole = new UserRole();
            operatorRole.setName("OPERATOR");
            operatorRole = userRoleRepository.save(operatorRole);
        }
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("OPERATOR"))) {
            throw new RoleAlreadyExistsException(ErrorMessage.ROLE_ALREADY_EXISTS + userId);
        }
        user.getRoles().add(operatorRole);
        user = userRepository.save(user);

        return userMapper.mapToDto(user);
    }
}
