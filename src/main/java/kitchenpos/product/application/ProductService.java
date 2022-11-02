package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        Product product = new Product(request.getName(), request.getPrice());

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
