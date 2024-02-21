package clh.comp313.gateway.bookstore.utils;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import clh.comp313.gateway.bookstore.dtos.BookDto;
import com.google.protobuf.Timestamp;
import io.clh.bookstore.entities.Entities;

import java.util.Arrays;

public class DtoToGrpcConverter {
    public static Entities.AuthorEntity AuthorDtoToAuthorEntity(AuthorDto authorDto) {
        Entities.AuthorEntity.Builder builder = Entities.AuthorEntity.newBuilder();

        if (authorDto.getAuthor_id() != null) {
            builder.setAuthorId(authorDto.getAuthor_id());
        }

        return builder
                .setName(Arrays.toString(authorDto.getName()))
                .setBiography(authorDto.getBiography())
                .setAvatarUrl(authorDto.getAvatar_url())
                .build();
    }


    public static Entities.Book BookDtoToBookEntity(BookDto book) {
        Timestamp timestamp = null;
        if (book.getPublicationDate() != null) {
            long millis = book.getPublicationDate().getTime();
            timestamp = Timestamp.newBuilder().setSeconds(millis / 1000).setNanos((int) ((millis % 1000) * 1000000)).build();
        }

        Entities.Book.Builder bookBuilder = Entities.Book.newBuilder();

        if (book.getBook_id() != null) {
            bookBuilder.setBookId(book.getBook_id());
        }

        if (book.getPrice() != null) {
            bookBuilder.setPrice(book.getPrice());
        }

        if (book.getIsbn() != null) {
            bookBuilder.setIsbn(book.getIsbn());
        }

        if (book.getDescription() != null) {
            bookBuilder.setDescription(book.getDescription());
        }

        if (book.getTitle() != null) {
            bookBuilder.setTitle(book.getTitle());
        }

        if (book.getStockQuantity() != null) {
            bookBuilder.setStockQuantity(book.getStockQuantity());
        }

        if (book.getAvatar_url() != null) {
            bookBuilder.setAvatarUrl(book.getAvatar_url());
        }

        if (timestamp != null) {
            bookBuilder.setPublicationDate(timestamp);
        }

        if (book.getAuthorIds() != null && !book.getAuthorIds().isEmpty()) {
            bookBuilder.addAllAuthorIds(book.getAuthorIds());
        }

        return bookBuilder.build();
    }

}
