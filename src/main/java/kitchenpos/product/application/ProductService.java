package kitchenpos.product.application;

import kitchenpos.product.Product;
import kitchenpos.product.ProductName;
import kitchenpos.product.ProductPrice;
import kitchenpos.product.ProductRepository;
import kitchenpos.product.application.request.ProductRequest;
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
