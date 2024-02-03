package clh.comp313.gateway.bookstore.author.controllers;

import clh.comp313.gateway.bookstore.author.dtos.AuthorDTO;
import clh.comp313.gateway.bookstore.author.services.AuthorGrpcClientService;
import com.google.protobuf.util.JsonFormat;
import io.clh.bookstore.author.AuthorEntity;
import io.clh.bookstore.author.CreateAuthorResponse;
import io.clh.bookstore.author.GetAllAuthorsResponse;
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
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDTO authorDto) {
        try {
            CreateAuthorResponse grpcResponse = authorGrpcClientService.createAuthor(new String(authorDto.getName()), authorDto.getBiography());

            String jsonResponse = JsonFormat.printer().print(grpcResponse);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());

            return new ResponseEntity<>(Collections.singletonMap("error", e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        Iterable<GetAllAuthorsResponse> allAuthors = authorGrpcClientService.getAllAuthors();

        List<AuthorDTO> authorList = new ArrayList<>();
        for (GetAllAuthorsResponse resp : allAuthors) {
            AuthorDTO authorDto = new AuthorDTO((int) resp.getAuthorId(), resp.getName().toCharArray(), resp.getBiography());
            authorList.add(authorDto);
        }

        return ResponseEntity.ok(authorList);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        AuthorEntity authorById = authorGrpcClientService.getAuthorById(Math.toIntExact(id));
        AuthorDTO authorDto = new AuthorDTO((int) authorById.getAuthorId(), authorById.getName().toCharArray(), authorById.getBiography());

        return ResponseEntity.ok(authorDto);
    }
}
