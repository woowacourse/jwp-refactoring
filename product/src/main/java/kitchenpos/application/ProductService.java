package kitchenpos.application;

import kitchenpos.Product;
import kitchenpos.ProductName;
import kitchenpos.ProductPrice;
import kitchenpos.ProductRepository;
import kitchenpos.application.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(final ProductRequest request) {
        return productRepository.save(
                new Product(new ProductName(request.getName()), new ProductPrice(request.getPrice()))
        );
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }
}
