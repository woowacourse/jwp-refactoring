package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.product.ProductsResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), request.getPrice());
        final Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public ProductsResponse list() {
        List<Product> products = productRepository.findAll();
        return ProductsResponse.from(products);
    }
}
