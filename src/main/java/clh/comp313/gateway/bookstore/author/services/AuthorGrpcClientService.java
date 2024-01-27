package clh.comp313.gateway.bookstore.author.services;

import io.clh.bookstore.author.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class AuthorGrpcClientService {
    private final AuthorServiceGrpc.AuthorServiceBlockingStub blockingStub;

    public AuthorGrpcClientService(@Value("${BOOKSTORE_GRPC_HOST:localhost}") String host, @Value("${BOOKSTORE_GRPC_PORT:8082}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext() // No SSL/TLS
                .build();
        blockingStub = AuthorServiceGrpc.newBlockingStub(channel);
    }

    public CreateAuthorResponse createAuthor(String name, String biography) {
        CreateAuthorRequest request = CreateAuthorRequest.newBuilder().setName(name).setBiography(biography).build();
        return blockingStub.createAuthor(request);
    }

    public Iterable<GetAllAuthorsResponse> getAllAuthors() {
        GetAllAuthorsRequest request = GetAllAuthorsRequest.newBuilder().build();
        Iterator<GetAllAuthorsResponse> responseIterator = blockingStub.getAllAuthors(request);

        // Convert the Iterator to a List (or another Iterable)
        List<GetAllAuthorsResponse> responseList = new ArrayList<>();
        responseIterator.forEachRemaining(responseList::add);

        return responseList;
    }
}
