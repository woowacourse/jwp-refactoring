package kitchenpos.support;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewTestSupporter {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct() {
        final Product product = new Product("name", 10_000);
        return productRepository.save(product);
    }
}
