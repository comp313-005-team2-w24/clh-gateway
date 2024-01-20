package clh.comp313.gateway.dtos;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
}
