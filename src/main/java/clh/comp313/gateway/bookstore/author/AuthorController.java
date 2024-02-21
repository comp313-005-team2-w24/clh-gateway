package clh.comp313.gateway.bookstore.author;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import clh.comp313.gateway.bookstore.dtos.AuthorsBooksDto;
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
    public ResponseEntity<List<AuthorDto>> getAllAuthors(@RequestParam(value = "page", defaultValue = "0") Integer page) {

        List<AuthorDto> allAuthors = authorService.getAllAuthors(page);

        return ResponseEntity.ok(allAuthors);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'GUEST', 'USER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        AuthorsBooksDto authorById = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorById);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/avatar")
    public ResponseEntity<AuthorDto> setAuthorAvatarUrlById(@PathVariable("id") Long id, @RequestParam("avatar_url") String avatarUrl) {
        AuthorDto authorDto = authorService.updateAuthorAvatarById(id, avatarUrl);
        return ResponseEntity.ok(authorDto);
    }
}
