package clh.comp313.gateway.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateResponseDTO {
    private boolean valid;
}