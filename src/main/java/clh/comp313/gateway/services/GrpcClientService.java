package clh.comp313.gateway.services;

import clh.comp313.gateway.grpc.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

    private final ManagedChannel channel;

    public GrpcClientService(@Value("${AUTH_SERVICE_ADDRESS:localhost}") String grpcServiceAddress) {
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

