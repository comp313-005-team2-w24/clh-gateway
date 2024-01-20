package clh.comp313.gateway.lombok;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
}
