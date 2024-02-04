package clh.comp313.gateway.bookstore.author.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AuthorDTO {
    private int author_id;
    private char[] name;
    private String biography;
    private String avatar_url;
}
