package clh.comp313.gateway.bookstore.utils;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import clh.comp313.gateway.bookstore.dtos.BookDto;
import clh.comp313.gateway.bookstore.dtos.CategoryDto;
import com.google.protobuf.Timestamp;
import io.clh.bookstore.entities.Entities;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GrpcToDtoConverter {
    public static AuthorDto AuthorEntityToAuthorDto(Entities.AuthorEntity entity) {
        return AuthorDto.builder()
                .author_id(entity.getAuthorId())
                .name(entity.getName().toCharArray())
                .avatar_url(entity.getAvatarUrl())
                .biography(entity.getBiography())
                .build();
    }


    public static BookDto BookGrpcToBookDto(Entities.Book book) {
        List<Long> authors = book.getAuthorIdsList().stream().toList();
        Date date = null;

        if (book.hasPublicationDate()) {
            Timestamp ts = book.getPublicationDate();
            long millis = ts.getSeconds() * 1000 + ts.getNanos() / 1000000;
            date = new Date(millis);
        }

        return BookDto.builder()
                .book_id(book.getBookId())
                .title(book.getTitle())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .avatar_url(book.getAvatarUrl())
                .authorIds(authors)
                .publicationDate(date)
                .build();
    }

    public static CategoryDto CategoryEntityToCategoryDto(Entities.Category category) {
        List<BookDto> bookDtos = category.getBooksList().stream()
                .map(GrpcToDtoConverter::BookGrpcToBookDto)
                .collect(Collectors.toList());

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .books(bookDtos)
                .build();
    }
}
