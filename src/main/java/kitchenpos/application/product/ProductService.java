package kitchenpos.application.product;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductCreationRequest;
import kitchenpos.application.dto.result.ProductResult;
import kitchenpos.dao.product.ProductRepository;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
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
