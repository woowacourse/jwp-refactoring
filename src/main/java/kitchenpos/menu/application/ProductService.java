package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.menu.ui.dto.request.ProductCreateRequest;
import kitchenpos.menu.ui.dto.response.ProductCreateResponse;
import kitchenpos.menu.ui.dto.response.ProductFindAllResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductCreateResponse create(final ProductCreateRequest request) {
        final Product product = productRepository.save(toProduct(request));
        return ProductCreateResponse.from(product);
    }

    private Product toProduct(final ProductCreateRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
    }

    public List<ProductFindAllResponse> list() {
        final List<Product> products = productRepository.findAll();
        return ProductFindAllResponse.from(products);
    }
}
