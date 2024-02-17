package clh.comp313.gateway.bookstore.book;

import clh.comp313.gateway.bookstore.dtos.BookDto;
import clh.comp313.gateway.bookstore.utils.DtoProtoConversions;
import com.google.protobuf.util.JsonFormat;
import io.clh.bookstore.BookOuterClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookGrpcClientService bookGrpcClientService;

    public BookController(BookGrpcClientService bookGrpcClientService) {
        this.bookGrpcClientService = bookGrpcClientService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) {
        try {
            BookOuterClass.Book bookProto = DtoProtoConversions.convertToBookOuterBookProto(bookDto);
            BookOuterClass.CreateBookResponse grpcResponse = bookGrpcClientService.createBook(bookProto);

            String jsonResponse = JsonFormat.printer().print(grpcResponse.getBook());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            BookOuterClass.GetBookByIdResponse grpcResponse = bookGrpcClientService.getBookById(id);
            BookDto bookDto = new BookDto(
                    Math.toIntExact(grpcResponse.getBook().getBookId()),
                    grpcResponse.getBook().getTitle(),
                    grpcResponse.getBook().getDescription(),
                    grpcResponse.getBook().getIsbn(),
                    new java.sql.Date(grpcResponse.getBook().getPublicationDate().getSeconds() * 1000),
                    grpcResponse.getBook().getPrice(),
                    grpcResponse.getBook().getStockQuantity(),
                    grpcResponse.getBook().getAuthorIdsList(),
                    grpcResponse.getBook().getAvatarUrl()
            );
            return ResponseEntity.ok(bookDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getLocalizedMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST', 'USER')")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        try {
            BookOuterClass.GetAllBooksResponse grpcResponse = bookGrpcClientService.getAllBooks(page);
            List<BookDto> booksDto = grpcResponse.getBooksList().stream().map(bookProto -> new BookDto(
                    Math.toIntExact(bookProto.getBookId()),
                    bookProto.getTitle(),
                    bookProto.getDescription(),
                    bookProto.getIsbn(),
                    new java.sql.Date(bookProto.getPublicationDate().getSeconds() * 1000),
                    bookProto.getPrice(),
                    bookProto.getStockQuantity(),
                    bookProto.getAuthorIdsList(),
                    bookProto.getAvatarUrl()
            )).collect(Collectors.toList());
            return ResponseEntity.ok(booksDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        try {
            BookOuterClass.Book bookProto = DtoProtoConversions.convertToBookOuterBookProto(bookDto);
            BookOuterClass.UpdateBookResponse grpcResponse = bookGrpcClientService.updateBook(bookProto);

            String jsonResponse = JsonFormat.printer().print(grpcResponse.getBook());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getLocalizedMessage()));
        }
    }
}
