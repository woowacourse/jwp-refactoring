package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import kitchenpos.ui.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = productRepository.save(
                Product.of(
                        request.getName(),
                        request.getPrice()
                )
        );
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(it -> new ProductResponse(it.getId(), it.getName(), it.getPrice()))
                .collect(Collectors.toList());
    }
}
