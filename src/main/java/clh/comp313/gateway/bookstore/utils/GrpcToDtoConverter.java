package clh.comp313.gateway.bookstore.utils;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import io.clh.bookstore.entities.Entities;

public class GrpcToDtoConverter {
    public static AuthorDto AuthorEntityToAuthorDto(Entities.AuthorEntity entity) {
        return AuthorDto.builder()
                .author_id(entity.getAuthorId())
                .name(entity.getName().toCharArray())
                .avatar_url(entity.getAvatarUrl())
                .biography(entity.getBiography())
                .build();
    }
}
