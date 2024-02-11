package clh.comp313.gateway.bookstore.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.protobuf.Timestamp;
import io.clh.bookstore.author.Book;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class BookDto {
    private Integer book_id;

    private String title;
    private String description;
    private String isbn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date publicationDate;

    private Double price;
    private Integer stockQuantity;
    private List<Long> authorIds;
    private String avatar_url;

    public static BookDto of(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setBook_id((int) book.getBookId());
        bookDto.setTitle(book.getTitle());
        bookDto.setDescription(book.getDescription());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setStockQuantity(book.getStockQuantity());
        bookDto.setAvatar_url(book.getAvatarUrl());

        List<Long> authors = book.getAuthorIdsList().stream().toList();
        bookDto.setAuthorIds(authors);

        if (book.hasPublicationDate()) {
            Timestamp ts = book.getPublicationDate();
            long millis = ts.getSeconds() * 1000 + ts.getNanos() / 1000000;
            bookDto.setPublicationDate(new java.sql.Date(millis));
        }



        return bookDto;
    }
}