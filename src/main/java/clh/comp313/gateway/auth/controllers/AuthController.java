package clh.comp313.gateway.auth.controllers;

import clh.comp313.gateway.grpc.*;
import clh.comp313.gateway.auth.dtos.UserDTO;
import clh.comp313.gateway.auth.dtos.ValidateResponseDTO;
import clh.comp313.gateway.auth.services.AuthGrpcClientService;
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
    private final AuthGrpcClientService authGrpcClientService;

    public AuthController(AuthGrpcClientService authGrpcClientService) {
        this.authGrpcClientService = authGrpcClientService;
    }


    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            // Map UserDTO to CreateUserRequest
            CreateUserRequest grpcRequest = CreateUserRequest.newBuilder().setUsername(userDTO.getUsername()).setPassword(userDTO.getPassword()).setEmail(userDTO.getEmail()).build();

            CreateUserResponse grpcResponse = authGrpcClientService.authServiceStub().createUser(grpcRequest);

            System.out.println(userDTO.getUsername());

            // Serialize the Protobuf object to JSON
            // https://stackoverflow.com/questions/51588778/convert-a-protobuf-to-json-using-jackson
            String jsonResponse = JsonFormat.printer().print(grpcResponse);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(Collections.singletonMap("error", e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            LoginRequest grpcRequest = LoginRequest.newBuilder()
                    .setUsername("")
                    .setPassword(userDTO.getPassword())
                    .setEmail(userDTO.getEmail())
                    .build();

            LoginResponse grpcResponse = authGrpcClientService.authServiceStub().login(grpcRequest);


            String jsonResponse = JsonFormat.printer().print(grpcResponse);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(Collections.singletonMap("error", e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        try {
            ValidateRequest grpcRequest = ValidateRequest.newBuilder().setToken(token).build();
            ValidateResponse grpcResponse = authGrpcClientService.authServiceStub().validateToken(grpcRequest);

            ValidateResponseDTO responseDTO = new ValidateResponseDTO(grpcResponse.getValid());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());

            return new ResponseEntity<>(Collections.singletonMap("error", e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
