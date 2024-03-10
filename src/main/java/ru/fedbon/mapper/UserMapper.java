package ru.fedbon.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fedbon.dto.UserDto;
import ru.fedbon.model.User;


@Service
@AllArgsConstructor
public class UserMapper {

    public UserDto mapToDto(User user) {

        if (user == null) {
            return null;
        }

        var userDto = new UserDto();

        userDto.setUserId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles().toString());

        return userDto;
    }

}
