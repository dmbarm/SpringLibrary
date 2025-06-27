package org.springlibrary.mappers;

import org.springlibrary.dtos.users.RegisterUserDTO;
import org.springlibrary.dtos.users.UserResponseDTO;
import org.springlibrary.entities.User;


public class UserMapper {
    private UserMapper() {}

    public static User toEntity(RegisterUserDTO dto) {
        return new User(
                dto.getUsername(),
                dto.getPassword()
        );
    }

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }
}
