package clh.comp313.gateway.bookstore.author;

import io.clh.bookstore.author.*;
import io.clh.bookstore.entities.Entities;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AuthorGrpcClientService {
    private final AuthorServiceGrpc.AuthorServiceBlockingStub blockingStub;

    public AuthorGrpcClientService(@Value("${BOOKSTORE_GRPC_HOST:localhost}") String host, @Value("${BOOKSTORE_GRPC_PORT:8082}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext() // No SSL/TLS
                .build();
        blockingStub = AuthorServiceGrpc.newBlockingStub(channel);
    }

    public Entities.AuthorEntity createAuthor(Author.CreateAuthorRequest request) {
        return blockingStub.createAuthor(request);
    }

    public Iterable<Entities.AuthorEntity> getAllAuthors(Author.GetAllAuthorsRequest request) {
        Iterator<Entities.AuthorEntity> responseIterator = blockingStub.getAllAuthors(request);

        List<Entities.AuthorEntity> responseList = new ArrayList<>();
        responseIterator.forEachRemaining(responseList::add);

        return responseList;
    }


    public GetAuthorByIdResponse getAuthorById(Integer id) {
        AuthorByIdRequest author_id = AuthorByIdRequest
                .newBuilder().setAuthorId(id).build();

        return blockingStub.getAuthorById(author_id);
    }

    public AuthorEntity setAuthorAvatarUrlById(Long id, String avatar_url) {
        AuthorAvatarUrlRequest request = AuthorAvatarUrlRequest.newBuilder().setAuthorId(id).setAvatarUrl(avatar_url).build();
        return blockingStub.setAuthorAvatarUrlById(request);
    }
}
