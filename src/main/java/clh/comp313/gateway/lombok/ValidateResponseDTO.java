package clh.comp313.gateway.lombok;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateResponseDTO {
    private boolean valid;
}