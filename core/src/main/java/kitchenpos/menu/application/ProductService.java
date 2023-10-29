package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.ProductCreateRequest;
import kitchenpos.menu.application.dto.ProductResponse;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long create(final ProductCreateRequest request) {
        final Product product = Product.of(request.getName(), request.getPrice());
        final Product saveProduct = productRepository.save(product);
        return saveProduct.getId();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
