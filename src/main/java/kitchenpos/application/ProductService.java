package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.ui.request.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(ProductCreateRequest request) {
        Product product = Product.of(request.getName(), request.getPrice());
        productRepository.save(product);

        return product;
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

}
