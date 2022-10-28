package kitchenpos.application;

import kitchenpos.application.dto.ProductRequest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice());
        return productRepository.save(product);

    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
