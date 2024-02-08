package clh.comp313.gateway.bookstore.dtos;

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
public class Author {
    private int author_id;
    private char[] name;
    private String biography;
    private String avatar_url;
}
