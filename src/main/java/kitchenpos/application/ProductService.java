package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.ProductsResponse;
import kitchenpos.dao.ProductRepository;
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
        Product product = request.toEntity();
        productRepository.save(product);
        return ProductResponse.from(product);
    }

    public ProductsResponse list() {
        List<Product> products = productRepository.findAll();
        return ProductsResponse.from(products);
    }
}
