package clh.comp313.gateway.bookstore.author;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import clh.comp313.gateway.bookstore.dtos.BookDto;
import clh.comp313.gateway.bookstore.dtos.authorsBooksDto;
import io.clh.bookstore.author.AuthorEntity;
import io.clh.bookstore.author.Book;
import io.clh.bookstore.author.GetAuthorByIdResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorGrpcClientService authorGrpcClientService, AuthorService authorService) {
        this.authorService = authorService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto author = authorService.createAuthor(authorDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(author);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST', 'USER')")
    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> allAuthors = authorService.getAllAuthors();

        return ResponseEntity.ok(allAuthors);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'GUEST', 'USER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        GetAuthorByIdResponse authorById = authorGrpcClientService.getAuthorById(Math.toIntExact(id));

        //gRPC
        AuthorEntity author = authorById.getAuthor();
        List<Book> booksList = authorById.getBooksList();

        //GetAuthorByIdResponse imp
        authorsBooksDto response = new authorsBooksDto();

        AuthorDto authorDto = new AuthorDto((int)
                author.getAuthorId(),
                author.getName().toCharArray(),
                author.getBiography(),
                author.getAvatarUrl());
        List<BookDto> bookDtoList = authorById.getBooksList().stream().map(BookDto::of).toList();

        response.setAuthorDto(authorDto);
        response.setBooks(bookDtoList);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/avatar")
    public ResponseEntity<AuthorDto> setAuthorAvatarUrlById(@PathVariable("id") Long id, @RequestParam("avatar_url") String avatarUrl) {
        try {
            AuthorEntity updatedAuthor = authorGrpcClientService.setAuthorAvatarUrlById(id, avatarUrl);
            AuthorDto authorDto = new AuthorDto((int) updatedAuthor.getAuthorId(), updatedAuthor.getName().toCharArray(), updatedAuthor.getBiography(), updatedAuthor.getAvatarUrl());

            return ResponseEntity.ok(authorDto);
        } catch (Exception e) {
            System.err.println("Error updating author's avatar URL: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
