package ru.fedbon.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private Long id;

    private String requestStatus;

    private String requestText;

    private String created;

    private String username;

    private Long userId;

}
