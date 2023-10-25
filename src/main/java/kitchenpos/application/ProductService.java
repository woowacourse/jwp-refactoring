package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.repository.ProductRepository;
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
    public Product create(ProductRequest productRequest) {

        Product product = new Product(productRequest.getName(), productRequest.getPrice());

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
