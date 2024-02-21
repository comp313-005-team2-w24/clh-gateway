package clh.comp313.gateway.bookstore.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Builder
public class BookDto {
    private Long book_id;

    private String title;
    private String description;
    private String isbn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date publicationDate;

    private Double price;
    private Integer stockQuantity;
    private List<Long> authorIds;
    private String avatar_url;
}