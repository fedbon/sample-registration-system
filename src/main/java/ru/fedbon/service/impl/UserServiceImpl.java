package ru.fedbon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fedbon.constants.ErrorMessage;
import ru.fedbon.dto.UserRequestDraft;
import ru.fedbon.dto.UserRequestDraftUpdate;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.dto.UserRequestNew;
import ru.fedbon.exception.NotFoundException;
import ru.fedbon.mapper.UserRequestMapper;
import ru.fedbon.model.UserRequest;
import ru.fedbon.model.UserRequestStatus;
import ru.fedbon.repository.UserRequestRepository;
import ru.fedbon.service.UserService;
import ru.fedbon.service.security.AuthService;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRequestRepository userRequestRepository;

    private final UserRequestMapper userRequestMapper;

    private final AuthService authService;

    @Transactional
    @Override
    public UserRequestDto sendUserRequest(UserRequestNew userRequestNew) {
        var userRequest = userRequestRepository.save(userRequestMapper.mapNewRequest(userRequestNew,
                authService.getCurrentUser()));

        return userRequestMapper.mapToDto(userRequest);
    }

    @Transactional
    @Override
    public UserRequestDto createDraftUserRequest(UserRequestDraft userRequestDraft) {
        var userRequest = userRequestRepository.save(userRequestMapper.mapDraftRequest(userRequestDraft,
                authService.getCurrentUser()));

        return userRequestMapper.mapToDto(userRequest);
    }

    @Transactional
    @Override
    public UserRequestDto updateDraftUserRequest(UserRequestDraftUpdate userRequestDraftUpdate) {
        var userRequest = userRequestRepository.findByIdAndRequestStatus(userRequestDraftUpdate.getRequestId(),
                        UserRequestStatus.DRAFT.name())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DRAFT_USER_REQUEST_NOT_FOUND +
                        userRequestDraftUpdate.getRequestId()));

        userRequest.setRequestText(userRequestDraftUpdate.getRequestText());

        return userRequestMapper.mapToDto(userRequest);
    }

    @Transactional
    @Override
    public UserRequestDto sendDraftUserRequest(long userRequestDraftId) {
        var userRequest = userRequestRepository.findByIdAndRequestStatus(userRequestDraftId,
                        UserRequestStatus.DRAFT.name())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DRAFT_USER_REQUEST_NOT_FOUND +
                        userRequestDraftId));

        userRequest.setRequestStatus(UserRequestStatus.PENDING.name());
        return userRequestMapper.mapToDto(userRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserRequestDto> getAllByUserId(Pageable pageable, long userId) {
        try {
            Page<UserRequest> userRequestsPage = userRequestRepository.findByUserId(pageable, userId);
            return userRequestsPage.map(userRequestMapper::mapToDto);
        } catch (Exception e) {
            throw new NotFoundException(ErrorMessage.USER_REQUEST_NOT_FOUND + userId);
        }
    }
}
