package clh.comp313.gateway.bookstore.dtos;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Builder
public class AuthorsBooksDto {
    private AuthorDto author;
    private List<BookDto> books;
}
