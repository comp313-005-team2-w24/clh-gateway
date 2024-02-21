package clh.comp313.gateway.bookstore.dtos;

import lombok.*;

@RequiredArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Builder
public class AuthorDto {
    private Long author_id;
    private char[] name;
    private String biography;
    private String avatar_url;
}
