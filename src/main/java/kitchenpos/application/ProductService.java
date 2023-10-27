package kitchenpos.application;

import java.util.List;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.product.Name;
import kitchenpos.domain.product.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final Name name, final Price price) {
        final Product product = new Product(null, name, price);
        return ProductResponse.from(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return ProductResponse.from(productRepository.findAll());
    }
}
