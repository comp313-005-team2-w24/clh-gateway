package clh.comp313.gateway.bookstore.author;

import clh.comp313.gateway.bookstore.dtos.AuthorDto;
import clh.comp313.gateway.bookstore.dtos.AuthorsBooksDto;
import clh.comp313.gateway.bookstore.dtos.BookDto;
import clh.comp313.gateway.bookstore.utils.GrpcToDtoConverter;
import io.clh.bookstore.author.Author;
import io.clh.bookstore.entities.Entities;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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


    public List<AuthorDto> getAllAuthors(Integer page) {
        Author.GetAllAuthorsRequest request = Author.GetAllAuthorsRequest.newBuilder().setPage(page).build();

        Iterable<Entities.AuthorEntity> allAuthors = authorGrpcClientService.getAllAuthors(request);

        List<AuthorDto> authorDtoList = new ArrayList<>();
        for (Entities.AuthorEntity resp : allAuthors) {
            AuthorDto authorDto = new AuthorDto(resp.getAuthorId(), resp.getName(), resp.getBiography(), resp.getAvatarUrl());
            authorDtoList.add(authorDto);
        }

        return authorDtoList;
    }

    public AuthorsBooksDto getAuthorById(Long authorId) {
        Author.AuthorByIdRequest build = Author.AuthorByIdRequest.newBuilder().setAuthorId(authorId).build();

        Author.GetAuthorByIdResponse response = authorGrpcClientService.getAuthorById(build);
        AuthorDto authorDto = AuthorEntityToAuthorDto(response.getAuthor());
        List<BookDto> bookDtoList = response.getBooksList().stream().map(GrpcToDtoConverter::BookGrpcToBookDto).toList();

        return AuthorsBooksDto.builder().author(authorDto).books(bookDtoList).build();
    }

    public AuthorDto updateAuthorAvatarById(Long id, String avatar_url) {
        Author.AuthorAvatarUrlRequest build = Author.AuthorAvatarUrlRequest.newBuilder().setAuthorId(id).setAvatarUrl(avatar_url).build();
        Entities.AuthorEntity authorEntity = authorGrpcClientService.setAuthorAvatarUrlById(build);
        return AuthorEntityToAuthorDto(authorEntity);
    }
}
