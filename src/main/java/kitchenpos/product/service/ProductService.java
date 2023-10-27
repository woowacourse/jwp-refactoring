package kitchenpos.product.service;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.infra.ProductRepository;
import kitchenpos.vo.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(String name, long price) {
        Product product = new Product(name, new Price(price));
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }
}
