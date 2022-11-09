package kitchenpos.product.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.resitory.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest productCreateRequest) {
        Product product = new Product(productCreateRequest.getName(), new Price(productCreateRequest.getPrice()));
        Product saved = productRepository.save(product);
        return new ProductResponse(saved);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
