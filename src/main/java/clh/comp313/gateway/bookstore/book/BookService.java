package clh.comp313.gateway.bookstore.book;

import clh.comp313.gateway.bookstore.dtos.BookDto;
import io.clh.bookstore.bookstore.Book;
import io.clh.bookstore.entities.Entities;
import org.springframework.stereotype.Service;

import static clh.comp313.gateway.bookstore.utils.DtoToGrpcConverter.BookDtoToBookEntity;
import static clh.comp313.gateway.bookstore.utils.GrpcToDtoConverter.BookGrpcToBookDto;

@Service
public class BookService {
    private final BookGrpcClientService bookGrpcClientService;

    public BookService(BookGrpcClientService bookGrpcClientService) {
        this.bookGrpcClientService = bookGrpcClientService;
    }

    public BookDto createBook(BookDto bookDto) {
        Entities.Book book = BookDtoToBookEntity(bookDto);

        Book.CreateBookRequest createBookRequest = Book.CreateBookRequest.newBuilder().setBook(book).build();

        Book.CreateBookResponse createBookResponse = bookGrpcClientService.createBook(createBookRequest);
        return BookGrpcToBookDto(createBookResponse.getBook());
    }


}
