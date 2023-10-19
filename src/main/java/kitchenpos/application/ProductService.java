package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductCreateRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = productRepository.save(request.toEntity());
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public Price calculatePrice(final long id, final long quantity) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return product.calculatePrice(quantity);
    }
}
