package ru.fedbon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fedbon.dto.UserRequestDto;


public interface OperatorService {

    Page<UserRequestDto> getAllPendingUserRequests(Pageable pageable);

    Page<UserRequestDto> getAllPendingUserRequestsByUser(Pageable pageable, String username);

    UserRequestDto getUserRequestById(long id);

    UserRequestDto approveUserRequestById(long id);

    UserRequestDto rejectUserRequestById(long id);
}
