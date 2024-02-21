package clh.comp313.gateway.bookstore.author;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import io.clh.bookstore.author.Author;
import io.clh.bookstore.entities.Entities;
import org.springframework.stereotype.Service;

import static clh.comp313.gateway.bookstore.utils.DtoToGrpcConverter.AuthorDtoToAuthorEntity;
import static clh.comp313.gateway.bookstore.utils.GrpcToDtoConverter.AuthorEntityToAuthorDto;

@Service
public class AuthorService {
    private final AuthorGrpcClientService authorGrpcClientService;

    public AuthorService(AuthorGrpcClientService authorGrpcClientService) {
        this.authorGrpcClientService = authorGrpcClientService;
    }

    public AuthorDto createAuthor(AuthorDto authorDto) {
        Entities.AuthorEntity authorEntityRequest = AuthorDtoToAuthorEntity(authorDto);
        Author.CreateAuthorRequest request = Author.CreateAuthorRequest.newBuilder().setAuthor(authorEntityRequest).build();

        Entities.AuthorEntity author = authorGrpcClientService.createAuthor(request);

        return AuthorEntityToAuthorDto(author);
    }


}
