package clh.comp313.gateway.grpc;

import com.redis.testcontainers.RedisContainer;
import io.clh.gateway.auth.AuthServiceGrpc;
import io.clh.gateway.auth.LoginRequest;
import io.clh.gateway.auth.LoginResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {
    private static AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;
    private static GenericContainer<?> redisContainer;
    private static GenericContainer<?> authContainer;

    @BeforeAll
    public static void setUp() {
        redisContainer = new RedisContainer(DockerImageName.parse("redis:alpine"))
                .withExposedPorts(6379);

        redisContainer.start();

        // Set up clh-grpc-auth container
        authContainer = new GenericContainer<>(DockerImageName.parse("glad2os/clh-auth"))
                .withExposedPorts(8080)
                .withEnv("REDIS_HOST", redisContainer.getHost() + ":" + redisContainer.getMappedPort(6379));
        authContainer.start();

        String grpcServiceAddress = authContainer.getHost();
        Integer grpcServicePort = authContainer.getMappedPort(8080); // Use the dynamically assigned port

        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcServiceAddress, grpcServicePort)
                .usePlaintext()
                .build();

        authServiceStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public static void tearDown() {
        if (authContainer != null) {
            authContainer.stop();
        }
        if (redisContainer != null) {
            redisContainer.stop();
        }
    }


//    @Test
//    @Order(1)
//    public void testCreateUser() {
//        CreateUserRequest request = CreateUserRequest.newBuilder().setUsername("newexampleuser").setPassword("mygreatpassword").setEmail("email@domain.com").build();
//        CreateUserResponse response = authServiceStub.createUser(request);
//
//        assertThat(response.getSuccess()).isTrue();
//        assertThat(response.getMessage()).isNotBlank();
//    }

//    @Test
//    @Order(2)
//    public void testLogin() {
//        LoginRequest request = LoginRequest.newBuilder().setUsername("testuser1").setPassword("mygreatpassword").setEmail("test@example.com").build();
//
//        LoginResponse response = authServiceStub.login(request);
//        assertThat(response.getToken()).isNotNull();
//    }

    @Test
    @Order(2)
    public void testLoginNegative() {
        LoginRequest request = LoginRequest.newBuilder().setPassword("wrongpassword").setEmail("email@domain.com").build();

        Executable loginExecutable = () -> authServiceStub.login(request);
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, loginExecutable);

        // Assertions
        assertThat(exception.getStatus().getCode()).isEqualTo(Status.Code.UNAUTHENTICATED);
        assertThat(exception.getMessage()).contains("invalid username or password");
    }

//    @Test
//    @Order(3)
//    public void testValidateToken() {
//        String validToken = obtainToken();
//
//        ValidateRequest request = ValidateRequest.newBuilder().setToken(validToken).build();
//        ValidateResponse response = authServiceStub.validateToken(request);
//
//        assertThat(response.getValid()).isTrue();
//    }

    private String obtainToken() {
        LoginRequest loginRequest = LoginRequest.newBuilder().setPassword("mygreatpassword").setEmail("test@example.com").build();

        LoginResponse loginResponse = authServiceStub.login(loginRequest);
        return loginResponse.getToken();
    }
}
