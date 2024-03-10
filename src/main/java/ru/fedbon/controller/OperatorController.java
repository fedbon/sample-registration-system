package ru.fedbon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.service.OperatorService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/operators")
@AllArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_OPERATOR')")
    @GetMapping("/requests")
    public List<UserRequestDto> getAllUserRequestsByLoggedUser(
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String usernameFilter) {

        var sort = getSort(direction);
        var pageRequest = PageRequest.of(page, size, sort);

        Page<UserRequestDto> userRequestsPage;
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            userRequestsPage = operatorService.getAllPendingUserRequestsByUser(pageRequest, usernameFilter);
        } else {
            userRequestsPage = operatorService.getAllPendingUserRequests(pageRequest);
        }

        return userRequestsPage.getContent();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_OPERATOR')")
    @GetMapping("/requests/{id}")
    public UserRequestDto getUserRequestById(@PathVariable long id) {
        return operatorService.getUserRequestById(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_OPERATOR')")
    @PutMapping("/requests/{id}/approve")
    public UserRequestDto approveUserRequestById(@PathVariable long id) {
        return operatorService.approveUserRequestById(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_OPERATOR')")
    @PutMapping("/requests/{id}/reject")
    public UserRequestDto rejectUserRequestById(@PathVariable long id) {
        return operatorService.rejectUserRequestById(id);
    }

    private Sort getSort(String direction) {
        return "ASC".equalsIgnoreCase(direction) ? Sort.by("created").ascending() :
                Sort.by("created").descending();
    }
}