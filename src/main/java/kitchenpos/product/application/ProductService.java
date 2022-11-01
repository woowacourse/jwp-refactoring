package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.application.dto.request.ProductCommand;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductCommand productCommand) {
        Product product = productRepository.save(productCommand.toEntity());
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }
}
