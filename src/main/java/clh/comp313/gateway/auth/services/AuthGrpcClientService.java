package clh.comp313.gateway.auth.services;

import clh.comp313.gateway.grpc.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthGrpcClientService {

    private final ManagedChannel channel;

    public AuthGrpcClientService(@Value("${AUTH_SERVICE_ADDRESS:localhost}") String grpcServiceAddress) {
        this.channel = ManagedChannelBuilder.forAddress(grpcServiceAddress, 8080)
                .usePlaintext()
                .build();
    }

    public AuthServiceGrpc.AuthServiceBlockingStub authServiceStub() {
        return AuthServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() {
        channel.shutdown();
    }
}

