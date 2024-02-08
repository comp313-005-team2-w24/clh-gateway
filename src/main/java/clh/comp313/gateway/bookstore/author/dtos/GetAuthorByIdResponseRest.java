package clh.comp313.gateway.bookstore.author.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class GetAuthorByIdResponseRest {
    private AuthorDtO author;
    private List<BookDto> books;
}
