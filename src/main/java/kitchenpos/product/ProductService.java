package kitchenpos.product;

import kitchenpos.common.Price;
import kitchenpos.ui.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest product) {
        return productRepository.save(new Product(
                product.getName(),
                new Price(product.getPrice())
        ));
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }
}
