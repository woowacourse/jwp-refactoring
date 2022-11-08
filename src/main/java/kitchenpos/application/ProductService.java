package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.ui.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository products;

    public ProductService(final ProductRepository products) {
        this.products = products;
    }

    @Transactional
    public Product create(final ProductRequest request) {
        return products.add(request.toEntity());
    }

    public List<Product> list() {
        return products.getAll();
    }
}
