package clh.comp313.gateway.controllers;

import clh.comp313.gateway.grpc.*;
import clh.comp313.gateway.lombok.TokenDTO;
import clh.comp313.gateway.lombok.UserDTO;
import clh.comp313.gateway.lombok.ValidateResponseDTO;
import clh.comp313.gateway.services.GrpcClientService;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final GrpcClientService grpcClientService;

    public AuthController(GrpcClientService grpcClientService) {
        this.grpcClientService = grpcClientService;
    }


    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            // Map UserDTO to CreateUserRequest
            CreateUserRequest grpcRequest = CreateUserRequest.newBuilder()
                    .setUsername(userDTO.getUsername())
                    .setPassword(userDTO.getPassword())
                    .setEmail(userDTO.getEmail())
                    .build();

            CreateUserResponse grpcResponse = grpcClientService.authServiceStub().createUser(grpcRequest);

            // Serialize the Protobuf object to JSON
            // https://stackoverflow.com/questions/51588778/convert-a-protobuf-to-json-using-jackson
            String jsonResponse = JsonFormat.printer().print(grpcResponse);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);

        } catch (StatusRuntimeException e) {
            return new ResponseEntity<>(
                    Collections.singletonMap("error", e.getStatus().getDescription()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(
                    Collections.singletonMap("error", e.getLocalizedMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            LoginRequest grpcRequest = LoginRequest.newBuilder()
                    .setUsername(userDTO.getUsername())
                    .setPassword(userDTO.getPassword())
                    .setEmail(userDTO.getEmail())
                    .build();

            LoginResponse grpcResponse = grpcClientService.authServiceStub().login(grpcRequest);

            // Serialize the Protobuf object to JSON
            // https://stackoverflow.com/questions/51588778/convert-a-protobuf-to-json-using-jackson
            String jsonResponse = JsonFormat.printer().print(grpcResponse);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);

        } catch (StatusRuntimeException e) {
            return new ResponseEntity<>(
                    Collections.singletonMap("error", e.getStatus().getDescription()),
                    HttpStatus.UNAUTHORIZED);
        } catch (InvalidProtocolBufferException e) {
            return new ResponseEntity<>(
                    Collections.singletonMap("error", e.getLocalizedMessage()),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        try {
            ValidateRequest grpcRequest = ValidateRequest.newBuilder().setToken(token).build();
            ValidateResponse grpcResponse = grpcClientService.authServiceStub().validateToken(grpcRequest);

            ValidateResponseDTO responseDTO = new ValidateResponseDTO(grpcResponse.getValid());
            return ResponseEntity.ok(responseDTO);

        } catch (StatusRuntimeException e) {
            return new ResponseEntity<>(
                    Collections.singletonMap("error", e.getStatus().getDescription()),
                    HttpStatus.INTERNAL_SERVER_ERROR); // or appropriate status
        }
    }
}
