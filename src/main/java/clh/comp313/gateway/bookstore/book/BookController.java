package clh.comp313.gateway.bookstore.book;

import clh.comp313.gateway.bookstore.dtos.BookDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookGrpcClientService bookGrpcClientService, BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) {
        BookDto book = bookService.createBook(bookDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(book);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        BookDto bookById = bookService.getBookById(id);
        return ResponseEntity.ok(bookById);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST', 'USER')")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        List<BookDto> bookDtoList = bookService.GetAllBooks(page);
        return ResponseEntity.ok(bookDtoList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        try {
            BookDto updatedBook = bookService.updateBook(id, bookDto);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getLocalizedMessage()));
        }
    }
}
