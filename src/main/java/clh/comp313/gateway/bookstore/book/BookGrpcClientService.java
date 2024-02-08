package clh.comp313.gateway.bookstore.book;

import io.clh.bookstore.BookOuterClass;
import io.clh.bookstore.BookServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BookGrpcClientService {
    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    public BookGrpcClientService(@Value("${BOOKSTORE_GRPC_HOST:localhost}") String host, @Value("${BOOKSTORE_GRPC_PORT:8082}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext() // No SSL/TLS
                .build();
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    public BookOuterClass.CreateBookResponse createBook(BookOuterClass.Book book) {
        BookOuterClass.CreateBookRequest request = BookOuterClass.CreateBookRequest.newBuilder().setBook(book).build();
        return blockingStub.createBook(request);
    }

    public BookOuterClass.GetBookByIdResponse getBookById(Long id) {
        BookOuterClass.GetBookByIdRequest request = BookOuterClass.GetBookByIdRequest.newBuilder().setId(id).build();
        return blockingStub.getBookById(request);
    }

    public BookOuterClass.GetAllBooksResponse getAllBooks(Integer page) {
        BookOuterClass.GetAllBooksRequest request = BookOuterClass.GetAllBooksRequest.newBuilder().setPage(page).build();
        return blockingStub.getAllBooks(request);
    }

    public BookOuterClass.UpdateBookResponse updateBook(BookOuterClass.Book book) {
        BookOuterClass.UpdateBookRequest request = BookOuterClass.UpdateBookRequest.newBuilder().setBook(book).build();
        return blockingStub.updateBook(request);
    }
}
