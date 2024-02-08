package clh.comp313.gateway.bookstore.author;

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

    public AuthorEntity createAuthor(String name, String biography, String avatar_url) {
        CreateAuthorRequest request = CreateAuthorRequest.newBuilder().setName(name).setBiography(biography).setAvatarUrl(avatar_url).build();
        return blockingStub.createAuthor(request);
    }

    public Iterable<AuthorEntity> getAllAuthors() {
        GetAllAuthorsRequest request = GetAllAuthorsRequest.newBuilder().build();
        Iterator<AuthorEntity> responseIterator = blockingStub.getAllAuthors(request);

        List<AuthorEntity> responseList = new ArrayList<>();
        responseIterator.forEachRemaining(responseList::add);

        return responseList;
    }


    public GetAuthorByIdResponse getAuthorById(Integer id){
        AuthorByIdRequest author_id = AuthorByIdRequest
                .newBuilder().setAuthorId(id).build();

        return blockingStub.getAuthorById(author_id);
    }

    public AuthorEntity setAuthorAvatarUrlById(Long id, String avatar_url) {
        AuthorAvatarUrlRequest request = AuthorAvatarUrlRequest.newBuilder().setAuthorId(id).setAvatarUrl(avatar_url).build();
        return blockingStub.setAuthorAvatarUrlById(request);
    }
}
