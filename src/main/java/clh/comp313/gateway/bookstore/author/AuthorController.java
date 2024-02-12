package clh.comp313.gateway.bookstore.author;

import clh.comp313.gateway.bookstore.dtos.Author;
import clh.comp313.gateway.bookstore.dtos.BookDto;
import clh.comp313.gateway.bookstore.dtos.authorsBooksDto;
import com.google.protobuf.util.JsonFormat;
import io.clh.bookstore.author.AuthorEntity;
import io.clh.bookstore.author.Book;
import io.clh.bookstore.author.GetAuthorByIdResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorGrpcClientService authorGrpcClientService;

    public AuthorController(AuthorGrpcClientService authorGrpcClientService) {
        this.authorGrpcClientService = authorGrpcClientService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody Author authorDto) {
        try {
            AuthorEntity grpcResponse = authorGrpcClientService.createAuthor(
                    new String(authorDto.getName()),
                    authorDto.getBiography(),
                    authorDto.getAvatar_url()
            );

            String jsonResponse = JsonFormat.printer().print(grpcResponse);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());

            return new ResponseEntity<>(Collections.singletonMap("error", e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST')")
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        Iterable<AuthorEntity> allAuthors = authorGrpcClientService.getAllAuthors();

        List<Author> authorList = new ArrayList<>();
        for (AuthorEntity resp : allAuthors) {
            Author authorDto = new Author((int) resp.getAuthorId(), resp.getName().toCharArray(), resp.getBiography(), resp.getAvatarUrl());
            authorList.add(authorDto);
        }

        return ResponseEntity.ok(authorList);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST')")
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        GetAuthorByIdResponse authorById = authorGrpcClientService.getAuthorById(Math.toIntExact(id));

        //gRPC
        AuthorEntity author = authorById.getAuthor();
        List<Book> booksList = authorById.getBooksList();

        //GetAuthorByIdResponse imp
        authorsBooksDto response = new authorsBooksDto();

        Author authorDto = new Author((int)
                author.getAuthorId(),
                author.getName().toCharArray(),
                author.getBiography(),
                author.getAvatarUrl());
        List<BookDto> bookDtoList = authorById.getBooksList().stream().map(BookDto::of).toList();

        response.setAuthor(authorDto);
        response.setBooks(bookDtoList);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST')")
    @PutMapping("/{id}/avatar")
    public ResponseEntity<Author> setAuthorAvatarUrlById(@PathVariable("id") Long id, @RequestParam("avatar_url") String avatarUrl) {
        try {
            AuthorEntity updatedAuthor = authorGrpcClientService.setAuthorAvatarUrlById(id, avatarUrl);
            Author authorDto = new Author((int) updatedAuthor.getAuthorId(), updatedAuthor.getName().toCharArray(), updatedAuthor.getBiography(), updatedAuthor.getAvatarUrl());

            return ResponseEntity.ok(authorDto);
        } catch (Exception e) {
            System.err.println("Error updating author's avatar URL: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
