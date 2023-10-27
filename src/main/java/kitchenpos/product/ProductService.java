package kitchenpos.product;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final JpaProductRepository jpaProductRepository;

    public ProductService(final JpaProductRepository JpaProductRepository) {
        this.jpaProductRepository = JpaProductRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Price price = new Price(request.getPrice());
        Product product = new Product(request.getName(), price);

        Product savedProduct = jpaProductRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> savedProducts = jpaProductRepository.findAll();

        return savedProducts.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
