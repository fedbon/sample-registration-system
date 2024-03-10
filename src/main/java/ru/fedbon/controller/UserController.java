package ru.fedbon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fedbon.dto.UserRequestDraft;
import ru.fedbon.dto.UserRequestDraftUpdate;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.dto.UserRequestNew;
import ru.fedbon.service.UserService;
import ru.fedbon.service.security.AuthService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthService authService;

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/send")
    public UserRequestDto handleSendUserRequest(@RequestBody UserRequestNew userRequestNew) {
        return userService.sendUserRequest(userRequestNew);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
    @GetMapping("/my")
    public List<UserRequestDto> getAllUserRequestsByLoggedUser(
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        var sort = getSort(direction);
        var pageRequest = PageRequest.of(page, size, sort);

        var userRequestsPage = userService.getAllByUserId(pageRequest,
                authService.getCurrentUser().getId());

        return userRequestsPage.getContent();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/create-draft")
    public UserRequestDto handleCreateDraftUserRequest(@RequestBody UserRequestDraft userRequestDraft) {
        return userService.createDraftUserRequest(userRequestDraft);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
    @PutMapping("/update-draft")
    public UserRequestDto handleUpdateDraftUserRequest(@RequestBody UserRequestDraftUpdate userRequestDraftUpdate) {
        return userService.updateDraftUserRequest(userRequestDraftUpdate);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
    @PostMapping("/{draftId}/send")
    public UserRequestDto handleSendDraftUserRequest(@PathVariable long draftId) {
        return userService.sendDraftUserRequest(draftId);
    }

    private Sort getSort(String direction) {
        return "ASC".equalsIgnoreCase(direction) ? Sort.by("created").ascending() :
                Sort.by("created").descending();
    }
}