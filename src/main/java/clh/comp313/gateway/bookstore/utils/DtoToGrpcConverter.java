package clh.comp313.gateway.bookstore.utils;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import io.clh.bookstore.entities.Entities;

import java.util.Arrays;

public class DtoToGrpcConverter {
    public static Entities.AuthorEntity AuthorDtoToAuthorEntity(AuthorDto authorDto) {
        return Entities.AuthorEntity.newBuilder()
                .setAuthorId(authorDto.getAuthor_id())
                .setName(Arrays.toString(authorDto.getName()))
                .setBiography(authorDto.getBiography())
                .setAvatarUrl(authorDto.getAvatar_url())
                .build();
    }
}
