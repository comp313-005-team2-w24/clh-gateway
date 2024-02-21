package clh.comp313.gateway.bookstore.category;

import com.google.protobuf.Empty;
import org.springframework.stereotype.Service;
import io.clh.bookstore.category.Category;
import io.clh.bookstore.category.CategoryServiceGrpc;
import io.clh.bookstore.entities.Entities;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
@Service
public class CategoryGrpcClientService {
    private final CategoryServiceGrpc.CategoryServiceBlockingStub blockingStub;

    public CategoryGrpcClientService(@Value("${BOOKSTORE_GRPC_HOST:localhost}") String host, @Value("${BOOKSTORE_GRPC_PORT:8082}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext() // No SSL/TLS
                .build();
        blockingStub = CategoryServiceGrpc.newBlockingStub(channel);
    }

    public Iterator<Entities.Book> getAllBooksByCategory(Category.GetAllBooksByCategoryRequest request) {
        return blockingStub.getAllBooksByCategory(request);
    }

    public Entities.Category addCategory(Entities.Category category) {
        return blockingStub.addCategory(category);
    }

    public Entities.Category deleteCategory(Category.DeleteCategoryRequest request) {
        return blockingStub.deleteCategory(request);
    }

    public Iterator<Entities.Category> getAllCategories(Empty request) {
        return blockingStub.getAllCategories(request);
    }

    public Entities.Category updateCategory(Entities.Category category) {
        return blockingStub.updateCategory(category);
    }

    public Entities.Category getCategoryById(Category.GetCategoryByIdRequest request) {
        return blockingStub.getCategoryById(request);
    }
}
