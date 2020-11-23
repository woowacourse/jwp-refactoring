package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        Product product = productCreateRequest.toProduct();
        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> listAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
