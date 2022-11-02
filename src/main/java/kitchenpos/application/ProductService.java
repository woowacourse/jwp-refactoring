package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository products;

    public ProductService(final ProductRepository products) {
        this.products = products;
    }

    @Transactional
    public Product create(final Product product) {
        return products.add(product);
    }

    public List<Product> list() {
        return products.getAll();
    }
}
