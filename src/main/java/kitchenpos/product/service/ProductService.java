package kitchenpos.product.service;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.reqeust.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.product.dto.response.ProductsResponse;
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
