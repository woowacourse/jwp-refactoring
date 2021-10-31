package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product newProduct = new Product(productRequest.getName(), productRequest.getPrice());
        productRepository.save(newProduct);
        return new ProductResponse(newProduct);
    }

    public List<ProductResponse> findAll() {
        final List<Product> foundAllProducts = productRepository.findAll();
        return foundAllProducts.stream()
            .map(ProductResponse::new)
            .collect(Collectors.toList());
    }
}
