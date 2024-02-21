package clh.comp313.gateway.bookstore.dtos;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AuthorsBooksDto {
    private AuthorDto authorDto;
    private List<BookDto> books;
}
