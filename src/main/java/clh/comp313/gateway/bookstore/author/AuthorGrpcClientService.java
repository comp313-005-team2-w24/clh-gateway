package clh.comp313.gateway.bookstore.author;

import io.clh.bookstore.author.Author;
import io.clh.bookstore.author.AuthorServiceGrpc;
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

    public Author.GetAuthorByIdResponse getAuthorById(Author.AuthorByIdRequest request) {
        return blockingStub.getAuthorById(request);
    }

    public Entities.AuthorEntity setAuthorAvatarUrlById(Author.AuthorAvatarUrlRequest request) {
        return blockingStub.setAuthorAvatarUrlById(request);
    }
}
