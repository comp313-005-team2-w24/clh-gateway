package clh.comp313.gateway.bookstore.book;

import io.clh.bookstore.BookOuterClass;
import io.clh.bookstore.BookServiceGrpc;
import io.clh.bookstore.bookstore.Book;
import io.clh.bookstore.bookstore.BookServiceGrpc;
import io.clh.bookstore.entities.Entities;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class BookGrpcClientService {

    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    public BookGrpcClientService(@Value("${BOOKSTORE_GRPC_HOST:localhost}") String host, @Value("${BOOKSTORE_GRPC_PORT:8082}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext() // No SSL/TLS
                .build();
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    public Book.CreateBookResponse createBook(Book.CreateBookRequest request) {
        return blockingStub.createBook(request);
    }

    public Book.GetBookByIdResponse getBookById(Book.GetBookByIdRequest request) {
        return blockingStub.getBookById(request);
    }

    public Iterator<Entities.Book> getAllBooks(Book.GetAllBooksRequest request) {
        return blockingStub.getAllBooks(request);
    }

    public Book.UpdateBookResponse updateBook(Book.UpdateBookRequest request) {
        return blockingStub.updateBook(request);
    }
}
