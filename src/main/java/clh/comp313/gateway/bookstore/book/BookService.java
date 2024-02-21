package clh.comp313.gateway.bookstore.book;

import clh.comp313.gateway.bookstore.dtos.BookDto;
import io.clh.bookstore.bookstore.Book;
import io.clh.bookstore.entities.Entities;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    public BookDto getBookById(Long id) {
        Book.GetBookByIdRequest build = Book.GetBookByIdRequest.newBuilder().setId(id).build();

        Book.GetBookByIdResponse grpcResponse = bookGrpcClientService.getBookById(build);
        return BookGrpcToBookDto(grpcResponse.getBook());
    }

    public List<BookDto> GetAllBooks(Integer page) {
        Book.GetAllBooksRequest build = Book.GetAllBooksRequest.newBuilder().setPage(page).build();
        Iterator<Entities.Book> allBooks = bookGrpcClientService.getAllBooks(build);

        List<BookDto> bookDtoList = new ArrayList<>();

        while (allBooks.hasNext()) {
            Entities.Book next = allBooks.next();
            bookDtoList.add(BookGrpcToBookDto(next));
        }

        return bookDtoList;
    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        //TODO: ???? Remove redundant id from proto
        bookDto.setBook_id(id);

        Book.UpdateBookRequest build = Book.UpdateBookRequest.newBuilder()
                .setBook(BookDtoToBookEntity(bookDto))
                .build();

        Book.UpdateBookResponse updateBookResponse = bookGrpcClientService.updateBook(build);
        return BookGrpcToBookDto(updateBookResponse.getBook());
    }
}
