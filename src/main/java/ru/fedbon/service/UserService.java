package ru.fedbon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fedbon.dto.UserRequestDraft;
import ru.fedbon.dto.UserRequestNew;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.dto.UserRequestDraftUpdate;

public interface UserService {

    UserRequestDto sendUserRequest(UserRequestNew userRequestNew);

    UserRequestDto createDraftUserRequest(UserRequestDraft userRequestDraft);

    UserRequestDto updateDraftUserRequest(UserRequestDraftUpdate userRequestDraftUpdate);

    UserRequestDto sendDraftUserRequest(long userRequestDraftId);

    Page<UserRequestDto> getAllByUserId(Pageable pageable, long userId);

}
