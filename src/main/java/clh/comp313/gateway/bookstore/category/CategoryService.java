package clh.comp313.gateway.bookstore.category;

import clh.comp313.gateway.bookstore.dtos.BookDto;
import clh.comp313.gateway.bookstore.dtos.CategoryDto;
import clh.comp313.gateway.bookstore.utils.DtoToGrpcConverter;
import clh.comp313.gateway.bookstore.utils.GrpcToDtoConverter;
import com.google.protobuf.Empty;
import io.clh.bookstore.category.Category;
import io.clh.bookstore.entities.Entities;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryGrpcClientService categoryGrpcClientService;

    public CategoryService(CategoryGrpcClientService categoryGrpcClientService) {
        this.categoryGrpcClientService = categoryGrpcClientService;
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        Entities.Category category = DtoToGrpcConverter.CategoryDtoToCategoryEntity(categoryDto);
        Entities.Category grpcResponse = categoryGrpcClientService.addCategory(category);
        return GrpcToDtoConverter.CategoryEntityToCategoryDto(grpcResponse);
    }

    public CategoryDto getCategoryById(Long id) {
        Category.GetCategoryByIdRequest request = Category.GetCategoryByIdRequest.newBuilder().setCategoryId(id).build();
        Entities.Category grpcResponse = categoryGrpcClientService.getCategoryById(request);
        return GrpcToDtoConverter.CategoryEntityToCategoryDto(grpcResponse);
    }

    public List<CategoryDto> getAllCategories() {
        Iterator<Entities.Category> grpcResponse = categoryGrpcClientService.getAllCategories(Empty.getDefaultInstance());
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        while (grpcResponse.hasNext()) {
            Entities.Category category = grpcResponse.next();
            categoryDtoList.add(GrpcToDtoConverter.CategoryEntityToCategoryDto(category));
        }
        return categoryDtoList;
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        categoryDto.setId(id);
        Entities.Category category = DtoToGrpcConverter.CategoryDtoToCategoryEntity(categoryDto);
        Entities.Category grpcResponse = categoryGrpcClientService.updateCategory(category);
        return GrpcToDtoConverter.CategoryEntityToCategoryDto(grpcResponse);
    }

    public CategoryDto deleteCategory(Long id) {
        Category.DeleteCategoryRequest request = Category.DeleteCategoryRequest.newBuilder().setCategoryId(Math.toIntExact(id)).build();
        Entities.Category grpcResponse = categoryGrpcClientService.deleteCategory(request);
        return GrpcToDtoConverter.CategoryEntityToCategoryDto(grpcResponse);
    }

    public List<BookDto> getAllBooksByCategory(Long categoryId) {
        Category.GetAllBooksByCategoryRequest request = Category.GetAllBooksByCategoryRequest.newBuilder().setCategoryId(Math.toIntExact(categoryId)).build();
        Iterator<Entities.Book> grpcResponse = categoryGrpcClientService.getAllBooksByCategory(request);
        List<BookDto> booksByCategoryList = new ArrayList<>();
        while (grpcResponse.hasNext()) {
            Entities.Book book = grpcResponse.next();
            booksByCategoryList.add(GrpcToDtoConverter.BookGrpcToBookDto(book));
        }

        return booksByCategoryList;
    }
}
