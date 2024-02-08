package clh.comp313.gateway.bookstore.author.controllers;

import clh.comp313.gateway.bookstore.author.dtos.AuthorDtO;
import clh.comp313.gateway.bookstore.author.dtos.BookDto;
import clh.comp313.gateway.bookstore.author.dtos.GetAuthorByIdResponseRest;
import clh.comp313.gateway.bookstore.author.services.AuthorGrpcClientService;
import com.google.protobuf.util.JsonFormat;
import io.clh.bookstore.author.AuthorEntity;
import io.clh.bookstore.author.Book;
import io.clh.bookstore.author.GetAuthorByIdResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDtO authorDto) {
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


    @GetMapping
    public ResponseEntity<List<AuthorDtO>> getAllAuthors() {
        Iterable<AuthorEntity> allAuthors = authorGrpcClientService.getAllAuthors();

        List<AuthorDtO> authorList = new ArrayList<>();
        for (AuthorEntity resp : allAuthors) {
            AuthorDtO authorDto = new AuthorDtO((int) resp.getAuthorId(), resp.getName().toCharArray(), resp.getBiography(), resp.getAvatarUrl());
            authorList.add(authorDto);
        }

        return ResponseEntity.ok(authorList);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        GetAuthorByIdResponse authorById = authorGrpcClientService.getAuthorById(Math.toIntExact(id));

        //gRPC
        AuthorEntity author = authorById.getAuthor();
        List<Book> booksList = authorById.getBooksList();

        //GetAuthorByIdResponse imp
        GetAuthorByIdResponseRest response = new GetAuthorByIdResponseRest();

        AuthorDtO authorDto = new AuthorDtO((int)
                author.getAuthorId(),
                author.getName().toCharArray(),
                author.getBiography(),
                author.getAvatarUrl());
        List<BookDto> bookDtoList = authorById.getBooksList().stream().map(BookDto::of).toList();

        response.setAuthor(authorDto);
        response.setBooks(bookDtoList);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/avatar")
    public ResponseEntity<AuthorDtO> setAuthorAvatarUrlById(@PathVariable("id") Long id, @RequestParam("avatar_url") String avatarUrl) {
        try {
            AuthorEntity updatedAuthor = authorGrpcClientService.setAuthorAvatarUrlById(id, avatarUrl);
            AuthorDtO authorDto = new AuthorDtO((int) updatedAuthor.getAuthorId(), updatedAuthor.getName().toCharArray(), updatedAuthor.getBiography(), updatedAuthor.getAvatarUrl());

            return ResponseEntity.ok(authorDto);
        } catch (Exception e) {
            System.err.println("Error updating author's avatar URL: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
