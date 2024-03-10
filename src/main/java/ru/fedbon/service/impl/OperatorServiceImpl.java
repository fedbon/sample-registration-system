package ru.fedbon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fedbon.constants.ErrorMessage;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.exception.NotFoundException;
import ru.fedbon.mapper.UserRequestMapper;
import ru.fedbon.model.UserRequestStatus;
import ru.fedbon.repository.UserRepository;
import ru.fedbon.repository.UserRequestRepository;
import ru.fedbon.service.OperatorService;



@Service
@RequiredArgsConstructor
public class OperatorServiceImpl implements OperatorService {

    private final UserRequestRepository userRequestRepository;

    private final UserRequestMapper userRequestMapper;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<UserRequestDto> getAllPendingUserRequests(Pageable pageable) {
        try {
            return userRequestRepository.findAllByRequestStatus(pageable, UserRequestStatus.PENDING.name())
                    .map(userRequestMapper::mapToDto);
        } catch (Exception e) {
            throw new NotFoundException(ErrorMessage.PENDING_USER_REQUEST_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserRequestDto> getAllPendingUserRequestsByUser(Pageable pageable, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND + username));
        try {
            return userRequestRepository.findAllByRequestStatusAndUser(pageable, UserRequestStatus.PENDING.name(), user)
                    .map(userRequestMapper::mapToDto);
        } catch (Exception e) {
            throw new NotFoundException(ErrorMessage.PENDING_USER_REQUEST_NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public UserRequestDto getUserRequestById(long id) {
        return userRequestMapper.mapToDto(
                userRequestRepository.findByIdAndRequestStatus(id, UserRequestStatus.PENDING.name())
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.PENDING_USER_REQUEST_NOT_FOUND + id)));
    }

    @Transactional
    @Override
    public UserRequestDto approveUserRequestById(long id) {
        var userRequest = userRequestRepository.findByIdAndRequestStatus(id, UserRequestStatus.PENDING.name())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.PENDING_USER_REQUEST_NOT_FOUND + id));
        userRequest.setRequestStatus(UserRequestStatus.APPROVED.name());
        return userRequestMapper.mapToDto(userRequest);
    }

    @Transactional
    @Override
    public UserRequestDto rejectUserRequestById(long id) {
        var userRequest = userRequestRepository.findByIdAndRequestStatus(id, UserRequestStatus.PENDING.name())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.PENDING_USER_REQUEST_NOT_FOUND + id));
        userRequest.setRequestStatus(UserRequestStatus.REJECTED.name());
        return userRequestMapper.mapToDto(userRequest);
    }
}
