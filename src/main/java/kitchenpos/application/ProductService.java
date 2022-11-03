package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        Product product = request.toEntity();

        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> savedProducts = productRepository.findAll();
        return toProductResponses(savedProducts);
    }

    private List<ProductResponse> toProductResponses(List<Product> savedProducts) {
        return savedProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
