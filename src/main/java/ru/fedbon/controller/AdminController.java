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
import ru.fedbon.dto.UserDto;
import ru.fedbon.dto.UserRequestDto;
import ru.fedbon.service.AdminService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admins")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/requests")
    public List<UserRequestDto> getAllUserRequests(
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String usernameFilter) {

        var sort = getSort(direction);
        var pageRequest = PageRequest.of(page, size, sort);
        Page<UserRequestDto> userRequestsPage;

        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            userRequestsPage = adminService.getAllUserRequestsByStatusAndUsername(pageRequest, usernameFilter);
        } else {
            userRequestsPage = adminService.getAllUserRequests(pageRequest);
        }

        return userRequestsPage.getContent();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("/users/{userId}/roles")
    public UserDto assignOperatorRoleByUserId(@PathVariable("userId") long userId) {
        return adminService.assignOperatorRoleByUserId(userId);
    }

    private Sort getSort(String direction) {
        return "ASC".equalsIgnoreCase(direction) ? Sort.by("created").ascending() :
                Sort.by("created").descending();
    }
}