package clh.comp313.gateway.bookstore.dtos;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private List<BookDto> books;
}
