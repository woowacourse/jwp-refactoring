package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.CreateProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final CreateProductDto request) {
        final Product product = new Product(request.getName(), request.getPrice());

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
