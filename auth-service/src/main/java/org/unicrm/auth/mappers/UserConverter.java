package org.unicrm.auth.mappers;

import org.springframework.stereotype.Component;
import org.unicrm.auth.dto.UserDto;
import org.unicrm.auth.entities.User;

@Component
public class UserConverter {
    public UserDto entityToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}
