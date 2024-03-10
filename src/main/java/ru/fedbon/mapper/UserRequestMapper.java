package ru.fedbon.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fedbon.dto.UserRequestDraft;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.dto.UserRequestNew;
import ru.fedbon.model.User;
import ru.fedbon.model.UserRequest;
import ru.fedbon.model.UserRequestStatus;

import java.time.Instant;


@Service
@AllArgsConstructor
public class UserRequestMapper {

    public UserRequest mapNewRequest(UserRequestNew userRequestNew, User user) {

        if (userRequestNew == null && user == null) {
            return null;
        }

        var userRequestBuilder = UserRequest.builder();

        if (userRequestNew != null && user != null) {
            userRequestBuilder.requestText(userRequestNew.getRequestText());
            userRequestBuilder.requestStatus(UserRequestStatus.PENDING.name());
            userRequestBuilder.created(Instant.now());
            userRequestBuilder.user(user);
        }
        return userRequestBuilder.build();
    }

    public UserRequest mapDraftRequest(UserRequestDraft userRequestDraft, User user) {

        if (userRequestDraft == null && user == null) {
            return null;
        }

        var userRequestBuilder = UserRequest.builder();

        if (userRequestDraft != null && user != null) {
            userRequestBuilder.requestText(userRequestDraft.getRequestText());
            userRequestBuilder.requestStatus(UserRequestStatus.DRAFT.name());
            userRequestBuilder.created(Instant.now());
            userRequestBuilder.user(user);
        }
        return userRequestBuilder.build();
    }

    public UserRequestDto mapToDto(UserRequest userRequest) {

        if (userRequest == null) {
            return null;
        }

        var userRequestDto = new UserRequestDto();

        userRequestDto.setId(userRequest.getId());
        userRequestDto.setRequestStatus(userRequest.getRequestStatus());
        userRequestDto.setRequestText(userRequest.getRequestText());
        userRequestDto.setCreated(userRequest.getCreated().toString());
        userRequestDto.setUsername(userRequest.getUser().getUsername());
        userRequestDto.setUserId(userRequest.getUser().getId());

        return userRequestDto;
    }

}
