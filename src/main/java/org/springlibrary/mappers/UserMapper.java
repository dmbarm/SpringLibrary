package org.springlibrary.mappers;

import org.springlibrary.dtos.users.RegisterUserDTO;
import org.springlibrary.entities.User;

public class UserMapper {
    private UserMapper() {}

    public static User toEntity(RegisterUserDTO dto) {
        return new User(
                dto.getUsername(),
                dto.getPassword()
        );
    }
}
