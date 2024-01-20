package clh.comp313.gateway.auth;

import clh.comp313.gateway.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {
    private static AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;

    @Container
    public static DockerComposeContainer environment = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("clh-grpc-auth", 8080)
            .withExposedService("redis", 6379)
            .withLocalCompose(true);

    @BeforeAll
    public static void setUp() {
        String grpcServiceAddress = environment.getServiceHost("clh-grpc-auth", 8080);
        Integer grpcServicePort = environment.getServicePort("clh-grpc-auth", 8080);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcServiceAddress, grpcServicePort).usePlaintext().build();

        authServiceStub = AuthServiceGrpc.newBlockingStub(channel);
    }


    @Test
    @Order(1)
    public void testCreateUser() {
        CreateUserRequest request = CreateUserRequest.newBuilder().setUsername("newexampleuser").setPassword("mygreatpassword").setEmail("email@domain.com").build();

        CreateUserResponse response = authServiceStub.createUser(request);

        assertThat(response.getSuccess()).isTrue();
        assertThat(response.getMessage()).isNotBlank();
    }

    @Test
    @Order(2)
    public void testLogin() {
        LoginRequest request = LoginRequest.newBuilder().setUsername("newexampleuser").setPassword("mygreatpassword").setEmail("email@domain.com").build();

        LoginResponse response = authServiceStub.login(request);
        assertThat(response.getToken()).isNotNull();
    }

    @Test
    @Order(2)
    public void testLoginNegative() {
        LoginRequest request = LoginRequest.newBuilder().setUsername("newexampleuser").setPassword("wrongpassword").setEmail("email@domain.com").build();

        Executable loginExecutable = () -> authServiceStub.login(request);
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, loginExecutable);

        // Assertions
        assertThat(exception.getStatus().getCode()).isEqualTo(Status.Code.UNAUTHENTICATED);
        assertThat(exception.getMessage()).contains("invalid username or password");
    }

    @Test
    @Order(3)
    public void testValidateToken() {
        String validToken = obtainToken();

        ValidateRequest request = ValidateRequest.newBuilder().setToken(validToken).build();
        ValidateResponse response = authServiceStub.validateToken(request);

        assertThat(response.getValid()).isTrue();
    }

    private String obtainToken() {
        LoginRequest loginRequest = LoginRequest.newBuilder().setUsername("newexampleuser").setPassword("mygreatpassword").setEmail("email@domain.com").build();

        LoginResponse loginResponse = authServiceStub.login(loginRequest);
        return loginResponse.getToken();
    }
}
