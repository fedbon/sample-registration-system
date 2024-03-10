package ru.fedbon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fedbon.model.User;
import ru.fedbon.model.UserRequest;

import java.util.Optional;


@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {

    Page<UserRequest> findByUserId(Pageable pageable, Long userId);

    Optional<UserRequest> findByIdAndRequestStatus(long id, String status);

    Page<UserRequest> findAllByRequestStatus(Pageable pageable, String status);

    Page<UserRequest> findAllByRequestStatusIn(Pageable pageable, String[] status);

    Page<UserRequest> findAllByRequestStatusAndUser(Pageable pageable, String status, User user);

    Page<UserRequest> findAllByRequestStatusInAndUser(Pageable pageable, String[] statuses, User user);
}
