package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.generic.domain.Price;
import kitchenpos.product.application.dto.ProductCreationRequest;
import kitchenpos.product.application.dto.ProductResult;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResult create(final ProductCreationRequest request) {
        final Product product = new Product(request.getName(), Price.from(request.getPrice()));
        return ProductResult.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResult> list() {
        return productRepository.findAll().stream()
                .map(ProductResult::from)
                .collect(Collectors.toList());
    }
}
