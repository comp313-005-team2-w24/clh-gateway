package clh.comp313.gateway.bookstore.utils;

import clh.comp313.gateway.bookstore.dtos.BookDto;
import com.google.protobuf.Timestamp;
import io.clh.bookstore.BookOuterClass;

public class DtoProtoConversions {
    public static BookOuterClass.Book convertToBookOuterBookProto(BookDto book) {
        Timestamp timestamp = null;
        if (book.getPublicationDate() != null) {
            long millis = book.getPublicationDate().getTime();
            timestamp = Timestamp.newBuilder()
                    .setSeconds(millis / 1000)
                    .setNanos((int) ((millis % 1000) * 1000000))
                    .build();
        }

        BookOuterClass.Book.Builder bookBuilder = BookOuterClass.Book.newBuilder()
                .setPrice(book.getPrice())
                .setIsbn(book.getIsbn())
                .setDescription(book.getDescription())
                .setTitle(book.getTitle())
                .setStockQuantity(book.getStockQuantity());

        if (book.getBook_id() != null) {
            bookBuilder.setBookId(book.getBook_id());
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
